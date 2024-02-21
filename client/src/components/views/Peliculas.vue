<template>
  <div>
    <div v-if="loading" class="overlay">
      <h2>Loading...</h2>
    </div>
    <b-container class="mt-5">
      <b-row class="text-right">
        <b-col cols="3" style="justify-content: right;">
          <b-form-input v-model="filtro" placeholder="Buscar por nombre" />
        </b-col>
      </b-row>
      <b-row>
        <!-- Iteración sobre las películas -->
        <transition-group name="fade" tag="div" class="col">
          <b-col cols="4" md="8" v-for="(movie, index) in listFilteredMovies" :key="index">
            <b-card class="mt-3 text-center movies-card">
              <img src="https://source.unsplash.com/category/movies/300x400" width="200px" height="300px">
              <h3 class="mt-3">{{ movie.nombre }}</h3>
              <b-card-text>
                <p>Director: {{ movie.director }}</p>
                <p>Duración: {{ movie.duracion }}</p>
                <p>Descripción: {{ movie.descripcion }}</p>
                <p>Género: {{ generos.find(gen => parseInt(gen.id) === parseInt(movie.generoId))?.text }}</p>
              </b-card-text>
              <b-card-content>
                <b-button class="m-2" @click="deleteMovie(movie.id)" variant="danger">Eliminar</b-button>
                <b-button class="m-2" @click="editMovie(movie.id)" variant="warning">Editar</b-button>
              </b-card-content>
            </b-card>
          </b-col>
        </transition-group>

        <!-- Formulario fijo en la pantalla -->
        <b-col cols="12" md="4" style="position: fixed; top: 90px; right: 0; z-index: 1000; height: calc(100vh - 30px); overflow-y: auto;">
          <div style="border-color: black; border-radius: 10%; border-width: 1px;">
            <b-form @submit.prevent="addNewMovie">
              <div class="drag-container">
                <b-form-group draggable="true" aria-placeholder="El gato con botas" label="Película*" @dragstart="handleDragStart" @dragover.prevent="handleDragOver" @drop="handleDrop">
                  <b-form-input v-model="newMovie.nombre" required></b-form-input>
                </b-form-group>
                <b-form-group draggable="true" label="Director*" @dragstart="handleDragStart" @dragover.prevent="handleDragOver" @drop="handleDrop">
                  <b-form-input aria-placeholder="Guillermo del Toro" v-model="newMovie.director" required></b-form-input>
                </b-form-group>
                <b-form-group draggable="true" label="Duración (En minutos)*" @dragstart="handleDragStart" @dragover.prevent="handleDragOver" @drop="handleDrop">
                  <b-form-input aria-placeholder="120" v-model="newMovie.duracion" type="text" pattern="\d*" required></b-form-input>
                </b-form-group>
                <b-form-group draggable="true" label="Descripción">
                  <b-form-textarea v-model="newMovie.descripcion"></b-form-textarea>
                </b-form-group>
                <b-form-group draggable="true" label="Género*">
                  <b-form-select v-model="newMovie.generoId" :options="generos" :value="newMovie.generoId" required></b-form-select>
                </b-form-group>
              </div>
              <b-button class="m-2" type="submit" variant="primary">Guardar Cambios</b-button>
              <b-button class="m-2" onclick="cancelMovie()" type="buttom" variant="warning">Cancelar</b-button>
            </b-form>
          </div>
        </b-col>
      </b-row>

      <!-- Modal de edición -->
      <b-modal v-model="showModal" title="Editar Película" class="draggable-modal" ref="draggableModal" @dragover.prevent @drop="handleDrop">
        <div>
          <b-form @submit.prevent="updateMovie">
            <div class="drag-container">
              <b-form-group draggable="true" aria-placeholder="El gato con botas" label="Película*">
                <b-form-input v-model="editedMovie.nombre" required></b-form-input>
              </b-form-group>
              <b-form-group draggable="true" label="Director*">
                <b-form-input aria-placeholder="Guillermo del Toro" v-model="editedMovie.director" required></b-form-input>
              </b-form-group>
              <b-form-group draggable="true" label="Duración (En minutos)*">
                <b-form-input aria-placeholder="120" v-model="editedMovie.duracion" type="text" pattern="\d*" required></b-form-input>
              </b-form-group>
              <b-form-group draggable="true" label="Descripción">
                <b-form-textarea v-model="editedMovie.descripcion"></b-form-textarea>
              </b-form-group>
              <b-form-group draggable="true" label="Género*">
                <b-form-select v-model="editedMovie.generoId" :options="generos" :value="editedMovie.generoId" required></b-form-select>
              </b-form-group>
            </div>
          </b-form>
        </div>
      </b-modal>
    </b-container>
  </div>
</template>

<script>
import movieService from "../../services/MoviesService";
import genderService from "../../services/GenderService";

export default {
  data() {
    return {
      filtro: "",
      perPage: 8,
      currentPage: 1,
      rows: 0,
      movies: [],
      listFilteredMovies: [],
      showModal: false,
      isEditing: false,
      loading: false,
      editedMovieGenero: 0,
      editedMovie: {
        nombre: "",
        director: "",
        duracion: "",
        descripcion: "",
        generoId: 0,
      },
      newMovie: {
        nombre: "",
        director: "",
        duracion: "",
        descripcion: "",
        generoId: 0,
      },
      generos: [
        { value: 0, text: "Selecciona el género" },
      ],
      showElement: true,
      lastScrollPosition: 0,
      isDragging: false,
      dragStartX: 0,
      dragStartY: 0,
      modalOffsetX: 0,
      modalOffsetY: 0,
      draggedLabel: "Película*",
    };
  },
  mounted() {
    this.obtenerGeneros();
    this.obtenerPeliculas();
    window.addEventListener("scroll", this.onScroll);
  },
  beforeDestroy() {
    window.removeEventListener("scroll", this.onScroll);
  },
  watch: {
    filtro: 'filterMovies'
  },
  methods: {
    async obtenerPeliculas() {
      try {
        this.loading = true;
        const data = await movieService.obtenerPeliculas();
        this.movies = data;
        this.filterMovies();
      } catch (error) {
        console.error(error);
      } finally {
        setTimeout(() => {
          this.loading = false;
        }, 1000);
      }
    },

    async obtenerGeneros() {
      try {
        const data = await genderService.obtenerGeneros();
        data.forEach(e => {
          this.generos.push({
            "id": e.id,
            "text": e.genero
          });
        });
      } catch (error) {
        console.error(error);
      }
    },

    onScroll() {
      const currentScrollPosition = window.pageYOffset || document.documentElement.scrollTop;

      if (Math.abs(currentScrollPosition - this.lastScrollPosition) < 60) {
        return;
      }
      this.showElement = currentScrollPosition < this.lastScrollPosition;
      this.lastScrollPosition = currentScrollPosition;
    },

    async deleteMovie(idMovie) {
      try {
        this.loading = true;
        await movieService.eliminarPelicula(idMovie);
        this.obtenerPeliculas();
      } catch (error) {
        console.error(error);
      } finally {
        setTimeout(() => {
          this.loading = false;
        }, 1000);
      }
    },

    async editMovie(idMovie) {
      const data = this.movies.find((mov) => parseInt(mov.id) === parseInt(idMovie));
      this.editedMovie = { ...data };
      this.editedMovie.generoId = data.generoId;
      this.editedMovieGenero = this.generos.find((genero) => parseInt(genero.id) === parseInt(this.editedMovie.generoId));
      this.isEditing = true;
      this.showModal = true;
    },

    async addNewMovie() {
      this.loading = true;

      try {
        await movieService.guardarPelicula(this.newMovie);
        this.obtenerPeliculas();
      } catch (error) {
        console.error(error);
      } finally {
        setTimeout(() => {
          this.loading = false;
        }, 1000);
      }
    },

    cancelMovie() {
      this.newMovie = {
        nombre: "",
        director: "",
        duracion: "",
        descripcion: "",
        generoId: 0,
      };
    },

    filterMovies() {
      const query = this.filtro.toLowerCase();
      this.listFilteredMovies = this.movies.filter(movie =>
        movie.director.toLowerCase().includes(query) ||
        movie.nombre.toLowerCase().includes(query)
      );
    },

    handleDrop(event) {
      // Aquí implementar la lógica para manejar el evento de soltar (drag and drop)
      // Puedes acceder a la información del elemento soltado a través del evento.
      // Por ejemplo, event.dataTransfer.getData('text') te dará el ID del elemento soltado.
    },
  },
};
</script>


<style>
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.drag-container {
  width: 300px;
  border: 2px dashed #ccc;
  padding: 20px;
}

.movies-card {
  opacity: 1;
  transition: opacity 0.5s, transform 0.5s;
  margin-bottom: 20px;
  justify-content: center !important;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s, transform 0.5s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
  transform: translateY(30px);
}

@media (min-width: 576px) {
  .movies-card {
    flex: 0 0 calc(70% - 20px);
    margin: 10px;
  }
}
</style>

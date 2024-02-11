<template>
  <div>
    <div v-if="loading" class="overlay">
      <h2>Loading...</h2>
    </div>
    <b-container class="mt-5">
      <b-row class="text-right">
        <b-col style="justify-content: right;">
          <b-button @click="openModal" variant="outline-primary">Nueva Película</b-button>
        </b-col>
      </b-row>
      <b-row>
        <b-col cols="4" v-for="(movie, index) in filteredMovies" :key="index">
          <b-card class="mt-3 text-center" style="box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); width: 350px; height: 600px;">
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
      </b-row>

      <div class="overflow-auto mt-5">
        <!-- <b-pagination v-model="currentPage" :total-rows="rows" :per-page="perPage" aria-controls="my-table"></b-pagination>
      <p class="mt-3">Página actual: {{ currentPage }}</p> -->
      </div>

      <!-- Modal de edición -->
      <b-modal v-model="showModal" title="Editar Película">
        <b-form @submit.prevent="updateMovie">
          <b-form-group aria-placeholder="El gato con botas" label="Película*">
            <b-form-input v-model="editedMovie.nombre" required></b-form-input>
          </b-form-group>
          <b-form-group label="Director*">
            <b-form-input aria-placeholder="Guillermo del Toro" v-model="editedMovie.director" required></b-form-input>
          </b-form-group>
          <b-form-group label="Duración (En minutos)*">
            <b-form-input aria-placeholder="120" v-model="editedMovie.duracion" type="text" pattern="\d*"
              required></b-form-input>
          </b-form-group>
          <b-form-group label="Descripción">
            <b-form-textarea v-model="editedMovie.descripcion"></b-form-textarea>
          </b-form-group>
          <b-form-group label="Género*">
            <b-form-select v-model="editedMovie.generoId" :options="generos" :value="editedMovie.generoId"
              required></b-form-select>
          </b-form-group>


          <footer>
            <b-button class="m-2" type="submit" variant="primary">Guardar Cambios</b-button>
            <b-button class="m-2" @click="cancelEdit" variant="secondary">Cancelar</b-button>
          </footer>
        </b-form>
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
      filteredMovies: [],
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
      generos: [
        { value: 0, text: "Selecciona el genero" },
      ],
    };
  },
  mounted() {
    this.obtenerGeneros();
    this.obtenerPeliculas();
  },
  methods: {
    async obtenerPeliculas() {
      try {
        this.loading = true;
        const data = await movieService.obtenerPeliculas();
        this.movies = data;
        this.updateFilteredMovies();
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

    updateFilteredMovies() {
      this.filteredMovies = this.movies.filter(movie =>
        movie.nombre.toLowerCase().includes(this.filtro.toLowerCase())
      );
      this.rows = this.filteredMovies.length;
    },
    onFiltered(filteredItems) {
      this.currentPage = 1;
      this.rows = filteredItems.length;
    },
    async deleteMovie(idMovie) {
      try {
        this.loading = true;
        const data = await movieService.eliminarPelicula(idMovie);
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

      // Asigna directamente el objeto del género seleccionado
      this.editedMovieGenero = this.generos.find((genero) => parseInt(genero.id) === parseInt(this.editedMovie.generoId));

      this.isEditing = true;
      this.showModal = true;
    },
    async updateMovie() {
      this.loading = true;

      try {
        if (this.isEditing) {
          await movieService.editarPelicula(this.editedMovie);
        } else {
          await movieService.guardarPelicula(this.editedMovie);
        }
        this.loading = true;
        this.obtenerPeliculas();
      } catch (error) {
        console.error(error);
      } finally {
        this.showModal = false;
        this.isEditing = false;
        setTimeout(() => {
          this.loading = false;
        }, 1000);
      }
    },
    cancelEdit() {
      this.showModal = false;
      this.isEditing = false;
    },
    openModal() {
      this.editedMovie = {
        nombre: "",
        director: "",
        duracion: "",
        descripcion: "",
        generoId: 0,
      };
      this.showModal = true;
      this.isEditing = false;
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
</style>
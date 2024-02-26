import axios from 'axios';

let API_URL = 'http://localhost:8081/api/movies';

const obtenerPeliculas = async () => {
   try {
      const response = await axios.get(API_URL);
      console.log(response.data);
      return response.data;
   } catch (error) {
      throw error;
   }
};

const obtenerPeliculasPorId = async (id) => {
    try {
       const response = await axios.get(`${API_URL}/${id}`);
       return response.data;
    } catch (error) {
       throw error;
    }
};

const guardarPelicula = async (movie) => {
   const params = {
      nombre: movie.nombre,
      director: movie.director,
      duracion: movie.duracion,
      descripcion: movie.descripcion,
      genero_id: movie.genero_id
   };
   try {
      const response = await axios.post(API_URL, params);
      return response.data;
   } catch (error) {
      throw error;
   }
};

const editarPelicula = async (movie) => {
   const NEW_URL = `${API_URL}/${movie.id}`;
   const params = {
      nombre: movie.nombre,
      director: movie.director,
      duracion: movie.duracion,
      descripcion: movie.descripcion,
      genero_id: movie.genero_id
   };
   try {
      const response = await axios.put(NEW_URL, params);
      return response.data;
   } catch (error) {
      throw error;
   }
};

const eliminarPelicula = async (movie_id) => {
   const NEW_URL = `${API_URL}/${movie_id}`;
   try {
      const response = await axios.delete(NEW_URL);
      return response.data;
   } catch (error) {
      throw error;
   }
};

const obtenerPeliculasPorRangoFecha = async(start, end) =>{
   const NEW_URL = `${API_URL}/searchForFechas`;
   const params = {
      fechaInicio: start,
      fechaFin: end
   }
   try {
      const response = await axios.get(NEW_URL, { params });
      return response;
   } catch (error) {
      throw error;
   }
}

const obtenerPeliculasOdernDesc = async(start) =>{
   const NEW_URL = `${API_URL}/searchForFechaPublicacionDesc`;
   const params = {
      fechaPublicacion: start
   }
   try {
      const response = await axios.get(NEW_URL, { params });
      return response;
   } catch (error) {
      throw error;
   }
}

export default {
   obtenerPeliculas,
   editarPelicula,
   guardarPelicula,
   eliminarPelicula,
   obtenerPeliculasOdernDesc,
   obtenerPeliculasPorRangoFecha,
};

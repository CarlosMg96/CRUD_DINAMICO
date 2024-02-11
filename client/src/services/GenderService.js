import axios from 'axios';

let API_URL = 'http://localhost:8080/api/genders';

const obtenerGeneros = async () => {
   try {
      const response = await axios.get(API_URL);
      console.log(response.data);
      return response.data;
   } catch (error) {
      throw error;
   }
};

export default {
    obtenerGeneros,
 };
package com.example.server.peliculas.controller;

import com.example.server.peliculas.model.MovieModel;
import com.example.server.peliculas.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.sql.Date;
import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping
    public List<MovieModel> getAllMovies(){
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public MovieModel getMovieById(@PathVariable int id){
        return movieService.getMovieById(id).orElseThrow(() -> new RuntimeException("Pelicula no encontrada"));
    }

    @GetMapping("/searchForName/{nombre}")
    public List<MovieModel> buscarPorNombre(@PathVariable String nombre) {
        return movieService.searchForNombre(nombre);
    }

    @GetMapping("/searchForDirector/{director}")
    public List<MovieModel> buscarPorDirector(@PathVariable String director) {
        return movieService.searchForDirector(director);
    }

    @GetMapping("/searchForFechas")
    public List<MovieModel> buscarPorFechas(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String fechaInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String fechaFin) {
        System.out.println("Fecha de inicio: " + fechaInicio);
        System.out.println("Fecha de fin: " + fechaFin);
        Date fechaInicioConvertida = Date.valueOf(fechaInicio);
        Date fechaFinConvertida = Date.valueOf(fechaFin);
        return movieService.buscarPorFechas(fechaInicioConvertida, fechaFinConvertida);
    }

    @GetMapping("/searchForGenderId/{generoId}")
    public List<MovieModel> buscarPorGeneroId(@PathVariable Integer generoId) {
        return movieService.buscarPorGeneroId(generoId);
    }

    @GetMapping("/searchForFechaPublicacionDesc")
    public List<MovieModel> buscarPorFechaPublicacionDesc(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaPublicacion) {
        return movieService.buscarPorFechaPublicacionDesc(fechaPublicacion);
    }



    @PostMapping
    public MovieModel createUser(@RequestBody MovieModel movie){
        return movieService.createMovie(movie);
    }

    @PutMapping("/{id}")
    public MovieModel updateMovie(@PathVariable int id, @RequestBody MovieModel movieDetails){
        return  movieService.updateMovie(id, movieDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable int id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("Película eliminada con éxito");
    }

}

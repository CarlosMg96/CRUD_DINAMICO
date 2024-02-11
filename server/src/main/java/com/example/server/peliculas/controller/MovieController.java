package com.example.server.peliculas.controller;

import com.example.server.peliculas.model.MovieModel;
import com.example.server.peliculas.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

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

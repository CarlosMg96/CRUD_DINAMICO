package com.example.server.peliculas.service;

import com.example.server.peliculas.model.MovieModel;
import com.example.server.peliculas.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {


    @Autowired
    private MovieRepository movieRepository;

    public List<MovieModel> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<MovieModel> getMovieById(int id) {
        return movieRepository.findById(id);
    }

    public MovieModel createMovie(MovieModel movie) {
        return movieRepository.save(movie);
    }

    public MovieModel updateMovie(int id, MovieModel movieDetails) {
        MovieModel movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        movie.setNombre(movieDetails.getNombre());
        movie.setDirector(movieDetails.getDirector());
        movie.setDuracion(movieDetails.getDuracion());
        movie.setDescripcion(movieDetails.getDescripcion());
        movie.setGeneroId(movieDetails.getGeneroId());
        return movieRepository.save(movie);
    }

    public void deleteMovie(int id) {
        movieRepository.deleteById(id);
    }
}

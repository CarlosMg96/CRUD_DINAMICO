package com.example.server.peliculas.repository;

import com.example.server.peliculas.model.MovieModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository  extends JpaRepository<MovieModel, Integer> {
}

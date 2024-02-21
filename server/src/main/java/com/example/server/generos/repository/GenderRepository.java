package com.example.server.generos.repository;

import com.example.server.generos.model.GenderModel;
import com.example.server.peliculas.model.MovieModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenderRepository extends JpaRepository<GenderModel, Integer> {
}

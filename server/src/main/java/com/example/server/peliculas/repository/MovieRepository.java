package com.example.server.peliculas.repository;

import com.example.server.generos.model.GenderModel;
import com.example.server.peliculas.model.MovieModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface MovieRepository  extends JpaRepository<MovieModel, Integer> {

    List<MovieModel> findByNombreContainingIgnoreCase(String nombre);

    List<MovieModel> findByDirectorContainingIgnoreCase(String director);

    List<MovieModel> findByFechaPublicacionBetween(Date fechaInicio, Date fechaFin);

    @Query("SELECT p FROM MovieModel p WHERE p.generoId = ?1")
    List<MovieModel> searchByGeneroId(Integer generoId);

    List<MovieModel> findByFechaPublicacionBeforeOrderByFechaPublicacionDesc(Date fechaPublicacion);
}

package com.example.server.generos.repository;

import com.example.server.generos.model.GenderModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderRepository extends JpaRepository<GenderModel, Integer> {

}

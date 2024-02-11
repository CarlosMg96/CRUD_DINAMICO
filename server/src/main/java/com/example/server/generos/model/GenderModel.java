package com.example.server.generos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "generos")
public class GenderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String genero;

    // Constructor con valores por defecto
    public GenderModel() {
        // Establecer un valor por defecto genérico
        this.genero = "Selecciona el género";
    }

    // Constructor para géneros específicos
    public GenderModel(String genero) {
        this.genero = genero;
    }

    // Otros constructores si es necesario

    // Getter y setter para 'id' y otros atributos

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}

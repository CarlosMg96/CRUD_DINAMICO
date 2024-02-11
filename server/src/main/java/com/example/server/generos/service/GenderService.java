package com.example.server.generos.service;

import com.example.server.generos.model.GenderModel;
import com.example.server.generos.repository.GenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class GenderService {

    @Autowired
    private GenderRepository genderRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        // Lógica para cargar géneros por defecto
        loadDefaultGenders();
    }

    public List<GenderModel> getAllGenders(){
        return genderRepository.findAll();
    }

    public GenderModel createGender(GenderModel gender) {
        return genderRepository.save(gender);
    }

    private void loadDefaultGenders() {
        List<String> defaultGenres = Arrays.asList("Acción", "Comedia", "Drama");

        for (String genre : defaultGenres) {
            GenderModel gender = new GenderModel(genre);
            genderRepository.save(gender);
        }
    }
}

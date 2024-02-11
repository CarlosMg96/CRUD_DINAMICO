package com.example.server.generos.controller;

import com.example.server.generos.model.GenderModel;
import com.example.server.generos.service.GenderService;
import com.example.server.peliculas.model.MovieModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/genders")
public class GenderController {
    @Autowired
    private GenderService genderService;

    @GetMapping
    public List<GenderModel> getAllGenders(){
        return genderService.getAllGenders();
    }

    @PostMapping
    public GenderModel createGender(@RequestBody GenderModel gender){
        return genderService.createGender(gender);
    }
}

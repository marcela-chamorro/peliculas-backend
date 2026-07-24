package com.unrn.peliculas.controller;

import com.unrn.peliculas.dto.GeneroDTO;
import com.unrn.peliculas.service.GeneroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/generos")
@CrossOrigin(origins = "*")
public class GeneroController {

    @Autowired
    private GeneroService generoService;

    @GetMapping
    public List<GeneroDTO> obtenerTodos() {
        return generoService.obtenerTodos();
    }
}
package com.unrn.peliculas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unrn.peliculas.dto.PeliculaDTO;
import com.unrn.peliculas.service.PeliculaService;

@RestController
@RequestMapping("/api/peliculas")
public class PeliculaController {
    @Autowired
    PeliculaService peliculaService;

    @PostMapping
    public PeliculaDTO crear(@RequestBody PeliculaDTO dto) {
        return peliculaService.crearPelicula(dto);
    }

    @PutMapping("/{id}")
    public PeliculaDTO editar(
            @PathVariable Integer id,
            @RequestBody PeliculaDTO dto) {
        return peliculaService.editarPelicula(id, dto);
    }
}

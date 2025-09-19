package com.unrn.peliculas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public PeliculaDTO obtenerDetallePelicula(@PathVariable Integer id) {
        return peliculaService.obtenerDetallePelicula(id);
    }
}

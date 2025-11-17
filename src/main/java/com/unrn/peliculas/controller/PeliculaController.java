package com.unrn.peliculas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.unrn.peliculas.dto.PeliculaDTO;
import com.unrn.peliculas.service.PeliculaService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/peliculas")
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

    @GetMapping
    public List<PeliculaDTO> listarPeliculas(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String actor,
            @RequestParam(required = false) Integer anio,
            @RequestParam(required = false) BigDecimal precioMax,
            @RequestParam(required = false) String formato) {
        return peliculaService.listarPeliculasFiltradas(titulo, genero, director, actor, anio, precioMax, formato);
    }

    @GetMapping("/genero/{genero}")
    public List<PeliculaDTO> listarPorGenero(@PathVariable String genero) {
        return peliculaService.listarPorGenero(genero);
    }

    @GetMapping("/director/{director}")
    public List<PeliculaDTO> listarPorDirector(@PathVariable String director) {
        return peliculaService.listarPorDirector(director);
    }

    @GetMapping("/actor/{actor}")
    public List<PeliculaDTO> listarPorActor(@PathVariable String actor) {
        return peliculaService.listarPorActor(actor);
    }
}

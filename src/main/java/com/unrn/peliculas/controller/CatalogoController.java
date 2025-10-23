package com.unrn.peliculas.controller;

import com.unrn.peliculas.dto.CatalogoDTO;
import com.unrn.peliculas.service.CatalogoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogos")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    // Crear un nuevo catálogo
    @PostMapping("/crear-catalogo/")
    public CatalogoDTO crearCatalogo(@RequestBody CatalogoDTO dto) {
        return catalogoService.crear(dto);
    }

    // Ver todos los catálogos
    @GetMapping
    public List<CatalogoDTO> listarCatalogos() {
        return catalogoService.listar();

    }

    // Ver un catálogo por id
    @GetMapping("/{id}")
    public CatalogoDTO obtenerCatalogo(@PathVariable Integer id) {
        return catalogoService.obtener(id);
        
    }

    // Agregar una película a un catálogo
    @PostMapping("/{catalogoId}/peliculas/{peliculaId}")
    public CatalogoDTO agregarPelicula(@PathVariable Integer catalogoId, @PathVariable Integer peliculaId) {
        return catalogoService.agregarPelicula(catalogoId, peliculaId);
        
    }
}

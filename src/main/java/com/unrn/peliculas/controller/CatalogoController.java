package com.unrn.peliculas.controller;

import com.unrn.peliculas.dto.CatalogoDTO;
import com.unrn.peliculas.service.CatalogoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogos")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    // Ver todos los catálogos
    @GetMapping
    @PreAuthorize("hasAnyRole('admin', 'cliente')")
    public List<CatalogoDTO> listarCatalogos() {
        return catalogoService.listar();
    }

    // Ver un catálogo por id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'cliente')")
    public CatalogoDTO obtenerCatalogo(@PathVariable Integer id) {
        return catalogoService.obtener(id);
    }

    // Agregar una película a un catálogo
    @PostMapping("/{catalogoId}/peliculas/{peliculaId}")
    @PreAuthorize("hasRole('admin')")
    public CatalogoDTO agregarPelicula(@PathVariable Integer catalogoId, @PathVariable Integer peliculaId) {
        return catalogoService.agregarPelicula(catalogoId, peliculaId);
    }
}

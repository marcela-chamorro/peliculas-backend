package com.unrn.peliculas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.unrn.peliculas.dto.PeliculaDTO;
import com.unrn.peliculas.domain.Pelicula;
import com.unrn.peliculas.repository.PeliculaRepository;
import org.springframework.web.server.ResponseStatusException;

@Service
@Validated
public class PeliculaService {

    @Autowired
    private PeliculaRepository peliculaRepo;

    public PeliculaDTO crearPelicula(PeliculaDTO dto) {

        Pelicula p = new Pelicula();
        p.setTitulo(dto.getTitulo());
        p.setFechaSalida(dto.getFechaSalida());
        p.setPrecio(dto.getPrecio());
        p.setCondicion(dto.getCondicion());
        p.setFormato(dto.getFormato());
        p.setSinopsis(dto.getSinopsis());
        p.setImagenAmpliada(dto.getImagenAmpliada());
        peliculaRepo.save(p);
        return dto;

    }

    public PeliculaDTO editarPelicula(Integer id, PeliculaDTO dto) {
        Pelicula p = peliculaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));
        p.setTitulo(dto.getTitulo());
        p.setFechaSalida(dto.getFechaSalida());
        p.setPrecio(dto.getPrecio());
        p.setCondicion(dto.getCondicion());
        p.setFormato(dto.getFormato());
        p.setSinopsis(dto.getSinopsis());
        p.setImagenAmpliada(dto.getImagenAmpliada());
        peliculaRepo.save(p);
        return dto;

    }

    public PeliculaDTO obtenerDetallePelicula(Integer id) {
        Pelicula p = peliculaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Película no encontrada"));
        return new PeliculaDTO(p.getPeliculaId(), p.getTitulo(), p.getFechaSalida(), p.getPrecio(),
                p.getCondicion(), p.getFormato(), p.getSinopsis(), p.getImagenAmpliada());
    }
}

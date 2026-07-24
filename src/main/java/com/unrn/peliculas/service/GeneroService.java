package com.unrn.peliculas.service;

import com.unrn.peliculas.dto.GeneroDTO;
import com.unrn.peliculas.repository.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneroService {

    @Autowired
    private GeneroRepository generoRepository;

    public List<GeneroDTO> obtenerTodos() {

        return generoRepository.findAll()
                .stream()
                .map(genero -> GeneroDTO.builder()
                        .generoId(genero.getGeneroId())
                        .nombre(genero.getNombre())
                        .build())
                .toList();
    }
}


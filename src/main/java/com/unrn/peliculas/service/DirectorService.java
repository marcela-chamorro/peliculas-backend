package com.unrn.peliculas.service;

import com.unrn.peliculas.dto.DirectorDTO;
import com.unrn.peliculas.repository.DirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectorService {

    @Autowired
    private DirectorRepository directorRepository;

    public List<DirectorDTO> obtenerTodos() {

        return directorRepository.findAll()
                .stream()
                .map(director -> DirectorDTO.builder()
                        .directorId(director.getDirectorId())
                        .nombre(director.getNombre())
                        .build())
                .toList();
    }
}
package com.unrn.peliculas.service;

import com.unrn.peliculas.dto.ActorDTO;
import com.unrn.peliculas.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    public List<ActorDTO> obtenerTodos() {
        return actorRepository.findAll()
                .stream()
                .map(actor -> ActorDTO.builder()
                        .actorId(actor.getActorId())
                        .nombre(actor.getNombre())
                        .build())
                .toList();
    }
}

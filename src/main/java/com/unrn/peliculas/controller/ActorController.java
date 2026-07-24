package com.unrn.peliculas.controller;

import com.unrn.peliculas.dto.ActorDTO;
import com.unrn.peliculas.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actores")
@CrossOrigin(origins = "*")
public class ActorController {

    @Autowired
    private ActorService actorService;

    @GetMapping
    public List<ActorDTO> obtenerTodos() {
        return actorService.obtenerTodos();
    }
}
package com.unrn.peliculas.controller;

import com.unrn.peliculas.dto.DirectorDTO;
import com.unrn.peliculas.service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/directores")
@CrossOrigin(origins = "*")
public class DirectorController {

    @Autowired
    private DirectorService directorService;

    @GetMapping
    public List<DirectorDTO> obtenerTodos() {
        return directorService.obtenerTodos();
    }
}
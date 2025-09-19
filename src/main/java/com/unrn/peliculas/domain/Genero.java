package com.unrn.peliculas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "generos")
public class Genero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short generoId;

    @Column(nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(nullable = false)
    private LocalDateTime lastUpdate = LocalDateTime.now();

    @ManyToMany(mappedBy = "generos")
    @JsonIgnore
    private Set<Pelicula> peliculas = new HashSet<>();
}

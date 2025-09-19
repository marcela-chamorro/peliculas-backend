package com.unrn.peliculas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "actores")
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short actorId;

    @Column(nullable = false, length = 255)
    private String nombre;

    @Column(nullable = false)
    private LocalDateTime lastUpdate = LocalDateTime.now();

    @ManyToMany(mappedBy = "actores")
    @JsonIgnore
    private Set<Pelicula> peliculas = new HashSet<>();
}

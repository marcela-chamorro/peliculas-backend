package com.unrn.peliculas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "directores")
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "director_id")
    private Short directorId;

    @Column(nullable = false, length = 255)
    private String nombre;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate = LocalDateTime.now();

    @ManyToMany(mappedBy = "directores")
    @JsonIgnore
    private Set<Pelicula> peliculas = new HashSet<>();
}

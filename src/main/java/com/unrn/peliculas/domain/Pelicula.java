package com.unrn.peliculas.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "peliculas")
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer peliculaId;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(nullable = false)
    private LocalDate fechaSalida;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false, length = 50)
    private String condicion;

    @Column(nullable = false, length = 50)
    private String formato;

    @Column(columnDefinition = "TEXT")
    private String sinopsis;

    @Column(length = 255)
    private String imagenAmpliada;

    @Column(nullable = false)
    private LocalDateTime lastUpdate = LocalDateTime.now();

    @ManyToMany
    @JoinTable(
            name = "pelicula_directores",
            joinColumns = @JoinColumn(name = "pelicula_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id")
    )
    private Set<Director> directores = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "pelicula_actores",
            joinColumns = @JoinColumn(name = "pelicula_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actores = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "pelicula_generos",
            joinColumns = @JoinColumn(name = "pelicula_id"),
            inverseJoinColumns = @JoinColumn(name = "genero_id")
    )
    private Set<Genero> generos = new HashSet<>();
}

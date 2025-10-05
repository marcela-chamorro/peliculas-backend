package com.unrn.peliculas.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "catalogos")
public class Catalogo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long catalogoId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @ManyToMany
    @JoinTable(name = "catalogo_peliculas", joinColumns = @JoinColumn(name = "catalogo_id"), inverseJoinColumns = @JoinColumn(name = "pelicula_id"))
    private List<Pelicula> peliculas = new ArrayList<>();
}

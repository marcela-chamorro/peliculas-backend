package com.unrn.peliculas.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PeliculaDTO {
    private Integer peliculaId;
    private String titulo;
    private LocalDate fechaSalida;
    private BigDecimal precio;
    private String condicion;
    private String formato;
    private String sinopsis;
    private String imagenAmpliada;

    // Para crear/editar: se envían IDs
    private List<Integer> actoresIds;
    private List<Integer> directoresIds;
    private List<Integer> generosIds;

    // Para detalle: se devuelven nombres
    private List<String> actores;
    private List<String> directores;
    private List<String> generos;
}


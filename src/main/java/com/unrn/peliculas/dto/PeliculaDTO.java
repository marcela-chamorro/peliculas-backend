package com.unrn.peliculas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PeliculaDTO {

    private Integer peliculaId;
    private String titulo;
    private LocalDate fechaSalida;
    private BigDecimal precio;
    private Integer stock;
    private String condicion;
    private String formato;
    private String sinopsis;
    private String imagenAmpliada;

    // Datos que envía el frontend
    private String director;
    private String actores;

    // Lista de géneros
    private List<Integer> generosIds;

    // Para mostrar en detalle/listado
    private List<String> directores;
    private List<String> generos;

    // Para el combo de géneros
    private List<GeneroDTO> generosDetalle;
}
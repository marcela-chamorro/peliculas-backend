package com.unrn.peliculas.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private List<Long> actoresIds;
    private List<Long> directoresIds;
    private List<Long> generosIds;

}

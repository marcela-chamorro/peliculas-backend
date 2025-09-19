package com.unrn.peliculas.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PeliculaDTO {

    public PeliculaDTO(Integer peliculaId, String titulo2, LocalDate fechaSalida2, BigDecimal precio2,
            String condicion2, String formato2, String sinopsis2, String imagenAmpliada2) {
    }

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

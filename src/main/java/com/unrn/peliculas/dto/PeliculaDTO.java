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

    public PeliculaDTO(Integer peliculaId, String titulo, LocalDate fechaSalida, BigDecimal precio,
                       String condicion, String formato, String sinopsis, String imagenAmpliada) {
        this.titulo = titulo;
        this.fechaSalida = fechaSalida;
        this.precio = precio;
        this.condicion = condicion;
        this.formato = formato;
        this.sinopsis = sinopsis;
        this.imagenAmpliada = imagenAmpliada;
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

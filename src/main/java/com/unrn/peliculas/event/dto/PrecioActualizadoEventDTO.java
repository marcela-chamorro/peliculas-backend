package com.unrn.peliculas.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrecioActualizadoEventDTO {

    private Integer peliculaId;
    private BigDecimal nuevoPrecio;
}
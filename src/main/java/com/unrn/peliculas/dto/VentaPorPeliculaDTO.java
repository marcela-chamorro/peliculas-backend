package com.unrn.peliculas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaPorPeliculaDTO {

    private Integer peliculaId;
    private Integer cantidadVendida;

}

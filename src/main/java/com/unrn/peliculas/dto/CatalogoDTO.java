package com.unrn.peliculas.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
public class CatalogoDTO {
    private Long catalogoId;
    private String nombre;
    private String descripcion;
    private List<PeliculaDTO> peliculas; // devuelve las películas del catálogo
}

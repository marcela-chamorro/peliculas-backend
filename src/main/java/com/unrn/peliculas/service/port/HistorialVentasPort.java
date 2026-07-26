package com.unrn.peliculas.service.port;

import com.unrn.peliculas.dto.VentaPorPeliculaDTO;
import java.util.List;

public interface HistorialVentasPort {
    List<VentaPorPeliculaDTO> obtenerVentasPorPelicula();
}

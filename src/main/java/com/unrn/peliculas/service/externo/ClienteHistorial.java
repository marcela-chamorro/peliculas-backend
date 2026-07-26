package com.unrn.peliculas.service.externo;

import com.unrn.peliculas.dto.VentaPorPeliculaDTO;
import com.unrn.peliculas.service.port.HistorialVentasPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ClienteHistorial implements HistorialVentasPort {

    private final RestTemplate rest;
    private final String baseUrl;

    public ClienteHistorial(
            RestTemplate rest,
            @Value("${historial.base-url}") String baseUrl) {

        this.rest = rest;
        this.baseUrl = baseUrl;
    }

    public List<VentaPorPeliculaDTO> obtenerVentasPorPelicula() {

        ResponseEntity<List<VentaPorPeliculaDTO>> response =
                rest.exchange(
                        baseUrl + "/historial/ventas-por-pelicula",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<VentaPorPeliculaDTO>>() {}
                );

        if (!response.getStatusCode().is2xxSuccessful()
                || response.getBody() == null) {

            throw new IllegalStateException(
                    "No se pudo obtener las ventas desde Historial");
        }

        return response.getBody();
    }
}

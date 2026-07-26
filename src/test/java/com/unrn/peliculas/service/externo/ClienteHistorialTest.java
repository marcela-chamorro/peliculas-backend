package com.unrn.peliculas.service.externo;

import com.unrn.peliculas.dto.VentaPorPeliculaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

class ClienteHistorialTest {

    @Mock
    private RestTemplate restTemplate;

    private ClienteHistorial clienteHistorial;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clienteHistorial = new ClienteHistorial(restTemplate, "http://localhost:8081");
    }

    @Test
    void obtenerVentasPorPelicula_Success() {
        // Arrange
        VentaPorPeliculaDTO dto = new VentaPorPeliculaDTO();
        dto.setPeliculaId(1);
        dto.setCantidadVendida(5);

        List<VentaPorPeliculaDTO> expectedList = Collections.singletonList(dto);
        ResponseEntity<List<VentaPorPeliculaDTO>> responseEntity = new ResponseEntity<>(expectedList, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8081/historial/ventas-por-pelicula"),
                eq(HttpMethod.GET),
                isNull(),
                ArgumentMatchers.<ParameterizedTypeReference<List<VentaPorPeliculaDTO>>>any()
        )).thenReturn(responseEntity);

        // Act
        List<VentaPorPeliculaDTO> result = clienteHistorial.obtenerVentasPorPelicula();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getCantidadVendida());
    }

    @Test
    void obtenerVentasPorPelicula_Failure_ThrowsException() {
        // Arrange
        ResponseEntity<List<VentaPorPeliculaDTO>> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.exchange(
                eq("http://localhost:8081/historial/ventas-por-pelicula"),
                eq(HttpMethod.GET),
                isNull(),
                ArgumentMatchers.<ParameterizedTypeReference<List<VentaPorPeliculaDTO>>>any()
        )).thenReturn(responseEntity);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            clienteHistorial.obtenerVentasPorPelicula();
        });

        assertEquals("No se pudo obtener las ventas desde Historial", exception.getMessage());
    }
}

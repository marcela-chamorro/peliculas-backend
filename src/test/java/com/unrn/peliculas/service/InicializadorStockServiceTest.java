package com.unrn.peliculas.service;

import com.unrn.peliculas.domain.Pelicula;
import com.unrn.peliculas.dto.VentaPorPeliculaDTO;
import com.unrn.peliculas.repository.PeliculaRepository;
import com.unrn.peliculas.service.port.HistorialVentasPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InicializadorStockServiceTest {

    @Mock
    private PeliculaRepository peliculaRepository;

    @Mock
    private HistorialVentasPort historialVentasPort;

    @InjectMocks
    private InicializadorStockService inicializadorStockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Inject the @Value property using ReflectionTestUtils
        ReflectionTestUtils.setField(inicializadorStockService, "stockBaseInicial", 50);
    }

    @Test
    void inicializarStock_Success() {
        // Arrange
        VentaPorPeliculaDTO ventaDTO = new VentaPorPeliculaDTO();
        ventaDTO.setPeliculaId(1);
        ventaDTO.setCantidadVendida(10);
        List<VentaPorPeliculaDTO> ventas = Collections.singletonList(ventaDTO);

        when(historialVentasPort.obtenerVentasPorPelicula()).thenReturn(ventas);

        Pelicula p1 = new Pelicula();
        p1.setPeliculaId(1);
        p1.setStock(null); // explicit null so it enters the initialization block

        Pelicula p2 = new Pelicula();
        p2.setPeliculaId(2);
        p2.setStock(10); // already has stock, shouldn't be overridden

        when(peliculaRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        // Act
        inicializadorStockService.inicializarStock();

        // Assert
        verify(peliculaRepository, times(1)).save(p1);
        verify(peliculaRepository, never()).save(p2); // Because stock was not null
        
        // p1 stock should be 50 - 10 = 40
        assert p1.getStock() == 40;
    }

    @Test
    void inicializarStock_HistorialThrowsException() {
        // Arrange
        when(historialVentasPort.obtenerVentasPorPelicula()).thenThrow(new RuntimeException("Service down"));

        // Act
        inicializadorStockService.inicializarStock();

        // Assert
        verify(peliculaRepository, never()).findAll();
        verify(peliculaRepository, never()).save(any());
    }
}

package com.unrn.peliculas.service;

import com.unrn.peliculas.domain.Pelicula;
import com.unrn.peliculas.dto.PeliculaDTO;
import com.unrn.peliculas.repository.ActorRepository;
import com.unrn.peliculas.repository.DirectorRepository;
import com.unrn.peliculas.repository.GeneroRepository;
import com.unrn.peliculas.repository.PeliculaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeliculaServiceTest {

    @Mock
    private PeliculaRepository peliculaRepo;

    @Mock
    private ActorRepository actorRepo;

    @Mock
    private DirectorRepository directorRepo;

    @Mock
    private GeneroRepository generoRepo;

    @Mock
    private PeliculaEventPublisher eventPublisher;

    @InjectMocks
    private PeliculaService peliculaService;

    private Pelicula peliculaTest;

    @BeforeEach
    void setUp() {
        peliculaTest = new Pelicula();
        peliculaTest.setPeliculaId(1);
        peliculaTest.setTitulo("Inception");
        peliculaTest.setPrecio(new BigDecimal("1500.00"));
        peliculaTest.setFechaSalida(LocalDate.of(2010, 7, 16));
        peliculaTest.setCondicion("Nuevo");
        peliculaTest.setFormato("Blu-Ray");
        peliculaTest.setStock(10);
        peliculaTest.setActores(new HashSet<>());
        peliculaTest.setDirectores(new HashSet<>());
        peliculaTest.setGeneros(new HashSet<>());
        peliculaTest.setLastUpdate(LocalDateTime.now());
    }

    @Test
    void testConsultarStock_Exitoso() {
        when(peliculaRepo.findById(1)).thenReturn(Optional.of(peliculaTest));

        Integer stock = peliculaService.consultarStock(1);

        assertEquals(10, stock);
        verify(peliculaRepo, times(1)).findById(1);
    }

    @Test
    void testDescontarStock_Exitoso() {
        when(peliculaRepo.descontarStockConcurrente(1, 2)).thenReturn(1);
        when(peliculaRepo.findByIdWithRelations(1)).thenReturn(Optional.of(peliculaTest));

        PeliculaDTO resultado = peliculaService.descontarStock(1, 2);

        assertNotNull(resultado);
        assertEquals("Inception", resultado.getTitulo());
        verify(peliculaRepo, times(1)).descontarStockConcurrente(1, 2);
        verify(eventPublisher, times(1)).enviarEvento(any());
    }

    @Test
    void testDescontarStock_SinStockSuficiente_LanzaExcepcion() {
        when(peliculaRepo.descontarStockConcurrente(1, 20)).thenReturn(0);
        when(peliculaRepo.findById(1)).thenReturn(Optional.of(peliculaTest));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            peliculaService.descontarStock(1, 20);
        });

        assertTrue(ex.getReason().contains("Stock insuficiente"));
        verify(eventPublisher, never()).enviarEvento(any());
    }

    @Test
    void testDescontarStock_CantidadInvalida_LanzaExcepcion() {
        assertThrows(ResponseStatusException.class, () -> {
            peliculaService.descontarStock(1, 0);
        });
    }

    @Test
    void testReponerStock_Exitoso() {
        when(peliculaRepo.findByIdWithLock(1)).thenReturn(Optional.of(peliculaTest));
        when(peliculaRepo.save(any(Pelicula.class))).thenReturn(peliculaTest);

        PeliculaDTO resultado = peliculaService.reponerStock(1, 5);

        assertNotNull(resultado);
        assertEquals(15, peliculaTest.getStock());
        verify(peliculaRepo, times(1)).save(peliculaTest);
        verify(eventPublisher, times(1)).enviarEvento(any());
    }
}

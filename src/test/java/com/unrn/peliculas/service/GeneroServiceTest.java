package com.unrn.peliculas.service;

import com.unrn.peliculas.domain.Genero;
import com.unrn.peliculas.dto.GeneroDTO;
import com.unrn.peliculas.repository.GeneroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class GeneroServiceTest {

    @Mock
    private GeneroRepository generoRepository;

    @InjectMocks
    private GeneroService generoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerTodos() {
        Genero genero = new Genero();
        genero.setGeneroId((short) 1);
        genero.setNombre("Action");

        when(generoRepository.findAll()).thenReturn(Collections.singletonList(genero));

        List<GeneroDTO> result = generoService.obtenerTodos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals((short) 1, result.get(0).getGeneroId());
        assertEquals("Action", result.get(0).getNombre());
    }
}

package com.unrn.peliculas.controller;

import com.unrn.peliculas.dto.GeneroDTO;
import com.unrn.peliculas.service.GeneroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class GeneroControllerTest {

    @Mock
    private GeneroService generoService;

    @InjectMocks
    private GeneroController generoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodos() {
        GeneroDTO dto = new GeneroDTO();
        dto.setGeneroId((short) 1);
        when(generoService.obtenerTodos()).thenReturn(Collections.singletonList(dto));

        List<GeneroDTO> result = generoController.obtenerTodos();

        assertEquals(1, result.size());
    }
}

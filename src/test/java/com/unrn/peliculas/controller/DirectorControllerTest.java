package com.unrn.peliculas.controller;

import com.unrn.peliculas.dto.DirectorDTO;
import com.unrn.peliculas.service.DirectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class DirectorControllerTest {

    @Mock
    private DirectorService directorService;

    @InjectMocks
    private DirectorController directorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodos() {
        DirectorDTO dto = new DirectorDTO();
        dto.setDirectorId((short) 1);
        when(directorService.obtenerTodos()).thenReturn(Collections.singletonList(dto));

        List<DirectorDTO> result = directorController.obtenerTodos();

        assertEquals(1, result.size());
    }
}

package com.unrn.peliculas.service;

import com.unrn.peliculas.domain.Director;
import com.unrn.peliculas.dto.DirectorDTO;
import com.unrn.peliculas.repository.DirectorRepository;
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

class DirectorServiceTest {

    @Mock
    private DirectorRepository directorRepository;

    @InjectMocks
    private DirectorService directorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerTodos() {
        Director director = new Director();
        director.setDirectorId((short) 1);
        director.setNombre("Nolan");

        when(directorRepository.findAll()).thenReturn(Collections.singletonList(director));

        List<DirectorDTO> result = directorService.obtenerTodos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals((short) 1, result.get(0).getDirectorId());
        assertEquals("Nolan", result.get(0).getNombre());
    }
}

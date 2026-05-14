package com.unrn.peliculas.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GeneroTest {

    @Test
    void crearGenero_seteadoCorrecto() {
        // Setup
        Genero genero = new Genero();
        genero.setGeneroId((short) 1);
        genero.setNombre("Acción");
        
        LocalDateTime ahora = LocalDateTime.now();
        genero.setLastUpdate(ahora);

        // Verificación
        assertEquals((short) 1, genero.getGeneroId(), "El ID del género debe coincidir con el valor asignado");
        assertEquals("Acción", genero.getNombre(), "El nombre del género debe coincidir con el valor asignado");
        assertEquals(ahora, genero.getLastUpdate(), "El lastUpdate del género debe coincidir con el valor asignado");
        assertNotNull(genero.getPeliculas(), "La colección de películas no debe ser nula por defecto");
        assertTrue(genero.getPeliculas().isEmpty(), "La colección de películas debe estar vacía al inicializarse");
    }
}

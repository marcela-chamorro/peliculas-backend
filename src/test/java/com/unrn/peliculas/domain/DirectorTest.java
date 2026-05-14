package com.unrn.peliculas.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DirectorTest {

    @Test
    void crearDirector_seteadoCorrecto() {
        // Setup
        Director director = new Director();
        director.setDirectorId((short) 1);
        director.setNombre("Christopher Nolan");
        
        LocalDateTime ahora = LocalDateTime.now();
        director.setLastUpdate(ahora);

        // Verificación
        assertEquals((short) 1, director.getDirectorId(), "El ID del director debe coincidir");
        assertEquals("Christopher Nolan", director.getNombre(), "El nombre del director debe coincidir");
        assertEquals(ahora, director.getLastUpdate(), "El lastUpdate debe coincidir");
        assertNotNull(director.getPeliculas(), "La colección de películas no debe ser nula por defecto");
        assertTrue(director.getPeliculas().isEmpty(), "La colección de películas debe estar vacía al inicializarse");
    }
}

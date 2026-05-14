package com.unrn.peliculas.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ActorTest {

    @Test
    void crearActor_seteadoCorrecto() {
        // Setup
        Actor actor = new Actor();
        actor.setActorId((short) 1);
        actor.setNombre("Leonardo DiCaprio");
        
        LocalDateTime ahora = LocalDateTime.now();
        actor.setLastUpdate(ahora);

        // Verificación
        assertEquals((short) 1, actor.getActorId(), "El ID del actor debe coincidir");
        assertEquals("Leonardo DiCaprio", actor.getNombre(), "El nombre del actor debe coincidir");
        assertEquals(ahora, actor.getLastUpdate(), "El lastUpdate debe coincidir");
        assertNotNull(actor.getPeliculas(), "La colección de películas no debe ser nula por defecto");
        assertTrue(actor.getPeliculas().isEmpty(), "La colección de películas debe estar vacía al inicializarse");
    }
}

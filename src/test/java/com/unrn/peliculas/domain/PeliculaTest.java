package com.unrn.peliculas.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PeliculaTest {

    @Test
    void crearPelicula_asignarValoresExitoso() {
        // Setup
        Pelicula pelicula = new Pelicula();
        pelicula.setPeliculaId(1);
        pelicula.setTitulo("Inception");
        pelicula.setPrecio(new BigDecimal("1500.50"));
        pelicula.setFechaSalida(LocalDate.of(2010, 7, 16));
        pelicula.setCondicion("Nuevo");
        pelicula.setFormato("Blu-Ray");
        pelicula.setStock(15);
        
        LocalDateTime updateDate = LocalDateTime.now();
        pelicula.setLastUpdate(updateDate);

        // Verificación
        assertEquals(1, pelicula.getPeliculaId(), "El ID de la película debe coincidir");
        assertEquals("Inception", pelicula.getTitulo(), "El título debe coincidir");
        assertEquals(new BigDecimal("1500.50"), pelicula.getPrecio(), "El precio debe coincidir");
        assertEquals(LocalDate.of(2010, 7, 16), pelicula.getFechaSalida(), "La fecha de salida debe coincidir");
        assertEquals("Nuevo", pelicula.getCondicion(), "La condición debe coincidir");
        assertEquals("Blu-Ray", pelicula.getFormato(), "El formato debe coincidir");
        assertEquals(15, pelicula.getStock(), "El stock debe coincidir");
        assertEquals(updateDate, pelicula.getLastUpdate(), "El lastUpdate debe coincidir");
    }

    @Test
    void agregarDirector_directorAñadidoCorrectamente() {
        // Setup
        Pelicula pelicula = new Pelicula();
        Director director = new Director();
        director.setDirectorId((short) 1);
        director.setNombre("Christopher Nolan");

        // Ejercitación
        pelicula.getDirectores().add(director);

        // Verificación
        assertFalse(pelicula.getDirectores().isEmpty(), "La colección de directores no debería estar vacía");
        assertTrue(pelicula.getDirectores().contains(director), "El director especificado debería estar en la colección de directores");
        assertEquals(1, pelicula.getDirectores().size(), "La cantidad de directores debería ser 1");
    }

    @Test
    void agregarActor_actorAñadidoCorrectamente() {
        // Setup
        Pelicula pelicula = new Pelicula();
        Actor actor = new Actor();
        actor.setActorId((short) 1);
        actor.setNombre("Leonardo DiCaprio");

        // Ejercitación
        pelicula.getActores().add(actor);

        // Verificación
        assertFalse(pelicula.getActores().isEmpty(), "La colección de actores no debería estar vacía");
        assertTrue(pelicula.getActores().contains(actor), "El actor debe encontrarse en la colección");
        assertEquals(1, pelicula.getActores().size(), "La cantidad de actores debería ser 1");
    }

    @Test
    void agregarGenero_generoAñadidoCorrectamente() {
        // Setup
        Pelicula pelicula = new Pelicula();
        Genero genero = new Genero();
        genero.setGeneroId((short) 1);
        genero.setNombre("Ciencia Ficción");

        // Ejercitación
        pelicula.getGeneros().add(genero);

        // Verificación
        assertFalse(pelicula.getGeneros().isEmpty(), "La colección de géneros no debería estar vacía");
        assertTrue(pelicula.getGeneros().contains(genero), "El género debe encontrarse en la colección");
        assertEquals(1, pelicula.getGeneros().size(), "La cantidad de géneros debería ser 1");
    }
}

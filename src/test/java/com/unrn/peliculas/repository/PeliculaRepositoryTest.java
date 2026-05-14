package com.unrn.peliculas.repository;

import com.unrn.peliculas.domain.Pelicula;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PeliculaRepositoryTest {

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Test
    void testGuardarYBuscarPelicula() {
        // Arrange
        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo("Inception");
        pelicula.setPrecio(new BigDecimal("1500.50"));
        pelicula.setFechaSalida(LocalDate.of(2010, 7, 16));
        pelicula.setCondicion("Nuevo");
        pelicula.setFormato("Blu-Ray");
        pelicula.setLastUpdate(LocalDateTime.now());

        // Act
        Pelicula savedPelicula = peliculaRepository.save(pelicula);
        Optional<Pelicula> foundPelicula = peliculaRepository.findByIdWithRelations(savedPelicula.getPeliculaId());

        // Assert
        assertTrue(foundPelicula.isPresent(), "La película debería existir en la base de datos");
        assertEquals("Inception", foundPelicula.get().getTitulo(), "El título debería coincidir");
    }

    @Test
    void testFindByFiltros() {
        // Arrange
        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo("The Matrix");
        pelicula.setPrecio(new BigDecimal("900.00"));
        pelicula.setFechaSalida(LocalDate.of(1999, 3, 31));
        pelicula.setCondicion("Usado");
        pelicula.setFormato("DVD");
        pelicula.setLastUpdate(LocalDateTime.now());
        peliculaRepository.save(pelicula);

        // Act
        List<Pelicula> resultados = peliculaRepository.findByFiltros(
                "Matrix", null, null, null, null, new BigDecimal("1000.00"), null);

        // Assert
        assertFalse(resultados.isEmpty(), "Debería encontrar al menos una película");
        assertEquals("The Matrix", resultados.get(0).getTitulo());
    }
}

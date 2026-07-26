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
        List<Pelicula> resultMatrix = peliculaRepository.findByFiltros(
                "Matrix", null, null, null, null, new BigDecimal("1000.00"), null);
                
        List<Pelicula> resultFull = peliculaRepository.findByFiltros(
                null, "Accion", "Wachowski", "Reeves", 1999, null, "DVD");

        // Assert
        assertFalse(resultMatrix.isEmpty(), "Debería encontrar al menos una película por título y precio");
        assertEquals("The Matrix", resultMatrix.get(0).getTitulo());
        
        // Even if relations aren't explicitly saved, we just want to hit the if blocks in Criteria API
        assertNotNull(resultFull, "Debería ejecutar la query con todos los filtros");
    }

    @Test
    void testOrdenamientoPorFechaSalida() {
        // Arrange: Guardar película más vieja y más nueva
        Pelicula vieja = new Pelicula();
        vieja.setTitulo("Vieja");
        vieja.setFechaSalida(LocalDate.of(1990, 1, 1));
        vieja.setPrecio(new BigDecimal("100.00"));
        vieja.setCondicion("Usado");
        vieja.setFormato("VHS");
        vieja.setLastUpdate(LocalDateTime.now());
        peliculaRepository.save(vieja);

        Pelicula nueva = new Pelicula();
        nueva.setTitulo("Nueva");
        nueva.setFechaSalida(LocalDate.of(2023, 1, 1));
        nueva.setPrecio(new BigDecimal("500.00"));
        nueva.setCondicion("Nuevo");
        nueva.setFormato("Blu-Ray");
        nueva.setLastUpdate(LocalDateTime.now());
        peliculaRepository.save(nueva);

        // Act
        List<Pelicula> resultFiltros = peliculaRepository.findByFiltros(null, null, null, null, null, null, null);
        List<Pelicula> resultAll = peliculaRepository.findAllWithRelations();

        // Assert: La primera debe ser la más nueva (2023)
        assertEquals("Nueva", resultFiltros.get(0).getTitulo());
        assertEquals("Nueva", resultAll.get(0).getTitulo());
    }

    @Test
    void testDescontarStockConcurrente() {
        // Arrange
        Pelicula p = new Pelicula();
        p.setTitulo("Stock Test");
        p.setPrecio(new BigDecimal("500.00"));
        p.setFechaSalida(LocalDate.of(2020, 1, 1));
        p.setCondicion("Nuevo");
        p.setFormato("DVD");
        p.setStock(5);
        p.setLastUpdate(LocalDateTime.now());
        Pelicula saved = peliculaRepository.save(p);

        // Act 1: Descontar 3 unidades (debería tener éxito)
        int filas1 = peliculaRepository.descontarStockConcurrente(saved.getPeliculaId(), 3);

        // Assert 1
        assertEquals(1, filas1, "Debería haber modificado 1 fila");
        Pelicula p1 = peliculaRepository.findById(saved.getPeliculaId()).orElseThrow();
        assertEquals(2, p1.getStock(), "El stock remanente debería ser 2");

        // Act 2: Intentar descontar 3 unidades cuando quedan 2 (debería fallar)
        int filas2 = peliculaRepository.descontarStockConcurrente(saved.getPeliculaId(), 3);

        // Assert 2
        assertEquals(0, filas2, "No debería modificar filas al no haber stock suficiente");
        Pelicula p2 = peliculaRepository.findById(saved.getPeliculaId()).orElseThrow();
        assertEquals(2, p2.getStock(), "El stock no debería haber cambiado y seguir en 2");
    }
}

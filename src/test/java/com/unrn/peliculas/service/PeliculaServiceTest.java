package com.unrn.peliculas.service;

import com.unrn.peliculas.domain.Actor;
import com.unrn.peliculas.domain.Director;
import com.unrn.peliculas.domain.Genero;
import com.unrn.peliculas.domain.Pelicula;
import com.unrn.peliculas.dto.PeliculaDTO;
import com.unrn.peliculas.event.dto.Event;
import com.unrn.peliculas.repository.ActorRepository;
import com.unrn.peliculas.repository.DirectorRepository;
import com.unrn.peliculas.repository.GeneroRepository;
import com.unrn.peliculas.repository.PeliculaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeliculaServiceTest {

    @Mock
    private PeliculaRepository peliculaRepo;

    @Mock
    private ActorRepository actorRepo;

    @Mock
    private DirectorRepository directorRepo;

    @Mock
    private GeneroRepository generoRepo;

    @Mock
    private PeliculaEventPublisher eventPublisher;

    @InjectMocks
    private PeliculaService peliculaService;

    private Pelicula pelicula;
    private PeliculaDTO peliculaDTO;
    private Actor actor;
    private Director director;
    private Genero genero;
    private Pelicula peliculaTest;

    @BeforeEach
    void setUp() {
        actor = new Actor();
        actor.setActorId((short) 1);
        actor.setNombre("Leonardo DiCaprio");

        director = new Director();
        director.setDirectorId((short) 1);
        director.setNombre("Christopher Nolan");

        genero = new Genero();
        genero.setGeneroId((short) 1);
        genero.setNombre("Ciencia Ficción");

        pelicula = new Pelicula();
        pelicula.setPeliculaId(1);
        pelicula.setTitulo("Inception");
        pelicula.setPrecio(new BigDecimal("15.50"));
        pelicula.setFechaSalida(LocalDate.of(2010, 7, 16));
        pelicula.setCondicion("Nuevo");
        pelicula.setFormato("Blu-Ray");
        pelicula.setSinopsis(
                "Un ladrón que roba secretos corporativos a través del uso de la tecnología de compartir sueños...");
        pelicula.setImagenAmpliada("inception.jpg");
        pelicula.setLastUpdate(LocalDateTime.now());
        pelicula.getActores().add(actor);
        pelicula.getDirectores().add(director);
        pelicula.getGeneros().add(genero);

        peliculaDTO = PeliculaDTO.builder()
                .peliculaId(1)
                .titulo("Inception")
                .precio(new BigDecimal("15.50"))
                .fechaSalida(LocalDate.of(2010, 7, 16))
                .condicion("Nuevo")
                .formato("Blu-Ray")
                .sinopsis(
                        "Un ladrón que roba secretos corporativos a través del uso de la tecnología de compartir sueños...")
                .imagenAmpliada("inception.jpg")
                .director("Christopher Nolan")
                .actores("Leonardo DiCaprio")
                .generosIds(List.of(1))
                .build();

        peliculaTest = new Pelicula();
        peliculaTest.setPeliculaId(1);
        peliculaTest.setTitulo("Inception");
        peliculaTest.setPrecio(new BigDecimal("1500.00"));
        peliculaTest.setFechaSalida(LocalDate.of(2010, 7, 16));
        peliculaTest.setCondicion("Nuevo");
        peliculaTest.setFormato("Blu-Ray");
        peliculaTest.setStock(10);
        peliculaTest.setActores(new HashSet<>());
        peliculaTest.setDirectores(new HashSet<>());
        peliculaTest.setGeneros(new HashSet<>());
        peliculaTest.setLastUpdate(LocalDateTime.now());
    }

    @Test
    void testCrearPelicula_Success() {
        when(actorRepo.findByNombreIgnoreCase("Leonardo DiCaprio"))
                .thenReturn(Optional.of(actor));

        when(directorRepo.findByNombreIgnoreCase("Christopher Nolan"))
                .thenReturn(Optional.of(director));

        when(generoRepo.findById(1))
                .thenReturn(Optional.of(genero));
        when(peliculaRepo.save(any(Pelicula.class))).thenAnswer(invocation -> {
            Pelicula p = invocation.getArgument(0);
            p.setPeliculaId(1);
            return p;
        });

        PeliculaDTO result = peliculaService.crearPelicula(peliculaDTO);

        assertNotNull(result);
        assertEquals(1, result.getPeliculaId());
        assertEquals("Inception", result.getTitulo());
        verify(peliculaRepo, times(1)).save(any(Pelicula.class));
        verify(eventPublisher, times(1)).enviarEvento(any(Event.class));
    }

    @Test
    void testCrearPelicula_SinRelaciones() {
        peliculaDTO.setActores(null);
        peliculaDTO.setDirector(null);
        peliculaDTO.setGenerosIds(null);

        when(peliculaRepo.save(any(Pelicula.class))).thenAnswer(invocation -> {
            Pelicula p = invocation.getArgument(0);
            p.setPeliculaId(1);
            return p;
        });

        PeliculaDTO result = peliculaService.crearPelicula(peliculaDTO);

        assertNotNull(result);
        assertTrue(result.getActores().isEmpty());
        assertTrue(result.getDirectores().isEmpty());
        assertTrue(result.getGeneros().isEmpty());
    }

    @Test
    void testEditarPelicula_Success() {
        when(peliculaRepo.findById(1)).thenReturn(Optional.of(pelicula));

        when(actorRepo.findByNombreIgnoreCase("Leonardo DiCaprio"))
                .thenReturn(Optional.of(actor));

        when(directorRepo.findByNombreIgnoreCase("Christopher Nolan"))
                .thenReturn(Optional.of(director));

        when(generoRepo.findById(1))
                .thenReturn(Optional.of(genero));

        when(peliculaRepo.save(any(Pelicula.class)))
                .thenReturn(pelicula);

        // Act
        PeliculaDTO result = peliculaService.editarPelicula(1, peliculaDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Inception", result.getTitulo());

        verify(peliculaRepo, times(1)).save(any(Pelicula.class));
        verify(eventPublisher, times(1)).enviarEvento(any(Event.class));
    }

    @Test
    void testEditarPelicula_PeliculaNoEncontrada() {
        when(peliculaRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> peliculaService.editarPelicula(99, peliculaDTO));
    }

    @Test
    void testEditarPelicula_GeneroNoEncontrado() {
        when(peliculaRepo.findById(1))
                .thenReturn(Optional.of(pelicula));

        when(actorRepo.findByNombreIgnoreCase("Leonardo DiCaprio"))
                .thenReturn(Optional.of(actor));

        when(directorRepo.findByNombreIgnoreCase("Christopher Nolan"))
                .thenReturn(Optional.of(director));

        when(generoRepo.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> peliculaService.editarPelicula(1, peliculaDTO));
    }

    @Test
    void testCrearPelicula_GeneroNoEncontrado() {
        when(actorRepo.findByNombreIgnoreCase("Leonardo DiCaprio"))
                .thenReturn(Optional.of(actor));

        when(directorRepo.findByNombreIgnoreCase("Christopher Nolan"))
                .thenReturn(Optional.of(director));

        when(generoRepo.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> peliculaService.crearPelicula(peliculaDTO));
    }

    @Test
    void testObtenerDetallePelicula_Success() {
        when(peliculaRepo.findByIdWithRelations(1)).thenReturn(Optional.of(pelicula));

        PeliculaDTO result = peliculaService.obtenerDetallePelicula(1);

        assertNotNull(result);
        assertEquals(1, result.getPeliculaId());
        assertEquals("Inception", result.getTitulo());
    }

    @Test
    void testObtenerDetallePelicula_NotFound() {
        when(peliculaRepo.findByIdWithRelations(99)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> peliculaService.obtenerDetallePelicula(99));
    }

    @Test
    void testListarTodasLasPeliculas() {
        when(peliculaRepo.findAllWithRelations()).thenReturn(List.of(pelicula));

        List<PeliculaDTO> result = peliculaService.listarTodasLasPeliculas();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitulo());
    }

    @Test
    void testListarPeliculasFiltradas() {
        when(peliculaRepo.findByFiltros(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(pelicula));

        List<PeliculaDTO> result = peliculaService.listarPeliculasFiltradas(
                "Inception", "Ciencia Ficción", "Christopher Nolan", "Leonardo DiCaprio", 2010, new BigDecimal("20.00"),
                "Blu-Ray");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitulo());
    }

    @Test
    void testListarPorGenero() {
        when(peliculaRepo.findByFiltros(null, "Acción", null, null, null, null, null))
                .thenReturn(List.of(pelicula));

        List<PeliculaDTO> result = peliculaService.listarPorGenero("Acción");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testListarPorDirector() {
        when(peliculaRepo.findByFiltros(null, null, "Christopher Nolan", null, null, null, null))
                .thenReturn(List.of(pelicula));

        List<PeliculaDTO> result = peliculaService.listarPorDirector("Christopher Nolan");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testListarPorActor() {
        when(peliculaRepo.findByFiltros(null, null, null, "Leonardo DiCaprio", null, null, null))
                .thenReturn(List.of(pelicula));

        List<PeliculaDTO> result = peliculaService.listarPorActor("Leonardo DiCaprio");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testBuscarPeliculas_QueryEmpty() {
        when(peliculaRepo.findAllWithRelations()).thenReturn(List.of(pelicula));

        List<PeliculaDTO> result = peliculaService.buscarPeliculas("");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testBuscarPeliculas_QueryNull() {
        when(peliculaRepo.findAllWithRelations()).thenReturn(List.of(pelicula));

        List<PeliculaDTO> result = peliculaService.buscarPeliculas(null);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testBuscarPeliculas_QueryValid() {
        when(peliculaRepo.findByFiltros("inception", "inception", "inception", "inception", null, null, null))
                .thenReturn(List.of(pelicula));

        List<PeliculaDTO> result = peliculaService.buscarPeliculas(" Inception ");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testListarPeliculasRecientes() {
        Pelicula vieja = new Pelicula();
        vieja.setPeliculaId(2);
        vieja.setTitulo("Pelicula Vieja");
        vieja.setFechaSalida(LocalDate.of(1990, 1, 1));
        vieja.setPrecio(new BigDecimal("5.00"));

        Pelicula nueva = new Pelicula();
        nueva.setPeliculaId(3);
        nueva.setTitulo("Pelicula Nueva");
        int añoActual = LocalDate.now().getYear();
        nueva.setFechaSalida(LocalDate.of(añoActual - 1, 1, 1));
        nueva.setPrecio(new BigDecimal("15.00"));

        when(peliculaRepo.findAllWithRelations()).thenReturn(List.of(vieja, nueva));

        List<PeliculaDTO> result = peliculaService.listarPeliculasRecientes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pelicula Nueva", result.get(0).getTitulo());
    }

    @Test
    void testListarPeliculasEnOferta() {
        Pelicula cara = new Pelicula();
        cara.setPeliculaId(2);
        cara.setTitulo("Pelicula Cara");
        cara.setFechaSalida(LocalDate.of(2020, 1, 1));
        cara.setPrecio(new BigDecimal("25.00"));

        Pelicula barata = new Pelicula();
        barata.setPeliculaId(3);
        barata.setTitulo("Pelicula Barata");
        barata.setFechaSalida(LocalDate.of(2020, 1, 1));
        barata.setPrecio(new BigDecimal("5.00"));

        when(peliculaRepo.findAllWithRelations()).thenReturn(List.of(cara, barata));

        List<PeliculaDTO> result = peliculaService.listarPeliculasEnOferta();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pelicula Barata", result.get(0).getTitulo());
    }

    @Test
    void testToDTOLista_SinopsisLengthCheck() {
        // Sinopsis > 100 caracteres
        pelicula.setSinopsis(
                "Esta es una sinopsis muy larga diseñada específicamente para probar que el método de listado trunca el texto de manera correcta cuando supera los cien caracteres de longitud.");
        when(peliculaRepo.findAllWithRelations()).thenReturn(List.of(pelicula));

        List<PeliculaDTO> result = peliculaService.listarTodasLasPeliculas();

        assertTrue(result.get(0).getSinopsis().endsWith("..."));
        assertEquals(103, result.get(0).getSinopsis().length());

        // Sinopsis <= 100 caracteres
        pelicula.setSinopsis("Sinopsis corta.");
        result = peliculaService.listarTodasLasPeliculas();
        assertEquals("Sinopsis corta.", result.get(0).getSinopsis());

        // Sinopsis es null
        pelicula.setSinopsis(null);
        result = peliculaService.listarTodasLasPeliculas();
        assertNull(result.get(0).getSinopsis());
    }

    @Test
    void testConsultarStock_Exitoso() {
        when(peliculaRepo.findById(1)).thenReturn(Optional.of(peliculaTest));

        Integer stock = peliculaService.consultarStock(1);

        assertEquals(10, stock);
        verify(peliculaRepo, times(1)).findById(1);
    }

    @Test
    void testDescontarStock_Exitoso() {
        when(peliculaRepo.descontarStockConcurrente(1, 2)).thenReturn(1);
        when(peliculaRepo.findByIdWithRelations(1)).thenReturn(Optional.of(peliculaTest));

        PeliculaDTO resultado = peliculaService.descontarStock(1, 2);

        assertNotNull(resultado);
        assertEquals("Inception", resultado.getTitulo());
        verify(peliculaRepo, times(1)).descontarStockConcurrente(1, 2);
        verify(eventPublisher, times(1)).enviarEvento(any());
    }

    @Test
    void testDescontarStock_SinStockSuficiente_LanzaExcepcion() {
        when(peliculaRepo.descontarStockConcurrente(1, 20)).thenReturn(0);
        when(peliculaRepo.findById(1)).thenReturn(Optional.of(peliculaTest));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            peliculaService.descontarStock(1, 20);
        });

        assertTrue(ex.getReason().contains("Stock insuficiente"));
        verify(eventPublisher, never()).enviarEvento(any());
    }

    @Test
    void testDescontarStock_CantidadInvalida_LanzaExcepcion() {
        assertThrows(ResponseStatusException.class, () -> {
            peliculaService.descontarStock(1, 0);
        });
    }

    @Test
    void testReponerStock_Exitoso() {
        when(peliculaRepo.findByIdWithLock(1)).thenReturn(Optional.of(peliculaTest));
        when(peliculaRepo.save(any(Pelicula.class))).thenReturn(peliculaTest);

        PeliculaDTO resultado = peliculaService.reponerStock(1, 5);

        assertNotNull(resultado);
        assertEquals(15, peliculaTest.getStock());
        verify(peliculaRepo, times(1)).save(peliculaTest);
        verify(eventPublisher, times(1)).enviarEvento(any());
    }
}

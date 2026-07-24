package com.unrn.peliculas.service;

import com.unrn.peliculas.domain.Actor;
import com.unrn.peliculas.domain.Director;
import com.unrn.peliculas.domain.Genero;
import com.unrn.peliculas.domain.Pelicula;
import com.unrn.peliculas.dto.DescuentoStockDTO;
import com.unrn.peliculas.dto.DescuentoStockRequestDTO;
import com.unrn.peliculas.dto.GeneroDTO;
import com.unrn.peliculas.dto.PeliculaDTO;
import com.unrn.peliculas.event.EventType;
import com.unrn.peliculas.event.dto.Event;
import com.unrn.peliculas.event.dto.PeliculaSimplificada;
import com.unrn.peliculas.repository.ActorRepository;
import com.unrn.peliculas.repository.DirectorRepository;
import com.unrn.peliculas.repository.GeneroRepository;
import com.unrn.peliculas.repository.PeliculaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional
public class PeliculaService {

    @Autowired
    private PeliculaRepository peliculaRepo;

    @Autowired
    private ActorRepository actorRepo;

    @Autowired
    private DirectorRepository directorRepo;

    @Autowired
    private GeneroRepository generoRepo;

    @Autowired
    private PeliculaEventPublisher eventPublisher;

    public PeliculaDTO crearPelicula(PeliculaDTO dto) {

        Pelicula pelicula = toEntity(dto);

        peliculaRepo.save(pelicula);

        Event<Integer, PeliculaSimplificada> evento =
                new Event<>(
                        EventType.CREATE,
                        pelicula.getPeliculaId(),
                        toPeliculaSimplificada(pelicula));

        eventPublisher.enviarEvento(evento);

        return toDTO(pelicula);
    }

    public PeliculaDTO editarPelicula(Integer id, PeliculaDTO dto) {

        Pelicula pelicula = peliculaRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Película no encontrada"));

        pelicula.setTitulo(dto.getTitulo());
        pelicula.setFechaSalida(dto.getFechaSalida());
        pelicula.setPrecio(dto.getPrecio());
        pelicula.setStock(dto.getStock());
        pelicula.setCondicion(dto.getCondicion());
        pelicula.setFormato(dto.getFormato());
        pelicula.setSinopsis(dto.getSinopsis());
        pelicula.setImagenAmpliada(dto.getImagenAmpliada());

        pelicula.setDirectores(
                obtenerOCrearDirector(dto.getDirector()));

        pelicula.setActores(
                obtenerOCrearActores(dto.getActores()));

        if (dto.getGenerosIds() != null) {

            pelicula.setGeneros(
                    dto.getGenerosIds()
                            .stream()
                            .map(idGenero ->
                                    generoRepo.findById(idGenero)
                                            .orElseThrow(() ->
                                                    new RuntimeException(
                                                            "Género no encontrado: "
                                                                    + idGenero)))
                            .collect(Collectors.toSet()));
        }

        pelicula.setLastUpdate(LocalDateTime.now());

        pelicula = peliculaRepo.save(pelicula);

        Event<Integer, PeliculaSimplificada> evento =
                new Event<>(
                        EventType.UPDATE,
                        pelicula.getPeliculaId(),
                        toPeliculaSimplificada(pelicula));

        eventPublisher.enviarEvento(evento);

        return toDTO(pelicula);
    }

    public void actualizarStock(Integer peliculaId, Integer nuevoStock) {

        Pelicula pelicula = peliculaRepo.findById(peliculaId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Película no encontrada"));

        if (nuevoStock < 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El stock no puede ser negativo");
        }

        pelicula.setStock(nuevoStock);

        peliculaRepo.save(pelicula);
    }

    public void descontarStock(DescuentoStockRequestDTO request) {

        List<Pelicula> peliculasActualizadas =
                new ArrayList<>();

        for (DescuentoStockDTO item : request.getPeliculas()) {

            Pelicula pelicula =
                    peliculaRepo.findById(item.getPeliculaId())
                            .orElseThrow(() ->
                                    new ResponseStatusException(
                                            HttpStatus.NOT_FOUND,
                                            "Película no encontrada: "
                                                    + item.getPeliculaId()));

            if (pelicula.getStock() < item.getCantidad()) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Stock insuficiente para la película: "
                                + pelicula.getTitulo());
            }

            pelicula.setStock(
                    pelicula.getStock() - item.getCantidad());

            peliculasActualizadas.add(pelicula);
        }

        peliculaRepo.saveAll(peliculasActualizadas);
    }

    public PeliculaDTO obtenerDetallePelicula(Integer id) {

        Pelicula pelicula =
                peliculaRepo.findByIdWithRelations(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Película no encontrada"));

        return toDTO(pelicula);
    }

    public List<PeliculaDTO> listarTodasLasPeliculas() {

        return peliculaRepo.findAllWithRelations()
                .stream()
                .map(this::toDTOLista)
                .collect(Collectors.toList());
    }

    public List<PeliculaDTO> listarPeliculasFiltradas(
            String titulo,
            String genero,
            String director,
            String actor,
            Integer anio,
            BigDecimal precioMax,
            String formato) {

        return peliculaRepo.findByFiltros(
                        titulo,
                        genero,
                        director,
                        actor,
                        anio,
                        precioMax,
                        formato)
                .stream()
                .map(this::toDTOLista)
                .collect(Collectors.toList());
    }

    public List<PeliculaDTO> listarPorGenero(String genero) {

        return peliculaRepo.findByFiltros(
                        null,
                        genero,
                        null,
                        null,
                        null,
                        null,
                        null)
                .stream()
                .map(this::toDTOLista)
                .collect(Collectors.toList());
    }

    public List<PeliculaDTO> listarPorDirector(String director) {

        return peliculaRepo.findByFiltros(
                        null,
                        null,
                        director,
                        null,
                        null,
                        null,
                        null)
                .stream()
                .map(this::toDTOLista)
                .collect(Collectors.toList());
    }

    public List<PeliculaDTO> listarPorActor(String actor) {

        return peliculaRepo.findByFiltros(
                        null,
                        null,
                        null,
                        actor,
                        null,
                        null,
                        null)
                .stream()
                .map(this::toDTOLista)
                .collect(Collectors.toList());
    }

    public List<PeliculaDTO> buscarPeliculas(String query) {

        if (query == null || query.isBlank()) {
            return listarTodasLasPeliculas();
        }

        query = query.toLowerCase().trim();

        return peliculaRepo.findByFiltros(
                        query,
                        query,
                        query,
                        query,
                        null,
                        null,
                        null)
                .stream()
                .map(this::toDTOLista)
                .collect(Collectors.toList());
    }

    public List<PeliculaDTO> listarPeliculasRecientes() {

        int anio = LocalDate.now().getYear();

        return peliculaRepo.findAllWithRelations()
                .stream()
                .filter(p ->
                        p.getFechaSalida().getYear() >= anio - 2)
                .map(this::toDTOLista)
                .collect(Collectors.toList());
    }

    public List<PeliculaDTO> listarPeliculasEnOferta() {

        return peliculaRepo.findAllWithRelations()
                .stream()
                .filter(p ->
                        p.getPrecio()
                                .compareTo(BigDecimal.TEN) < 0)
                .map(this::toDTOLista)
                .collect(Collectors.toList());
    }

    // =======================
    // Métodos de Gestión y Control de Stock (RF-15 Concurrencia)
    // =======================

    public Integer consultarStock(Integer id) {
        Pelicula p = peliculaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Película no encontrada"));
        return p.getStock();
    }

    public PeliculaDTO descontarStock(Integer id, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad a descontar debe ser mayor a 0");
        }

        // Intento de actualización atómica directa a nivel SQL para mayor eficiencia y seguridad bajo concurrencia
        int filasAfectadas = peliculaRepo.descontarStockConcurrente(id, cantidad);
        if (filasAfectadas == 0) {
            // Verificar si la película no existe o si no hay stock suficiente
            Pelicula p = peliculaRepo.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Película no encontrada"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Stock insuficiente para la película '" + p.getTitulo() + "'. Stock disponible: " + p.getStock());
        }

        Pelicula p = peliculaRepo.findByIdWithRelations(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Película no encontrada"));

        // Publicar evento UPDATE con el stock actualizado
        Event<Integer, PeliculaSimplificada> evento = new Event<>(
            EventType.UPDATE,
            p.getPeliculaId(),
            toPeliculaSimplificada(p)
        );
        eventPublisher.enviarEvento(evento);

        return toDTO(p);
    }

    public PeliculaDTO reponerStock(Integer id, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad a reponer debe ser mayor a 0");
        }

        Pelicula p = peliculaRepo.findByIdWithLock(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Película no encontrada"));

        p.setStock((p.getStock() != null ? p.getStock() : 0) + cantidad);
        p.setLastUpdate(LocalDateTime.now());
        peliculaRepo.save(p);

        // Publicar evento UPDATE con el stock actualizado
        Event<Integer, PeliculaSimplificada> evento = new Event<>(
            EventType.UPDATE,
            p.getPeliculaId(),
            toPeliculaSimplificada(p)
        );
        eventPublisher.enviarEvento(evento);

        return toDTO(p);
    }

    // =======================
    // Métodos privados de conversión
    // =======================

    private PeliculaDTO toDTO(Pelicula pelicula) {

        return PeliculaDTO.builder()
                .peliculaId(pelicula.getPeliculaId())
                .titulo(pelicula.getTitulo())
                .fechaSalida(pelicula.getFechaSalida())
                .precio(pelicula.getPrecio())
                .stock(pelicula.getStock())
                .condicion(pelicula.getCondicion())
                .formato(pelicula.getFormato())
                .sinopsis(pelicula.getSinopsis())
                .imagenAmpliada(pelicula.getImagenAmpliada())

                // Director (uno solo)
                .director(
                        pelicula.getDirectores()
                                .stream()
                                .findFirst()
                                .map(Director::getNombre)
                                .orElse("")
                )

                // Actores separados por coma
                .actores(
                        pelicula.getActores()
                                .stream()
                                .map(Actor::getNombre)
                                .collect(Collectors.joining(", "))
                )

                .directores(
                        pelicula.getDirectores()
                                .stream()
                                .map(Director::getNombre)
                                .collect(Collectors.toList())
                )

                .generos(
                        pelicula.getGeneros()
                                .stream()
                                .map(Genero::getNombre)
                                .collect(Collectors.toList())
                )

                .generosDetalle(
                        pelicula.getGeneros()
                                .stream()
                                .map(genero ->
                                        GeneroDTO.builder()
                                                .generoId(genero.getGeneroId())
                                                .nombre(genero.getNombre())
                                                .build())
                                .collect(Collectors.toList())
                )
                .build();
    }

    private PeliculaDTO toDTOLista(Pelicula pelicula) {

        return PeliculaDTO.builder()

                .peliculaId(pelicula.getPeliculaId())
                .titulo(pelicula.getTitulo())
                .fechaSalida(pelicula.getFechaSalida())
                .precio(pelicula.getPrecio())
                .stock(pelicula.getStock())
                .condicion(pelicula.getCondicion())
                .formato(pelicula.getFormato())

                .sinopsis(
                        pelicula.getSinopsis() != null &&
                        pelicula.getSinopsis().length() > 100
                                ? pelicula.getSinopsis().substring(0, 100) + "..."
                                : pelicula.getSinopsis())

                .imagenAmpliada(pelicula.getImagenAmpliada())

                .director(
                        pelicula.getDirectores()
                                .stream()
                                .findFirst()
                                .map(Director::getNombre)
                                .orElse("")
                )

                .actores(
                        pelicula.getActores()
                                .stream()
                                .map(Actor::getNombre)
                                .collect(Collectors.joining(", "))
                )

                .directores(
                        pelicula.getDirectores()
                                .stream()
                                .map(Director::getNombre)
                                .collect(Collectors.toList())
                )

                .generos(
                        pelicula.getGeneros()
                                .stream()
                                .map(Genero::getNombre)
                                .collect(Collectors.toList())
                )

                .build();
    }

    private Pelicula toEntity(PeliculaDTO dto) {

        Pelicula pelicula = new Pelicula();

        pelicula.setTitulo(dto.getTitulo());
        pelicula.setFechaSalida(dto.getFechaSalida());
        pelicula.setPrecio(dto.getPrecio());
        pelicula.setStock(dto.getStock());
        pelicula.setCondicion(dto.getCondicion());
        pelicula.setFormato(dto.getFormato());
        pelicula.setSinopsis(dto.getSinopsis());
        pelicula.setImagenAmpliada(dto.getImagenAmpliada());

        pelicula.setDirectores(
                obtenerOCrearDirector(dto.getDirector()));

        pelicula.setActores(
                obtenerOCrearActores(dto.getActores()));

        if (dto.getGenerosIds() != null) {

            pelicula.setGeneros(
                    dto.getGenerosIds()
                            .stream()
                            .map(id ->
                                    generoRepo.findById(id)
                                            .orElseThrow(() ->
                                                    new RuntimeException(
                                                            "Género no encontrado: "
                                                                    + id)))
                            .collect(Collectors.toSet()));
        }

        pelicula.setLastUpdate(LocalDateTime.now());

        return pelicula;
    }

        private Set<Director> obtenerOCrearDirector(String nombreDirector) {

        Set<Director> directores = new HashSet<>();

        if (nombreDirector == null || nombreDirector.isBlank()) {
            return directores;
        }

        Director director = directorRepo
                .findByNombreIgnoreCase(nombreDirector.trim())
                .orElseGet(() -> {

                    Director nuevo = new Director();
                    nuevo.setNombre(nombreDirector.trim());

                    return directorRepo.save(nuevo);
                });

        directores.add(director);

        return directores;
    }

    private Set<Actor> obtenerOCrearActores(String textoActores) {

        Set<Actor> actores = new HashSet<>();

        if (textoActores == null || textoActores.isBlank()) {
            return actores;
        }

        Arrays.stream(textoActores.split(","))
                .map(String::trim)
                .filter(nombre -> !nombre.isBlank())
                .forEach(nombre -> {

                    Actor actor = actorRepo
                            .findByNombreIgnoreCase(nombre)
                            .orElseGet(() -> {

                                Actor nuevo = new Actor();
                                nuevo.setNombre(nombre);

                                return actorRepo.save(nuevo);
                            });

                    actores.add(actor);
                });

        return actores;
    }

    // ==========================================================
    // Conversión para eventos RabbitMQ
    // ==========================================================

    private PeliculaSimplificada toPeliculaSimplificada(Pelicula pelicula) {

        return new PeliculaSimplificada(
                pelicula.getPeliculaId(),
                pelicula.getTitulo(),
                pelicula.getFechaSalida(),
                pelicula.getPrecio(),
                pelicula.getCondicion(),
                pelicula.getFormato(),
                pelicula.getSinopsis(),
                pelicula.getImagenAmpliada(),
                pelicula.getStock(),
                pelicula.getLastUpdate()
        );
    }

}
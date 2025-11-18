package com.unrn.peliculas.service;

import com.unrn.peliculas.domain.Actor;
import com.unrn.peliculas.domain.Director;
import com.unrn.peliculas.domain.Genero;
import com.unrn.peliculas.domain.Pelicula;
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
import java.util.List;
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
        Pelicula p = toEntity(dto);
        peliculaRepo.save(p);
        
        // Publicar evento CREATE vía RabbitMQ
        Event<Integer, PeliculaSimplificada> evento = new Event<>(
            EventType.CREATE,
            p.getPeliculaId(),
            toPeliculaSimplificada(p)
        );
        eventPublisher.enviarEvento(evento);
        
        return toDTO(p);
    }

    public PeliculaDTO editarPelicula(Integer id, PeliculaDTO dto) {
        Pelicula p = peliculaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));

        // actualizar datos básicos
        p.setTitulo(dto.getTitulo());
        p.setFechaSalida(dto.getFechaSalida());
        p.setPrecio(dto.getPrecio());
        p.setCondicion(dto.getCondicion());
        p.setFormato(dto.getFormato());
        p.setSinopsis(dto.getSinopsis());
        p.setImagenAmpliada(dto.getImagenAmpliada());

        // actualizar relaciones
        if(dto.getActoresIds() != null) {
            p.setActores(dto.getActoresIds().stream()
                    .map(idActor -> actorRepo.findById(idActor)
                            .orElseThrow(() -> new RuntimeException("Actor no encontrado: " + idActor)))
                    .collect(Collectors.toSet()));
        }

        if(dto.getDirectoresIds() != null) {
            p.setDirectores(dto.getDirectoresIds().stream()
                    .map(idDirector -> directorRepo.findById(idDirector)
                            .orElseThrow(() -> new RuntimeException("Director no encontrado: " + idDirector)))
                    .collect(Collectors.toSet()));
        }

        if(dto.getGenerosIds() != null) {
            p.setGeneros(dto.getGenerosIds().stream()
                    .map(idGenero -> generoRepo.findById(idGenero)
                            .orElseThrow(() -> new RuntimeException("Género no encontrado: " + idGenero)))
                    .collect(Collectors.toSet()));
        }

        // Actualizar automáticamente la fecha de última modificación
        p.setLastUpdate(LocalDateTime.now());

        peliculaRepo.save(p);
        
        // Publicar evento UPDATE vía RabbitMQ
        Event<Integer, PeliculaSimplificada> evento = new Event<>(
            EventType.UPDATE,
            p.getPeliculaId(),
            toPeliculaSimplificada(p)
        );
        eventPublisher.enviarEvento(evento);
        
        return toDTO(p);
    }

    public PeliculaDTO obtenerDetallePelicula(Integer id) {
        Pelicula p = peliculaRepo.findByIdWithRelations(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Película no encontrada"));
        return toDTO(p);
    }

    public List<PeliculaDTO> listarTodasLasPeliculas() {
        return peliculaRepo.findAllWithRelations().stream()
                .map(this::toDTOLista)
                .collect(Collectors.toList());
    }

    // Listar películas con filtros múltiples
    public List<PeliculaDTO> listarPeliculasFiltradas(String titulo, String genero, String director, String actor,
                                                      Integer anio, BigDecimal precioMax, String formato) {
        List<Pelicula> peliculas = peliculaRepo.findByFiltros(titulo, genero, director, actor, anio, precioMax, formato);
        return peliculas.stream()
                .map(this::toDTOLista)
                .collect(Collectors.toList());
    }

    public List<PeliculaDTO> listarPorGenero(String genero) {
        return peliculaRepo.findByFiltros(null, genero, null, null, null, null, null)
                .stream()
                .map(this::toDTOLista)
                .collect(Collectors.toList());
    }

    public List<PeliculaDTO> listarPorDirector(String director) {
        return peliculaRepo.findByFiltros(null, null, director, null, null, null, null)
                .stream()
                .map(this::toDTOLista)
                .collect(Collectors.toList());
    }

    public List<PeliculaDTO> listarPorActor(String actor) {
        return peliculaRepo.findByFiltros(null, null, null, actor, null, null, null)
                .stream()
                .map(this::toDTOLista)
                .collect(Collectors.toList());
    }

    // =======================
    // Métodos de búsqueda y filtros especializados
    // =======================

    public List<PeliculaDTO> buscarPeliculas(String query) {
        if (query == null || query.trim().isEmpty()) {
            return listarTodasLasPeliculas();
        }

        String queryLower = query.toLowerCase().trim();
        return peliculaRepo.findByFiltros(queryLower, queryLower, queryLower, queryLower, null, null, null)
                .stream()
                .map(this::toDTOLista)
                .collect(Collectors.toList());
    }

    public List<PeliculaDTO> listarPeliculasRecientes() {
        int añoActual = LocalDate.now().getYear();
        return peliculaRepo.findAllWithRelations().stream()
                .filter(p -> p.getFechaSalida().getYear() >= añoActual - 2)
                .map(this::toDTOLista)
                .collect(Collectors.toList());
    }

    public List<PeliculaDTO> listarPeliculasEnOferta() {
        return peliculaRepo.findAllWithRelations().stream()
                .filter(p -> p.getPrecio().compareTo(BigDecimal.valueOf(10.0)) < 0)
                .map(this::toDTOLista)
                .collect(Collectors.toList());
    }

    // =======================
    // Métodos privados de conversión
    // =======================

    // DTO completo para detalles
    private PeliculaDTO toDTO(Pelicula p) {
        return PeliculaDTO.builder()
                .peliculaId(p.getPeliculaId())
                .titulo(p.getTitulo())
                .fechaSalida(p.getFechaSalida())
                .precio(p.getPrecio())
                .condicion(p.getCondicion())
                .formato(p.getFormato())
                .sinopsis(p.getSinopsis())
                .imagenAmpliada(p.getImagenAmpliada())
                // Devolver nombres para detalle
                .actores(p.getActores().stream().map(Actor::getNombre).collect(Collectors.toList()))
                .directores(p.getDirectores().stream().map(Director::getNombre).collect(Collectors.toList()))
                .generos(p.getGeneros().stream().map(Genero::getNombre).collect(Collectors.toList()))
                .build();
    }

    // DTO simplificado para listas
    private PeliculaDTO toDTOLista(Pelicula p) {
        return PeliculaDTO.builder()
                .peliculaId(p.getPeliculaId())
                .titulo(p.getTitulo())
                .fechaSalida(p.getFechaSalida())
                .precio(p.getPrecio())
                .condicion(p.getCondicion())
                .formato(p.getFormato())
                .sinopsis(p.getSinopsis() != null && p.getSinopsis().length() > 100 ?
                        p.getSinopsis().substring(0, 100) + "..." : p.getSinopsis())
                .imagenAmpliada(p.getImagenAmpliada())
                // Solo primeros elementos para listas
                .actores(p.getActores().stream().limit(3).map(Actor::getNombre).collect(Collectors.toList()))
                .directores(p.getDirectores().stream().limit(2).map(Director::getNombre).collect(Collectors.toList()))
                .generos(p.getGeneros().stream().limit(2).map(Genero::getNombre).collect(Collectors.toList()))
                .build();
    }

    private Pelicula toEntity(PeliculaDTO dto) {
        Pelicula p = new Pelicula();
        p.setTitulo(dto.getTitulo());
        p.setFechaSalida(dto.getFechaSalida());
        p.setPrecio(dto.getPrecio());
        p.setCondicion(dto.getCondicion());
        p.setFormato(dto.getFormato());
        p.setSinopsis(dto.getSinopsis());
        p.setImagenAmpliada(dto.getImagenAmpliada());

        // Mapear relaciones solo si vienen los IDs
        if(dto.getActoresIds() != null) {
            p.setActores(dto.getActoresIds().stream()
                    .map(id -> actorRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException("Actor no encontrado: " + id)))
                    .collect(Collectors.toSet()));
        }

        if(dto.getDirectoresIds() != null) {
            p.setDirectores(dto.getDirectoresIds().stream()
                    .map(id -> directorRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException("Director no encontrado: " + id)))
                    .collect(Collectors.toSet()));
        }

        if(dto.getGenerosIds() != null) {
            p.setGeneros(dto.getGenerosIds().stream()
                    .map(id -> generoRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException("Género no encontrado: " + id)))
                    .collect(Collectors.toSet()));
        }

        // Establecer automáticamente la fecha de última modificación al crear
        p.setLastUpdate(LocalDateTime.now());

        return p;
    }

    // Convertir Pelicula a PeliculaSimplificada para eventos RabbitMQ
    private PeliculaSimplificada toPeliculaSimplificada(Pelicula p) {
        return new PeliculaSimplificada(
            p.getPeliculaId(),
            p.getTitulo(),
            p.getFechaSalida(),
            p.getPrecio(),
            p.getCondicion(),
            p.getFormato(),
            p.getSinopsis(),
            p.getImagenAmpliada(),
            p.getLastUpdate()
        );
    }
}

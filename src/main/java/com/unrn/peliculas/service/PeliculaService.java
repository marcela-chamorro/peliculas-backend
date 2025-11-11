package com.unrn.peliculas.service;

import com.unrn.peliculas.domain.Actor;
import com.unrn.peliculas.domain.Director;
import com.unrn.peliculas.domain.Genero;
import com.unrn.peliculas.domain.Pelicula;
import com.unrn.peliculas.dto.PeliculaDTO;
import com.unrn.peliculas.repository.ActorRepository;
import com.unrn.peliculas.repository.DirectorRepository;
import com.unrn.peliculas.repository.GeneroRepository;
import com.unrn.peliculas.repository.PeliculaRepository;
import com.unrn.peliculas.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
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
    private RabbitTemplate rabbitTemplate; // 🔹 Para enviar mensajes a RabbitMQ

    private static final String ROUTING_KEY = "pelicula.evento";

    // =======================
    // Crear película (Publica mensaje)
    // =======================
    public PeliculaDTO crearPelicula(PeliculaDTO dto) {
        Pelicula p = toEntity(dto);
        peliculaRepo.save(p);

        PeliculaDTO peliculaCreada = toDTO(p);

        // 🔹 Enviar mensaje a RabbitMQ cuando se crea la película
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, ROUTING_KEY, peliculaCreada);
        System.out.println("📤 Enviado mensaje RabbitMQ: Nueva película creada -> " + peliculaCreada.getTitulo());

        return peliculaCreada;
    }

    // =======================
    // Editar película (Publica mensaje)
    // =======================
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
        if (dto.getActoresIds() != null) {
            p.setActores(dto.getActoresIds().stream()
                    .map(idActor -> actorRepo.findById(idActor)
                            .orElseThrow(() -> new RuntimeException("Actor no encontrado: " + idActor)))
                    .collect(Collectors.toSet()));
        }

        if (dto.getDirectoresIds() != null) {
            p.setDirectores(dto.getDirectoresIds().stream()
                    .map(idDirector -> directorRepo.findById(idDirector)
                            .orElseThrow(() -> new RuntimeException("Director no encontrado: " + idDirector)))
                    .collect(Collectors.toSet()));
        }

        if (dto.getGenerosIds() != null) {
            p.setGeneros(dto.getGenerosIds().stream()
                    .map(idGenero -> generoRepo.findById(idGenero)
                            .orElseThrow(() -> new RuntimeException("Género no encontrado: " + idGenero)))
                    .collect(Collectors.toSet()));
        }

        peliculaRepo.save(p);
        PeliculaDTO peliculaEditada = toDTO(p);

        // 🔹 Enviar mensaje a RabbitMQ cuando se edita la película
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, ROUTING_KEY, peliculaEditada);
        System.out.println("📤 Enviado mensaje RabbitMQ: Película editada -> " + peliculaEditada.getTitulo());

        return peliculaEditada;
    }

    // =======================
    // Obtener detalle película
    // =======================
    public PeliculaDTO obtenerDetallePelicula(Integer id) {
        Pelicula p = peliculaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Película no encontrada"));
        return toDTO(p);
    }

    // =======================
    // Métodos privados de conversión
    // =======================
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
                .actores(p.getActores().stream().map(Actor::getNombre).toList())
                .directores(p.getDirectores().stream().map(Director::getNombre).toList())
                .generos(p.getGeneros().stream().map(Genero::getNombre).toList())
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

        if (dto.getActoresIds() != null) {
            p.setActores(dto.getActoresIds().stream()
                    .map(id -> actorRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException("Actor no encontrado: " + id)))
                    .collect(Collectors.toSet()));
        }

        if (dto.getDirectoresIds() != null) {
            p.setDirectores(dto.getDirectoresIds().stream()
                    .map(id -> directorRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException("Director no encontrado: " + id)))
                    .collect(Collectors.toSet()));
        }

        if (dto.getGenerosIds() != null) {
            p.setGeneros(dto.getGenerosIds().stream()
                    .map(id -> generoRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException("Género no encontrado: " + id)))
                    .collect(Collectors.toSet()));
        }

        return p;
    }
}

package com.unrn.peliculas.service;

import com.unrn.peliculas.domain.Actor;
import com.unrn.peliculas.domain.Director;
import com.unrn.peliculas.domain.Genero;
import com.unrn.peliculas.domain.Pelicula;
import com.unrn.peliculas.dto.PeliculaDTO;
import com.unrn.peliculas.repository.PeliculaRepository;
import com.unrn.peliculas.repository.ActorRepository;
import com.unrn.peliculas.repository.DirectorRepository;
import com.unrn.peliculas.repository.GeneroRepository;
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

    // Crear
    public PeliculaDTO crearPelicula(PeliculaDTO dto) {
        Pelicula p = toEntity(dto);
        peliculaRepo.save(p);
        return toDTO(p);
    }

    // Editar
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
            p.setActores((Set<Actor>) dto.getActoresIds().stream()
                    .map(idActor -> actorRepo.findById(idActor)
                            .orElseThrow(() -> new RuntimeException("Actor no encontrado: " + idActor)))
                    .toList());
        }

        if(dto.getDirectoresIds() != null) {
            p.setDirectores((Set<Director>) dto.getDirectoresIds().stream()
                    .map(idDirector -> directorRepo.findById(idDirector)
                            .orElseThrow(() -> new RuntimeException("Director no encontrado: " + idDirector)))
                    .toList());
        }

        if(dto.getGenerosIds() != null) {
            p.setGeneros((Set<Genero>) dto.getGenerosIds().stream()
                    .map(idGenero -> generoRepo.findById(idGenero)
                            .orElseThrow(() -> new RuntimeException("Género no encontrado: " + idGenero)))
                    .toList());
        }

        peliculaRepo.save(p);
        return toDTO(p);
    }

    // Obtener detalle
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
                // Devolver nombres para detalle
                .actores(p.getActores().stream().map(a -> a.getNombre()).toList())
                .directores(p.getDirectores().stream().map(d -> d.getNombre()).toList())
                .generos(p.getGeneros().stream().map(g -> g.getNombre()).toList())
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

        return p;
    }
}

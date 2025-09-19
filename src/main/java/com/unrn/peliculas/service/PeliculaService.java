package com.unrn.peliculas.service;

import com.unrn.peliculas.domain.Pelicula;
import com.unrn.peliculas.dto.PeliculaDTO;
import com.unrn.peliculas.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

@Service
@Validated
public class PeliculaService {

    @Autowired
    private PeliculaRepository peliculaRepo;

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

        // actualizar datos
        p.setTitulo(dto.getTitulo());
        p.setFechaSalida(dto.getFechaSalida());
        p.setPrecio(dto.getPrecio());
        p.setCondicion(dto.getCondicion());
        p.setFormato(dto.getFormato());
        p.setSinopsis(dto.getSinopsis());
        p.setImagenAmpliada(dto.getImagenAmpliada());

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
                .actoresIds(
                        p.getActores()
                                .stream()
                                .map(a -> a.getActorId().longValue())
                                .toList()
                )
                .directoresIds(
                        p.getDirectores()
                                .stream()
                                .map(d -> d.getDirectorId().longValue())
                                .toList()
                )
                .generosIds(
                        p.getGeneros()
                                .stream()
                                .map(g -> g.getGeneroId().longValue())
                                .toList()
                )
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
        return p;
    }
}

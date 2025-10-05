package com.unrn.peliculas.service;

import com.unrn.peliculas.domain.Catalogo;
import com.unrn.peliculas.domain.Pelicula;
import com.unrn.peliculas.dto.CatalogoDTO;
import com.unrn.peliculas.dto.PeliculaDTO;
import com.unrn.peliculas.repository.CatalogoRepository;
import com.unrn.peliculas.repository.PeliculaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class CatalogoService {

        @Autowired
        private CatalogoRepository catalogoRepo;
        @Autowired
        private PeliculaRepository peliculaRepo;

        public CatalogoDTO crear(CatalogoDTO dto) {
                Catalogo catalogo = toEntity(dto);
                catalogoRepo.save(catalogo);
                return toDto(catalogo);
        }

        public CatalogoDTO obtener(Integer id) {
                Catalogo catalogo = catalogoRepo.findById(id)
                                .orElseThrow(() -> new RuntimeException("Catálogo no encontrado"));
                return toDto(catalogo);
        }

        public List<CatalogoDTO> listar() {
                return catalogoRepo.findAll().stream()
                                .map(this::toDto)
                                .collect(Collectors.toList());
        }

        public CatalogoDTO agregarPelicula(Integer catalogoId, Integer peliculaId) {
                Catalogo catalogo = catalogoRepo.findById(catalogoId)
                                .orElseThrow(() -> new RuntimeException("Catálogo no encontrado"));

                Pelicula pelicula = peliculaRepo.findById(peliculaId)
                                .orElseThrow(() -> new RuntimeException("Película no encontrada"));

                if (!catalogo.getPeliculas().contains(pelicula)) {
                        catalogo.getPeliculas().add(pelicula);
                        catalogoRepo.save(catalogo);
                }

                return toDto(catalogo);
        }

        public CatalogoDTO removerPelicula(Integer catalogoId, Integer peliculaId) {
                Catalogo catalogo = catalogoRepo.findById(catalogoId)
                                .orElseThrow(() -> new RuntimeException("Catálogo no encontrado"));

                Pelicula pelicula = peliculaRepo.findById(peliculaId)
                                .orElseThrow(() -> new RuntimeException("Película no encontrada"));

                if (catalogo.getPeliculas().contains(pelicula)) {
                        catalogo.getPeliculas().remove(pelicula);
                        catalogoRepo.save(catalogo);
                }

                return toDto(catalogo);
        }

        private CatalogoDTO toDto(Catalogo catalogo) {
                return CatalogoDTO.builder()
                                .catalogoId(catalogo.getCatalogoId())
                                .nombre(catalogo.getNombre())
                                .descripcion(catalogo.getDescripcion())
                                .peliculas(catalogo.getPeliculas().stream()
                                                .map(p -> PeliculaDTO.builder()
                                                                .peliculaId(p.getPeliculaId())
                                                                .titulo(p.getTitulo())
                                                                .fechaSalida(p.getFechaSalida())
                                                                .precio(p.getPrecio())
                                                                .condicion(p.getCondicion())
                                                                .formato(p.getFormato())
                                                                .sinopsis(p.getSinopsis())
                                                                .imagenAmpliada(p.getImagenAmpliada())
                                                                .actores(p.getActores().stream().map(a -> a.getNombre())
                                                                                .collect(Collectors.toList()))
                                                                .directores(
                                                                                p.getDirectores().stream()
                                                                                                .map(d -> d.getNombre())
                                                                                                .collect(Collectors
                                                                                                                .toList()))
                                                                .generos(p.getGeneros().stream().map(g -> g.getNombre())
                                                                                .collect(Collectors.toList()))
                                                                .build())
                                                .collect(Collectors.toList()))
                                .build();
        }

        private Catalogo toEntity(CatalogoDTO dto) {
                Catalogo catalogo = new Catalogo();
                catalogo.setNombre(dto.getNombre());
                catalogo.setDescripcion(dto.getDescripcion());
                if (dto.getPeliculas() != null) {
                        List<Pelicula> peliculas = dto.getPeliculas().stream()
                                        .map(pDTO -> peliculaRepo.findById(pDTO.getPeliculaId())
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "Película no encontrada: "
                                                                                        + pDTO.getPeliculaId())))
                                        .collect(Collectors.toList());
                        catalogo.setPeliculas(peliculas);
                }
                return catalogo;
        }
}

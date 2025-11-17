package com.unrn.peliculas.repository;

import com.unrn.peliculas.domain.Actor;
import com.unrn.peliculas.domain.Director;
import com.unrn.peliculas.domain.Genero;
import com.unrn.peliculas.domain.Pelicula;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Integer>, JpaSpecificationExecutor<Pelicula> {

    // Método usando Criteria API para evitar problemas de tipos
    @EntityGraph(attributePaths = {"actores", "directores", "generos"})
    default List<Pelicula> findByFiltros(String titulo, String genero, String director, String actor,
                                         Integer anio, BigDecimal precioMax, String formato) {
        return findAll((Specification<Pelicula>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Join con las relaciones
            Join<Pelicula, Actor> actorJoin = root.join("actores", JoinType.LEFT);
            Join<Pelicula, Director> directorJoin = root.join("directores", JoinType.LEFT);
            Join<Pelicula, Genero> generoJoin = root.join("generos", JoinType.LEFT);

            // Filtro por título
            if (titulo != null && !titulo.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("titulo")),
                        "%" + titulo.toLowerCase() + "%"
                ));
            }

            // Filtro por género
            if (genero != null && !genero.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(generoJoin.get("nombre")),
                        "%" + genero.toLowerCase() + "%"
                ));
            }

            // Filtro por director
            if (director != null && !director.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(directorJoin.get("nombre")),
                        "%" + director.toLowerCase() + "%"
                ));
            }

            // Filtro por actor
            if (actor != null && !actor.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(actorJoin.get("nombre")),
                        "%" + actor.toLowerCase() + "%"
                ));
            }

            // Filtro por año
            if (anio != null) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.function("year", Integer.class, root.get("fechaSalida")),
                        anio
                ));
            }

            // Filtro por precio máximo
            if (precioMax != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("precio"), precioMax));
            }

            // Filtro por formato
            if (formato != null && !formato.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("formato")),
                        formato.toLowerCase()
                ));
            }

            query.distinct(true);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    // Mantener los otros métodos
    @EntityGraph(attributePaths = {"actores", "directores", "generos"})
    @Query("SELECT p FROM Pelicula p")
    List<Pelicula> findAllWithRelations();

    @EntityGraph(attributePaths = {"actores", "directores", "generos"})
    @Query("SELECT p FROM Pelicula p WHERE p.peliculaId = :id")
    Optional<Pelicula> findByIdWithRelations(@Param("id") Integer id);
}

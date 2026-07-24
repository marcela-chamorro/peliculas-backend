package com.unrn.peliculas.repository;

import com.unrn.peliculas.domain.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

    Optional<Actor> findByNombreIgnoreCase(String nombre);

}
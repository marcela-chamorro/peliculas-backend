package com.unrn.peliculas.repository;

import com.unrn.peliculas.domain.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Integer> {

    Optional<Director> findByNombreIgnoreCase(String nombre);

}

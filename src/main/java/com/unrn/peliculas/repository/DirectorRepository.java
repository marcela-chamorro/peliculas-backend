package com.unrn.peliculas.repository;

import com.unrn.peliculas.domain.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Integer> {
}

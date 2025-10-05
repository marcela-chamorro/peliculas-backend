package com.unrn.peliculas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unrn.peliculas.domain.Catalogo;

@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Integer> {
}
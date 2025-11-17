package com.unrn.peliculas.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO simplificado de Película para eventos RabbitMQ
 * Coincide con la estructura esperada por el servicio de carrito
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeliculaSimplificada {
    private String peliculaId; // String para coincidir con carrito
    private String titulo;
    private LocalDate fechaSalida;
    private BigDecimal precio;
    private String condicion;
    private String formato;
    private String sinopsis;
    private String imagenAmpliada;
    private LocalDateTime lastUpdate;
}


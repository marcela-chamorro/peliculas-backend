package com.unrn.peliculas.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PeliculaDTO {

    private String titulo;
    private LocalDate fechaSalida;
    private BigDecimal precio;
    private String condicion;
    private String formato;
    private String sinopsis;
    private String imagenAmpliada;
    private List<Long> actoresIds;
    private List<Long> directoresIds;
    private List<Long> generosIds;

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getImagenAmpliada() {
        return imagenAmpliada;
    }

    public void setImagenAmpliada(String imagenAmpliada) {
        this.imagenAmpliada = imagenAmpliada;
    }

    public List<Long> getActoresIds() {
        return actoresIds;
    }

    public void setActoresIds(List<Long> actoresIds) {
        this.actoresIds = actoresIds;
    }

    public List<Long> getDirectoresIds() {
        return directoresIds;
    }

    public void setDirectoresIds(List<Long> directoresIds) {
        this.directoresIds = directoresIds;
    }

    public List<Long> getGenerosIds() {
        return generosIds;
    }

    public void setGenerosIds(List<Long> generosIds) {
        this.generosIds = generosIds;
    }

}

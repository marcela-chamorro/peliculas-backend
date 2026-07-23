package com.unrn.peliculas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unrn.peliculas.dto.PeliculaDTO;
import com.unrn.peliculas.service.PeliculaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PeliculaController.class)
class PeliculaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PeliculaService peliculaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCrearPelicula() throws Exception {
        // Arrange
        PeliculaDTO inputDto = new PeliculaDTO();
        inputDto.setTitulo("Interstellar");
        inputDto.setPrecio(new BigDecimal("2000.00"));

        PeliculaDTO savedDto = new PeliculaDTO();
        savedDto.setPeliculaId(1);
        savedDto.setTitulo("Interstellar");
        savedDto.setPrecio(new BigDecimal("2000.00"));

        when(peliculaService.crearPelicula(any(PeliculaDTO.class))).thenReturn(savedDto);

        // Act & Assert
        mockMvc.perform(post("/peliculas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.peliculaId").value(1))
                .andExpect(jsonPath("$.titulo").value("Interstellar"))
                .andExpect(jsonPath("$.precio").value(2000.00));
    }

    @Test
    void testObtenerDetallePelicula() throws Exception {
        // Arrange
        PeliculaDTO dto = new PeliculaDTO();
        dto.setPeliculaId(1);
        dto.setTitulo("Batman Begins");

        when(peliculaService.obtenerDetallePelicula(1)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/peliculas/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.peliculaId").value(1))
                .andExpect(jsonPath("$.titulo").value("Batman Begins"));
    }

    @Test
    void testListarPeliculasConFiltros() throws Exception {
        // Arrange
        PeliculaDTO dto1 = new PeliculaDTO();
        dto1.setTitulo("Matrix");
        PeliculaDTO dto2 = new PeliculaDTO();
        dto2.setTitulo("Matrix Reloaded");

        when(peliculaService.listarPeliculasFiltradas(
                eq("Matrix"), any(), any(), any(), any(), any(), any()))
                .thenReturn(Arrays.asList(dto1, dto2));

        // Act & Assert
        mockMvc.perform(get("/peliculas")
                .param("titulo", "Matrix"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("Matrix"))
                .andExpect(jsonPath("$[1].titulo").value("Matrix Reloaded"));
    }

    @Test
    void testObtenerDetallePelicula_NotFound() throws Exception {
        when(peliculaService.obtenerDetallePelicula(99))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Película no encontrada"));

        mockMvc.perform(get("/peliculas/{id}", 99))
                .andExpect(status().isNotFound());
    }
}

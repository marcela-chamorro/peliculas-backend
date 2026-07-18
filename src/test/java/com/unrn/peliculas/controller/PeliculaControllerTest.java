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
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import com.unrn.peliculas.config.SecurityConfig;

@WebMvcTest(
    controllers = PeliculaController.class,
    excludeAutoConfiguration = {
        SecurityAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class,
        OAuth2ResourceServerAutoConfiguration.class
    },
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityConfig.class
    )
)
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
        // Arrange: Simulamos que el servicio lanza una excepción que en Spring suele mapearse a 404
        when(peliculaService.obtenerDetallePelicula(99)).thenThrow(new RuntimeException("Película no encontrada"));

        // Act & Assert: Verificamos que se lance la excepción cuando se realiza la petición
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            mockMvc.perform(get("/peliculas/{id}", 99));
        });
        org.junit.jupiter.api.Assertions.assertTrue(exception.getCause().getMessage().contains("Película no encontrada"));
    }

    @Test
    void testEditarPelicula() throws Exception {
        // Arrange
        PeliculaDTO inputDto = new PeliculaDTO();
        inputDto.setTitulo("Inception (Updated)");
        inputDto.setPrecio(new BigDecimal("1800.00"));

        PeliculaDTO savedDto = new PeliculaDTO();
        savedDto.setPeliculaId(1);
        savedDto.setTitulo("Inception (Updated)");
        savedDto.setPrecio(new BigDecimal("1800.00"));

        when(peliculaService.editarPelicula(eq(1), any(PeliculaDTO.class))).thenReturn(savedDto);

        // Act & Assert
        mockMvc.perform(put("/peliculas/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.peliculaId").value(1))
                .andExpect(jsonPath("$.titulo").value("Inception (Updated)"))
                .andExpect(jsonPath("$.precio").value(1800.00));
    }

    @Test
    void testListarPorGenero() throws Exception {
        // Arrange
        PeliculaDTO dto = new PeliculaDTO();
        dto.setTitulo("Interstellar");

        when(peliculaService.listarPorGenero("Sci-Fi")).thenReturn(Arrays.asList(dto));

        // Act & Assert
        mockMvc.perform(get("/peliculas/genero/{genero}", "Sci-Fi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Interstellar"));
    }

    @Test
    void testListarPorDirector() throws Exception {
        // Arrange
        PeliculaDTO dto = new PeliculaDTO();
        dto.setTitulo("Dunkirk");

        when(peliculaService.listarPorDirector("Nolan")).thenReturn(Arrays.asList(dto));

        // Act & Assert
        mockMvc.perform(get("/peliculas/director/{director}", "Nolan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Dunkirk"));
    }

    @Test
    void testListarPorActor() throws Exception {
        // Arrange
        PeliculaDTO dto = new PeliculaDTO();
        dto.setTitulo("The Revenant");

        when(peliculaService.listarPorActor("DiCaprio")).thenReturn(Arrays.asList(dto));

        // Act & Assert
        mockMvc.perform(get("/peliculas/actor/{actor}", "DiCaprio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("The Revenant"));
    }
}

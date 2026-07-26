package com.unrn.peliculas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unrn.peliculas.dto.PeliculaDTO;
import com.unrn.peliculas.service.PeliculaService;
import com.unrn.peliculas.service.port.HistorialVentasPort;

import jakarta.servlet.ServletException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import java.util.Map;

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

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private HistorialVentasPort historialVentasPort;

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
        .with(jwt().jwt(jwt -> jwt.claim(
                "realm_access",
                Map.of("roles", List.of("admin"))
        )))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(inputDto)))
        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
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
    @WithMockUser
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
        .with(jwt().jwt(jwt -> jwt.claim(
                "realm_access",
                Map.of("roles", List.of("admin"))
        )))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(inputDto)))
        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
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

    @Test
    void testConsultarStock() throws Exception {
        when(peliculaService.consultarStock(1)).thenReturn(10);

        mockMvc.perform(get("/peliculas/{id}/stock", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    void testDescontarStock() throws Exception {
        PeliculaDTO dto = new PeliculaDTO();
        dto.setPeliculaId(1);
        dto.setTitulo("Inception");
        dto.setStock(8);

        when(peliculaService.descontarStock(1, 2)).thenReturn(dto);

        mockMvc.perform(post("/peliculas/{id}/descontar-stock", 1)
                .param("cantidad", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.peliculaId").value(1))
                .andExpect(jsonPath("$.stock").value(8));
    }

    @Test
    void testReponerStock() throws Exception {
        PeliculaDTO dto = new PeliculaDTO();
        dto.setPeliculaId(1);
        dto.setTitulo("Inception");
        dto.setStock(15);

        when(peliculaService.reponerStock(1, 5)).thenReturn(dto);

        mockMvc.perform(post("/peliculas/{id}/reponer-stock", 1)
                .param("cantidad", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.peliculaId").value(1))
                .andExpect(jsonPath("$.stock").value(15));
    }
}

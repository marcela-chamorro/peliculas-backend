package com.unrn.peliculas.controller;

import com.unrn.peliculas.dto.VentaPorPeliculaDTO;
import com.unrn.peliculas.dto.PeliculaDTO;
import com.unrn.peliculas.service.PeliculaService;
import com.unrn.peliculas.service.port.HistorialVentasPort;
import com.unrn.peliculas.dto.ActualizarStockDTO;
import com.unrn.peliculas.dto.DescuentoStockRequestDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/peliculas")
public class PeliculaController {

    @Autowired
    private PeliculaService peliculaService;

    @Autowired
    private HistorialVentasPort historialVentasPort;

    @PostMapping
    public PeliculaDTO crear(@RequestBody PeliculaDTO dto) {
        return peliculaService.crearPelicula(dto);
    }

    @PutMapping("/{id}")
    public PeliculaDTO editar(
            @PathVariable Integer id,
            @RequestBody PeliculaDTO dto) {
        return peliculaService.editarPelicula(id, dto);
    }

    @GetMapping("/stock/test")
    public ResponseEntity<List<VentaPorPeliculaDTO>> probarHistorial() {

        return ResponseEntity.ok(
                historialVentasPort.obtenerVentasPorPelicula());
    }

    @PutMapping("/descontar-stock")
    public ResponseEntity<Void> descontarStock(
            @RequestBody DescuentoStockRequestDTO request) {

        peliculaService.descontarStock(request);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Void> actualizarStock(
            @PathVariable Integer id,
            @RequestBody ActualizarStockDTO dto) {

        peliculaService.actualizarStock(id, dto.getStock());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/buscar")
    public List<PeliculaDTO> buscarPeliculas(@RequestParam(required = false) String query) {
        return peliculaService.buscarPeliculas(query);
    }

    @GetMapping("/{id}")
    public PeliculaDTO obtenerDetallePelicula(@PathVariable Integer id) {
        return peliculaService.obtenerDetallePelicula(id);
    }

    @GetMapping
    public List<PeliculaDTO> listarPeliculas(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String actor,
            @RequestParam(required = false) Integer anio,
            @RequestParam(required = false) BigDecimal precioMax,
            @RequestParam(required = false) String formato) {

        return peliculaService.listarPeliculasFiltradas(
                titulo,
                genero,
                director,
                actor,
                anio,
                precioMax,
                formato);
    }

    @GetMapping("/genero/{genero}")
    public List<PeliculaDTO> listarPorGenero(@PathVariable String genero) {
        return peliculaService.listarPorGenero(genero);
    }

    @GetMapping("/director/{director}")
    public List<PeliculaDTO> listarPorDirector(@PathVariable String director) {
        return peliculaService.listarPorDirector(director);
    }

    @GetMapping("/actor/{actor}")
    public List<PeliculaDTO> listarPorActor(@PathVariable String actor) {
        return peliculaService.listarPorActor(actor);
    }

    @GetMapping("/{id}/stock")
    public Integer consultarStock(@PathVariable Integer id) {
        return peliculaService.consultarStock(id);
    }

    @PostMapping("/{id}/descontar-stock")
    public PeliculaDTO descontarStock(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "1") Integer cantidad) {
        return peliculaService.descontarStock(id, cantidad);
    }

    @PostMapping("/{id}/reponer-stock")
    public PeliculaDTO reponerStock(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "1") Integer cantidad) {
        return peliculaService.reponerStock(id, cantidad);
    }
}

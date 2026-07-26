package com.unrn.peliculas.service;

import com.unrn.peliculas.domain.Pelicula;
import com.unrn.peliculas.dto.VentaPorPeliculaDTO;
import com.unrn.peliculas.repository.PeliculaRepository;
import com.unrn.peliculas.service.port.HistorialVentasPort;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InicializadorStockService {

    private static final Logger log = LoggerFactory.getLogger(InicializadorStockService.class);

    private final PeliculaRepository peliculaRepository;
    private final HistorialVentasPort historialVentasPort;

    @Value("${pelicula.stock-inicial:50}")
    private Integer stockBaseInicial;

    public InicializadorStockService(PeliculaRepository peliculaRepository,
                                     HistorialVentasPort historialVentasPort) {
        this.peliculaRepository = peliculaRepository;
        this.historialVentasPort = historialVentasPort;
    }

    @PostConstruct
    public void inicializarStock() {

        List<VentaPorPeliculaDTO> ventas;

        try {
            ventas = historialVentasPort.obtenerVentasPorPelicula();
        } catch (Exception e) {
            log.warn("No fue posible obtener el historial de ventas. Se omite la inicialización automática del stock.", e);
            return;
        }

        Map<Integer, Integer> ventasPorPelicula =
                ventas.stream()
                        .collect(Collectors.toMap(
                                VentaPorPeliculaDTO::getPeliculaId,
                                VentaPorPeliculaDTO::getCantidadVendida
                        ));

        List<Pelicula> peliculas = peliculaRepository.findAll();

        for (Pelicula pelicula : peliculas) {

            if (pelicula.getStock() == null) {

                Integer cantidadVendida =
                        ventasPorPelicula.getOrDefault(
                                pelicula.getPeliculaId(),
                                0
                        );

                Integer stockInicial = stockBaseInicial - cantidadVendida;

                pelicula.setStock(stockInicial);

                peliculaRepository.save(pelicula);
            }
        }

        log.info("Inicialización de stock finalizada correctamente.");
    }
}

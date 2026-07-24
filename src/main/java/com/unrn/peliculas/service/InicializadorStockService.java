package com.unrn.peliculas.service;

import com.unrn.peliculas.domain.Pelicula;
import com.unrn.peliculas.dto.VentaPorPeliculaDTO;
import com.unrn.peliculas.repository.PeliculaRepository;
import com.unrn.peliculas.service.externo.ClienteHistorial;
import jakarta.annotation.PostConstruct;
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
    private final ClienteHistorial clienteHistorial;

    public InicializadorStockService(PeliculaRepository peliculaRepository,
                                     ClienteHistorial clienteHistorial) {
        this.peliculaRepository = peliculaRepository;
        this.clienteHistorial = clienteHistorial;
    }

    @PostConstruct
    public void inicializarStock() {

        List<VentaPorPeliculaDTO> ventas;

        try {
            ventas = clienteHistorial.obtenerVentasPorPelicula();
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

                Integer stockInicial = 50 - cantidadVendida;

                pelicula.setStock(stockInicial);

                peliculaRepository.save(pelicula);
            }
        }

        log.info("Inicialización de stock finalizada correctamente.");
    }
}

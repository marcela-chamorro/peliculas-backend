package com.unrn.peliculas.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unrn.peliculas.dto.DescuentoStockDTO;
import com.unrn.peliculas.dto.DescuentoStockRequestDTO;
import com.unrn.peliculas.service.PeliculaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompraStockListener {

    private final PeliculaService peliculaService;
    private final ObjectMapper objectMapper;

    public CompraStockListener(PeliculaService peliculaService, ObjectMapper objectMapper) {
        this.peliculaService = peliculaService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${rabbitmq.event.compra.queue.name:peliculas.stock.queue}")
    public void recibirEventoCompra(Object evento) {
        if (evento == null) {
            return;
        }

        try {
            JsonNode node = objectMapper.valueToTree(evento);
            List<DescuentoStockDTO> itemsDescuento = new ArrayList<>();

            if (node.has("items") && node.get("items").isArray()) {
                for (JsonNode itemNode : node.get("items")) {
                    if (itemNode.has("peliculaId") && itemNode.has("cantidad")) {
                        DescuentoStockDTO dto = new DescuentoStockDTO();
                        dto.setPeliculaId(itemNode.get("peliculaId").asInt());
                        dto.setCantidad(itemNode.get("cantidad").asInt());
                        itemsDescuento.add(dto);
                    }
                }
            } else if (node.has("productos") && node.get("productos").isArray()) {
                for (JsonNode prodNode : node.get("productos")) {
                    if (prodNode.has("idProducto") && prodNode.has("cantidad")) {
                        DescuentoStockDTO dto = new DescuentoStockDTO();
                        dto.setPeliculaId(Integer.parseInt(prodNode.get("idProducto").asText()));
                        dto.setCantidad(prodNode.get("cantidad").asInt());
                        itemsDescuento.add(dto);
                    }
                }
            }

            if (!itemsDescuento.isEmpty()) {
                // El descuento de stock ya fue procesado de manera síncrona durante el checkout
                // vía REST para garantizar la validación de disponibilidad y consistencia inmediata.
                System.out.println("Evento de compra recibido en peliculas.stock.queue (descuento síncrono previamente realizado).");
            }
        } catch (Exception e) {
            System.err.println("Error descontando stock tras evento de compra: " + e.getMessage());
        }
    }
}

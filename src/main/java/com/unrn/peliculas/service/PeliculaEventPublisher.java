package com.unrn.peliculas.service;

import com.unrn.peliculas.event.dto.Event;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PeliculaEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;

    public PeliculaEventPublisher(RabbitTemplate rabbitTemplate,
                                  @Value("${app.rabbitmq.exchange}") String exchangeName) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.routingKey = "pelicula.evento"; // Routing key que espera carrito
    }

    public void enviarEvento(Event<String, ?> evento) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, evento);
        System.out.println("Evento publicado en " + exchangeName + " con routing key " + routingKey + ": " + evento.getEventType() + " para película " + evento.getKey());
    }
}

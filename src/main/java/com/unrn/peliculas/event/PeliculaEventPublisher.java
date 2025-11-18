package com.unrn.peliculas.event;

import com.unrn.peliculas.event.dto.Event;
import com.unrn.peliculas.event.dto.PeliculaSimplificada;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PeliculaEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchangeName;

    // routing key que Carrito va a escuchar
    @Value("${app.rabbitmq.routing-key:pelicula.event}")
    private String routingKey;

    public PeliculaEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(Event<String, PeliculaSimplificada> event) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, event);
        System.out.println("📤 Evento enviado a RabbitMQ: " + event);
    }
}

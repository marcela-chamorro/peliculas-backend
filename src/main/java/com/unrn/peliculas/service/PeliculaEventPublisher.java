package com.unrn.peliculas.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.unrn.peliculas.config.RabbitMQConfig;

@Service
public class PeliculaEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public PeliculaEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarEvento(String tipoEvento, Object mensaje) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, tipoEvento, mensaje);
        System.out.println("📤 Evento publicado: " + tipoEvento + " → " + mensaje);
    }
}
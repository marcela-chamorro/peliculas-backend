package com.unrn.peliculas.service;

import com.unrn.peliculas.event.EventType;
import com.unrn.peliculas.event.dto.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeliculaEventPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private PeliculaEventPublisher eventPublisher;
    private final String exchangeName = "pelicula_exchange";
    private final String routingKey = "pelicula.event";

    @BeforeEach
    void setUp() {
        eventPublisher = new PeliculaEventPublisher(rabbitTemplate, exchangeName, routingKey);
    }

    @Test
    void testEnviarEvento() {
        // Arrange
        Event<Integer, String> event = new Event<>(EventType.CREATE, 1, "test-payload");

        // Act
        eventPublisher.enviarEvento(event);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend(exchangeName, routingKey, event);
    }
}

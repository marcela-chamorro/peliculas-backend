package com.unrn.peliculas.controller;

import com.unrn.peliculas.dto.ActorDTO;
import com.unrn.peliculas.service.ActorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ActorControllerTest {

    @Mock
    private ActorService actorService;

    @InjectMocks
    private ActorController actorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodos() {
        ActorDTO dto = new ActorDTO();
        dto.setActorId((short) 1);
        when(actorService.obtenerTodos()).thenReturn(Collections.singletonList(dto));

        List<ActorDTO> result = actorController.obtenerTodos();

        assertEquals(1, result.size());
    }
}

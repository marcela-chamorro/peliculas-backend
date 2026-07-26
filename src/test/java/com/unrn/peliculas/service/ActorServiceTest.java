package com.unrn.peliculas.service;

import com.unrn.peliculas.domain.Actor;
import com.unrn.peliculas.dto.ActorDTO;
import com.unrn.peliculas.repository.ActorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class ActorServiceTest {

    @Mock
    private ActorRepository actorRepository;

    @InjectMocks
    private ActorService actorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerTodos() {
        Actor actor = new Actor();
        actor.setActorId((short) 1);
        actor.setNombre("DiCaprio");

        when(actorRepository.findAll()).thenReturn(Collections.singletonList(actor));

        List<ActorDTO> result = actorService.obtenerTodos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals((short) 1, result.get(0).getActorId());
        assertEquals("DiCaprio", result.get(0).getNombre());
    }
}

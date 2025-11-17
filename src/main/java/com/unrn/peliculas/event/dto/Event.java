package com.unrn.peliculas.event.dto;

import com.unrn.peliculas.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event<K, T> {
    private EventType eventType;
    private K key;
    private T data;
}


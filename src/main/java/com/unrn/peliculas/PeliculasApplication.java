package com.unrn.peliculas;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class PeliculasApplication {
    public static void main(String[] args) {
        SpringApplication.run(PeliculasApplication.class, args);
    }
}
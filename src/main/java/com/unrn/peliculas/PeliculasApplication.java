package com.unrn.peliculas;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.TimeZone;

@EnableRabbit
@SpringBootApplication
public class PeliculasApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));

        System.out.println("Zona JVM: " + TimeZone.getDefault().getID());
        SpringApplication.run(PeliculasApplication.class, args);
    }
}
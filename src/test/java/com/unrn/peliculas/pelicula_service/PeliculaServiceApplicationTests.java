package com.unrn.peliculas.pelicula_service;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:peliculas_test_db;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE",
    "spring.sql.init.mode=never"
})
class PeliculaServiceApplicationTests {

	@MockBean
	private ConnectionFactory connectionFactory;

	@MockBean
	private RabbitTemplate rabbitTemplate;

	@MockBean
	private JwtDecoder jwtDecoder;

	@Test
	void contextLoads() {
	}

}

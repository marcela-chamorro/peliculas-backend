-- Insertar directores
INSERT INTO directores (nombre, last_update) VALUES
('Christopher Nolan', CURRENT_TIMESTAMP),
('Steven Spielberg', CURRENT_TIMESTAMP),
('Quentin Tarantino', CURRENT_TIMESTAMP);

-- Insertar actores
INSERT INTO actores (nombre, last_update) VALUES
('Leonardo DiCaprio', CURRENT_TIMESTAMP),
('Brad Pitt', CURRENT_TIMESTAMP),
('Tom Hanks', CURRENT_TIMESTAMP),
('Scarlett Johansson', CURRENT_TIMESTAMP);

-- Insertar géneros
INSERT INTO generos (nombre, last_update) VALUES
('Acción', CURRENT_TIMESTAMP),
('Drama', CURRENT_TIMESTAMP),
('Ciencia Ficción', CURRENT_TIMESTAMP),
('Comedia', CURRENT_TIMESTAMP);

-- Insertar películas
INSERT INTO peliculas (titulo, fecha_salida, precio, condicion, formato, sinopsis, imagen_ampliada, last_update) VALUES
('Inception', '2010-07-16', 2500.00, 'Nuevo', 'Blu-ray', 'Un ladrón que roba secretos a través de sueños es contratado para implantar una idea en la mente de un CEO.', 'inception.jpg', CURRENT_TIMESTAMP),
('Pulp Fiction', '1994-10-14', 1800.00, 'Usado', 'DVD', 'Historias entrelazadas de crimen y redención en Los Ángeles.', 'pulpfiction.jpg', CURRENT_TIMESTAMP),
('Jurassic Park', '1993-06-11', 2000.00, 'Nuevo', '4K UHD', 'Un parque temático con dinosaurios clonados se convierte en un caos cuando las criaturas escapan.', 'jurassicpark.jpg', CURRENT_TIMESTAMP);

-- Relacionar películas con directores
INSERT INTO pelicula_directores (pelicula_id, director_id, last_update) VALUES
(1, 1, CURRENT_TIMESTAMP), -- Inception - Nolan
(2, 3, CURRENT_TIMESTAMP), -- Pulp Fiction - Tarantino
(3, 2, CURRENT_TIMESTAMP); -- Jurassic Park - Spielberg

-- Relacionar películas con actores
INSERT INTO pelicula_actores (pelicula_id, actor_id, last_update) VALUES
(1, 1, CURRENT_TIMESTAMP), -- Inception - DiCaprio
(2, 1, CURRENT_TIMESTAMP), -- Pulp Fiction - DiCaprio (ejemplo)
(2, 2, CURRENT_TIMESTAMP), -- Pulp Fiction - Brad Pitt
(3, 3, CURRENT_TIMESTAMP), -- Jurassic Park - Tom Hanks (ejemplo ficticio)
(3, 4, CURRENT_TIMESTAMP); -- Jurassic Park - Scarlett Johansson (ejemplo ficticio)

-- Relacionar películas con géneros
INSERT INTO pelicula_generos (pelicula_id, genero_id, last_update) VALUES
(1, 1, CURRENT_TIMESTAMP), -- Inception - Acción
(1, 3, CURRENT_TIMESTAMP), -- Inception - Ciencia Ficción
(2, 1, CURRENT_TIMESTAMP), -- Pulp Fiction - Acción
(2, 2, CURRENT_TIMESTAMP), -- Pulp Fiction - Drama
(3, 3, CURRENT_TIMESTAMP); -- Jurassic Park - Ciencia Ficción

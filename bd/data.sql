-- Insertar directores
INSERT INTO directores (nombre) VALUES
('Christopher Nolan'),
('Steven Spielberg'),
('Quentin Tarantino');

-- Insertar actores
INSERT INTO actores (nombre) VALUES
('Leonardo DiCaprio'),
('Brad Pitt'),
('Tom Hanks'),
('Scarlett Johansson');

-- Insertar géneros
INSERT INTO generos (nombre) VALUES
('Acción'),
('Drama'),
('Ciencia Ficción'),
('Comedia');

-- Insertar películas
INSERT INTO peliculas (titulo, fecha_salida, precio, condicion, formato, sinopsis, imagen_ampliada) VALUES
('Inception', '2010-07-16', 2500.00, 'Nuevo', 'Blu-ray', 'Un ladrón que roba secretos a través de sueños es contratado para implantar una idea en la mente de un CEO.', 'inception.jpg'),
('Pulp Fiction', '1994-10-14', 1800.00, 'Usado', 'DVD', 'Historias entrelazadas de crimen y redención en Los Ángeles.', 'pulpfiction.jpg'),
('Jurassic Park', '1993-06-11', 2000.00, 'Nuevo', '4K UHD', 'Un parque temático con dinosaurios clonados se convierte en un caos cuando las criaturas escapan.', 'jurassicpark.jpg');

-- Relacionar películas con directores
INSERT INTO pelicula_directores (pelicula_id, director_id) VALUES
(1, 1), -- Inception - Nolan
(2, 3), -- Pulp Fiction - Tarantino
(3, 2); -- Jurassic Park - Spielberg

-- Relacionar películas con actores
INSERT INTO pelicula_actores (pelicula_id, actor_id) VALUES
(1, 1), -- Inception - DiCaprio
(2, 1), -- Pulp Fiction - DiCaprio (ejemplo)
(2, 2), -- Pulp Fiction - Brad Pitt
(3, 3), -- Jurassic Park - Tom Hanks (ejemplo ficticio)
(3, 4); -- Jurassic Park - Scarlett Johansson (ejemplo ficticio)

-- Relacionar películas con géneros
INSERT INTO pelicula_generos (pelicula_id, genero_id) VALUES
(1, 1), -- Inception - Acción
(1, 3), -- Inception - Ciencia Ficción
(2, 1), -- Pulp Fiction - Acción
(2, 2), -- Pulp Fiction - Drama
(3, 3); -- Jurassic Park - Ciencia Ficción

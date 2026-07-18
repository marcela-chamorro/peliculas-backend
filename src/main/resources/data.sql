-- Script de inserción de datos para el sistema de películas con imágenes reales de TMDB
-- Primero verificamos si los datos existen antes de insertar

-- Inserción de géneros (verificando existencia primero)
INSERT INTO generos (nombre, last_update)
SELECT 'Acción', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM generos WHERE nombre = 'Acción')
UNION ALL
SELECT 'Aventura', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM generos WHERE nombre = 'Aventura')
UNION ALL
SELECT 'Comedia', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM generos WHERE nombre = 'Comedia')
UNION ALL
SELECT 'Drama', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM generos WHERE nombre = 'Drama')
UNION ALL
SELECT 'Ciencia Ficción', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM generos WHERE nombre = 'Ciencia Ficción')
UNION ALL
SELECT 'Fantasía', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM generos WHERE nombre = 'Fantasía')
UNION ALL
SELECT 'Terror', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM generos WHERE nombre = 'Terror')
UNION ALL
SELECT 'Romance', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM generos WHERE nombre = 'Romance')
UNION ALL
SELECT 'Suspenso', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM generos WHERE nombre = 'Suspenso')
UNION ALL
SELECT 'Animación', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM generos WHERE nombre = 'Animación')
UNION ALL
SELECT 'Documental', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM generos WHERE nombre = 'Documental')
UNION ALL
SELECT 'Crimen', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM generos WHERE nombre = 'Crimen');

-- Inserción de directores (verificando existencia primero)
INSERT INTO directores (nombre, last_update)
SELECT 'Christopher Nolan', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM directores WHERE nombre = 'Christopher Nolan')
UNION ALL
SELECT 'Steven Spielberg', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM directores WHERE nombre = 'Steven Spielberg')
UNION ALL
SELECT 'Quentin Tarantino', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM directores WHERE nombre = 'Quentin Tarantino')
UNION ALL
SELECT 'James Cameron', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM directores WHERE nombre = 'James Cameron')
UNION ALL
SELECT 'Peter Jackson', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM directores WHERE nombre = 'Peter Jackson')
UNION ALL
SELECT 'Hayao Miyazaki', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM directores WHERE nombre = 'Hayao Miyazaki')
UNION ALL
SELECT 'Martin Scorsese', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM directores WHERE nombre = 'Martin Scorsese')
UNION ALL
SELECT 'Alfonso Cuarón', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM directores WHERE nombre = 'Alfonso Cuarón')
UNION ALL
SELECT 'Guillermo del Toro', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM directores WHERE nombre = 'Guillermo del Toro')
UNION ALL
SELECT 'Ridley Scott', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM directores WHERE nombre = 'Ridley Scott')
UNION ALL
SELECT 'Tim Burton', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM directores WHERE nombre = 'Tim Burton')
UNION ALL
SELECT 'David Fincher', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM directores WHERE nombre = 'David Fincher')
UNION ALL
SELECT 'Damien Chazelle', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM directores WHERE nombre = 'Damien Chazelle')
UNION ALL
SELECT 'Lana Wachowski', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM directores WHERE nombre = 'Lana Wachowski')
UNION ALL
SELECT 'Ryan Coogler', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM directores WHERE nombre = 'Ryan Coogler')
UNION ALL
SELECT 'Jonathan Demme', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM directores WHERE nombre = 'Jonathan Demme');

-- Inserción de actores (verificando existencia primero)
INSERT INTO actores (nombre, last_update)
SELECT 'Leonardo DiCaprio', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM actores WHERE nombre = 'Leonardo DiCaprio')
UNION ALL
SELECT 'Tom Hanks', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM actores WHERE nombre = 'Tom Hanks')
UNION ALL
SELECT 'Meryl Streep', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM actores WHERE nombre = 'Meryl Streep')
UNION ALL
SELECT 'Robert Downey Jr.', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM actores WHERE nombre = 'Robert Downey Jr.')
UNION ALL
SELECT 'Scarlett Johansson', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM actores WHERE nombre = 'Scarlett Johansson')
UNION ALL
SELECT 'Brad Pitt', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM actores WHERE nombre = 'Brad Pitt')
UNION ALL
SELECT 'Jennifer Lawrence', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM actores WHERE nombre = 'Jennifer Lawrence')
UNION ALL
SELECT 'Denzel Washington', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM actores WHERE nombre = 'Denzel Washington')
UNION ALL
SELECT 'Morgan Freeman', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM actores WHERE nombre = 'Morgan Freeman')
UNION ALL
SELECT 'Samuel L. Jackson', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM actores WHERE nombre = 'Samuel L. Jackson')
UNION ALL
SELECT 'Natalie Portman', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM actores WHERE nombre = 'Natalie Portman')
UNION ALL
SELECT 'Christian Bale', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM actores WHERE nombre = 'Christian Bale')
UNION ALL
SELECT 'Anne Hathaway', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM actores WHERE nombre = 'Anne Hathaway')
UNION ALL
SELECT 'Ryan Gosling', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM actores WHERE nombre = 'Ryan Gosling')
UNION ALL
SELECT 'Emma Stone', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM actores WHERE nombre = 'Emma Stone');

-- Inserción de películas (verificando existencia primero y convirtiendo fechas)
INSERT INTO peliculas (titulo, fecha_salida, precio, condicion, formato, sinopsis, imagen_ampliada, last_update)
SELECT
    'Inception',
    '2010-07-16'::DATE,
    15.99,
    'Nuevo',
    'Blu-ray',
    'Un ladrón que roba secretos corporativos a través del uso de la tecnología de sueños compartidos tiene la tarea inversa de plantar una idea en la mente de un CEO.',
    'https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM peliculas WHERE titulo = 'Inception')
UNION ALL
SELECT
    'The Dark Knight',
    '2008-07-18'::DATE,
    12.50,
    'Usado',
    'DVD',
    'Batman se enfrenta al Joker, un criminal que siembra el caos en Gotham City.',
    'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM peliculas WHERE titulo = 'The Dark Knight')
UNION ALL
SELECT
    'Pulp Fiction',
    '1994-10-14'::DATE,
    9.99,
    'Usado',
    'DVD',
    'Las vidas de dos matones, un boxeador y una pareja de bandidos se entrelazan en cuatro historias de violencia y redención.',
    'https://image.tmdb.org/t/p/w500/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM peliculas WHERE titulo = 'Pulp Fiction')
UNION ALL
SELECT
    'Avatar',
    '2009-12-18'::DATE,
    18.75,
    'Nuevo',
    'Blu-ray',
    'Un marine parapléjico es enviado a la luna Pandora en una misión única, pero se debate entre seguir órdenes y proteger el mundo que considera su hogar.',
    'https://image.tmdb.org/t/p/w500/kyeqWdyUXW608qlYkRqosgbbJyK.jpg',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM peliculas WHERE titulo = 'Avatar')
UNION ALL
SELECT
    'Spirited Away',
    '2001-07-20'::DATE,
    14.25,
    'Nuevo',
    'Blu-ray',
    'Durante su mudanza, una niña de 10 años se aventura en un mundo de dioses, brujas y espíritus, donde los humanos se convierten en bestias.',
    'https://image.tmdb.org/t/p/w500/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM peliculas WHERE titulo = 'Spirited Away')
UNION ALL
SELECT
    'The Godfather',
    '1972-03-24'::DATE,
    8.99,
    'Usado',
    'DVD',
    'El patriarca envejecido de una dinastía del crimen organizado transfiere el control de su imperio clandestino a su hijo reacio.',
    'https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM peliculas WHERE titulo = 'The Godfather')
UNION ALL
SELECT
    'Forrest Gump',
    '1994-07-06'::DATE,
    11.25,
    'Usado',
    'DVD',
    'Las presidencias de Kennedy y Johnson, la guerra de Vietnam y otros eventos históricos se desarrollan desde la perspectiva de un hombre de Alabama.',
    'https://image.tmdb.org/t/p/w500/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM peliculas WHERE titulo = 'Forrest Gump')
UNION ALL
SELECT
    'Interstellar',
    '2014-11-07'::DATE,
    16.50,
    'Nuevo',
    'Blu-ray',
    'Un equipo de exploradores viaja a través de un agujero de gusano en el espacio en un intento por asegurar la supervivencia de la humanidad.',
    'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM peliculas WHERE titulo = 'Interstellar')
UNION ALL
SELECT
    'The Shawshank Redemption',
    '1994-10-14'::DATE,
    7.99,
    'Usado',
    'DVD',
    'Dos hombres encarcelados se vinculan a lo largo de los años, encontrando consuelo y eventual redención a través de actos de decencia común.',
    'https://image.tmdb.org/t/p/w500/hBcY0fEyRebn97iyjtVyYWo6QnO.jpg',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM peliculas WHERE titulo = 'The Shawshank Redemption')
UNION ALL
SELECT
    'La La Land',
    '2016-12-09'::DATE,
    13.75,
    'Nuevo',
    'Blu-ray',
    'Mientras buscan fama en la ciudad de Los Ángeles, un pianista de jazz y una actriz se enamoran mientras luchan por reconciliar sus aspiraciones artísticas.',
    'https://image.tmdb.org/t/p/w500/uDO8zWDhfWwoFdKS4fzkUJt0Rf0.jpg',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM peliculas WHERE titulo = 'La La Land')
UNION ALL
SELECT
    'The Matrix',
    '1999-03-31'::DATE,
    10.99,
    'Usado',
    'DVD',
    'Un hacker se entera de la verdad sobre su realidad y su papel en la guerra contra sus controladores.',
    'https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM peliculas WHERE titulo = 'The Matrix')
UNION ALL
SELECT
    'Titanic',
    '1997-12-19'::DATE,
    9.50,
    'Usado',
    'DVD',
    'Una aristócrata de diecisiete años se enamora de un artista amable pero pobre a bordo del lujoso y desafortunado R.M.S. Titanic.',
    'https://image.tmdb.org/t/p/w500/9xjZS2rlVxm8SFx8kPC3aIGCOYQ.jpg',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM peliculas WHERE titulo = 'Titanic')
UNION ALL
SELECT
    'Jurassic Park',
    '1993-06-11'::DATE,
    12.25,
    'Usado',
    'DVD',
    'Durante una visita previa a un parque temático, un empresario multimillonario, un pequeño grupo es atacado por dinosaurios clonados.',
    'https://image.tmdb.org/t/p/w500/oU7Oq2kFAAlGqbU4VoAE36g4hoI.jpg',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM peliculas WHERE titulo = 'Jurassic Park')
UNION ALL
SELECT
    'Black Panther',
    '2018-02-16'::DATE,
    17.25,
    'Nuevo',
    'Blu-ray',
    'T Challa, heredero del reino oculto de Wakanda, debe dar un paso adelante para liderar a su pueblo en un nuevo camino.',
    'https://image.tmdb.org/t/p/w500/uxzzxijgPIY7slzFvMotPv8wjKA.jpg',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM peliculas WHERE titulo = 'Black Panther')
UNION ALL
SELECT
    'The Silence of the Lambs',
    '1991-02-14'::DATE,
    8.25,
    'Usado',
    'DVD',
    'Una joven agente del FBI debe recibir la ayuda de un asesino en serie encarcelado y manipulador para ayudar a atrapar a otro asesino en serie.',
    'https://image.tmdb.org/t/p/w500/uS9m8OBk1A8eM9I042bx8XXpqAq.jpg',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM peliculas WHERE titulo = 'The Silence of the Lambs');

-- Ahora insertamos las relaciones usando subconsultas para obtener los IDs correctos
-- Relaciones Película - Directores
INSERT INTO pelicula_directores (pelicula_id, director_id)
SELECT p.pelicula_id, d.director_id
FROM peliculas p
         CROSS JOIN directores d
WHERE ((p.titulo = 'Inception' AND d.nombre = 'Christopher Nolan')
   OR (p.titulo = 'The Dark Knight' AND d.nombre = 'Christopher Nolan')
   OR (p.titulo = 'Pulp Fiction' AND d.nombre = 'Quentin Tarantino')
   OR (p.titulo = 'Avatar' AND d.nombre = 'James Cameron')
   OR (p.titulo = 'Spirited Away' AND d.nombre = 'Hayao Miyazaki')
   OR (p.titulo = 'The Godfather' AND d.nombre = 'Martin Scorsese')
   OR (p.titulo = 'Forrest Gump' AND d.nombre = 'Steven Spielberg')
   OR (p.titulo = 'Interstellar' AND d.nombre = 'Christopher Nolan')
   OR (p.titulo = 'The Shawshank Redemption' AND d.nombre = 'Steven Spielberg')
   OR (p.titulo = 'La La Land' AND d.nombre = 'Damien Chazelle')
   OR (p.titulo = 'The Matrix' AND d.nombre = 'Lana Wachowski')
   OR (p.titulo = 'Titanic' AND d.nombre = 'James Cameron')
   OR (p.titulo = 'Jurassic Park' AND d.nombre = 'Steven Spielberg')
   OR (p.titulo = 'Black Panther' AND d.nombre = 'Ryan Coogler')
   OR (p.titulo = 'The Silence of the Lambs' AND d.nombre = 'Jonathan Demme'))
    AND NOT EXISTS (
        SELECT 1 FROM pelicula_directores pd
        WHERE pd.pelicula_id = p.pelicula_id AND pd.director_id = d.director_id
    );

-- Relaciones Película - Actores
INSERT INTO pelicula_actores (pelicula_id, actor_id)
SELECT p.pelicula_id, a.actor_id
FROM peliculas p
         CROSS JOIN actores a
WHERE ((p.titulo = 'Inception' AND a.nombre IN ('Leonardo DiCaprio', 'Robert Downey Jr.', 'Samuel L. Jackson'))
   OR (p.titulo = 'The Dark Knight' AND a.nombre IN ('Christian Bale', 'Samuel L. Jackson', 'Anne Hathaway'))
   OR (p.titulo = 'Pulp Fiction' AND a.nombre IN ('Brad Pitt', 'Samuel L. Jackson', 'Natalie Portman'))
   OR (p.titulo = 'Avatar' AND a.nombre IN ('Leonardo DiCaprio', 'Scarlett Johansson', 'Denzel Washington'))
   OR (p.titulo = 'Spirited Away' AND a.nombre IN ('Natalie Portman', 'Anne Hathaway'))
   OR (p.titulo = 'The Godfather' AND a.nombre IN ('Leonardo DiCaprio', 'Morgan Freeman', 'Samuel L. Jackson'))
   OR (p.titulo = 'Forrest Gump' AND a.nombre IN ('Tom Hanks', 'Meryl Streep', 'Brad Pitt'))
   OR (p.titulo = 'Interstellar' AND a.nombre IN ('Christian Bale', 'Anne Hathaway', 'Leonardo DiCaprio'))
   OR (p.titulo = 'The Shawshank Redemption' AND a.nombre IN ('Tom Hanks', 'Morgan Freeman', 'Brad Pitt'))
   OR (p.titulo = 'La La Land' AND a.nombre IN ('Ryan Gosling', 'Emma Stone', 'Meryl Streep'))
   OR (p.titulo = 'The Matrix' AND a.nombre IN ('Christian Bale', 'Samuel L. Jackson', 'Scarlett Johansson'))
   OR (p.titulo = 'Titanic' AND a.nombre IN ('Leonardo DiCaprio', 'Scarlett Johansson', 'Jennifer Lawrence'))
   OR (p.titulo = 'Jurassic Park' AND a.nombre IN ('Tom Hanks', 'Meryl Streep', 'Morgan Freeman'))
   OR (p.titulo = 'Black Panther' AND a.nombre IN ('Denzel Washington', 'Samuel L. Jackson', 'Natalie Portman'))
   OR (p.titulo = 'The Silence of the Lambs' AND a.nombre IN ('Meryl Streep', 'Brad Pitt', 'Morgan Freeman')))
    AND NOT EXISTS (
        SELECT 1 FROM pelicula_actores pa
        WHERE pa.pelicula_id = p.pelicula_id AND pa.actor_id = a.actor_id
    );

-- Relaciones Película - Géneros
INSERT INTO pelicula_generos (pelicula_id, genero_id)
SELECT p.pelicula_id, g.genero_id
FROM peliculas p
         CROSS JOIN generos g
WHERE ((p.titulo = 'Inception' AND g.nombre IN ('Ciencia Ficción', 'Drama', 'Suspenso'))
   OR (p.titulo = 'The Dark Knight' AND g.nombre IN ('Acción', 'Drama', 'Suspenso'))
   OR (p.titulo = 'Pulp Fiction' AND g.nombre IN ('Comedia', 'Drama', 'Crimen'))
   OR (p.titulo = 'Avatar' AND g.nombre IN ('Acción', 'Aventura', 'Ciencia Ficción'))
   OR (p.titulo = 'Spirited Away' AND g.nombre IN ('Fantasía', 'Aventura', 'Animación'))
   OR (p.titulo = 'The Godfather' AND g.nombre IN ('Drama', 'Crimen', 'Suspenso'))
   OR (p.titulo = 'Forrest Gump' AND g.nombre IN ('Drama', 'Comedia', 'Romance'))
   OR (p.titulo = 'Interstellar' AND g.nombre IN ('Ciencia Ficción', 'Aventura', 'Drama'))
   OR (p.titulo = 'The Shawshank Redemption' AND g.nombre IN ('Drama', 'Suspenso'))
   OR (p.titulo = 'La La Land' AND g.nombre IN ('Romance', 'Drama', 'Comedia'))
   OR (p.titulo = 'The Matrix' AND g.nombre IN ('Acción', 'Ciencia Ficción', 'Suspenso'))
   OR (p.titulo = 'Titanic' AND g.nombre IN ('Romance', 'Drama', 'Aventura'))
   OR (p.titulo = 'Jurassic Park' AND g.nombre IN ('Acción', 'Aventura', 'Ciencia Ficción'))
   OR (p.titulo = 'Black Panther' AND g.nombre IN ('Acción', 'Aventura', 'Ciencia Ficción'))
   OR (p.titulo = 'The Silence of the Lambs' AND g.nombre IN ('Suspenso', 'Crimen', 'Drama')))
    AND NOT EXISTS (
        SELECT 1 FROM pelicula_generos pg
        WHERE pg.pelicula_id = p.pelicula_id AND pg.genero_id = g.genero_id
    );

-- Verificación de datos insertados
SELECT 'Géneros en BD: ' || COUNT(*) FROM generos
UNION ALL
SELECT 'Directores en BD: ' || COUNT(*) FROM directores
UNION ALL
SELECT 'Actores en BD: ' || COUNT(*) FROM actores
UNION ALL
SELECT 'Películas en BD: ' || COUNT(*) FROM peliculas
UNION ALL
SELECT 'Relaciones película-director: ' || COUNT(*) FROM pelicula_directores
UNION ALL
SELECT 'Relaciones película-actor: ' || COUNT(*) FROM pelicula_actores
UNION ALL
SELECT 'Relaciones película-género: ' || COUNT(*) FROM pelicula_generos;

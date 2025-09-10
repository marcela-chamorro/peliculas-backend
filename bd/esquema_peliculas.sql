-- Función común de trigger
CREATE FUNCTION set_last_update_func() RETURNS TRIGGER 
AS $$  
BEGIN
  NEW.last_update = CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Tabla directores
CREATE TABLE directores (
  director_id SMALLINT CHECK (director_id > 0) NOT NULL GENERATED ALWAYS AS IDENTITY,
  nombre VARCHAR(255) NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (director_id)
);
CREATE TRIGGER director_before_update BEFORE UPDATE ON directores 
FOR EACH ROW EXECUTE FUNCTION set_last_update_func();

-- Tabla actores
CREATE TABLE actores (
  actor_id SMALLINT CHECK (actor_id > 0) NOT NULL GENERATED ALWAYS AS IDENTITY,
  nombre VARCHAR(255) NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (actor_id)
);
CREATE TRIGGER actor_before_update BEFORE UPDATE ON actores 
FOR EACH ROW EXECUTE FUNCTION set_last_update_func();

-- Tabla géneros
CREATE TABLE generos (
  genero_id SMALLINT CHECK (genero_id > 0) NOT NULL GENERATED ALWAYS AS IDENTITY,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (genero_id)
);
CREATE TRIGGER genero_before_update BEFORE UPDATE ON generos 
FOR EACH ROW EXECUTE FUNCTION set_last_update_func();

-- Tabla películas
CREATE TABLE peliculas (
  pelicula_id INT CHECK (pelicula_id > 0) NOT NULL GENERATED ALWAYS AS IDENTITY,
  titulo VARCHAR(255) NOT NULL,
  fecha_salida DATE NOT NULL,
  precio NUMERIC(10, 2) NOT NULL,
  condicion VARCHAR(50) NOT NULL,
  formato VARCHAR(50) NOT NULL,
  sinopsis TEXT,
  imagen_ampliada VARCHAR(255),
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (pelicula_id)
);
CREATE TRIGGER pelicula_before_update BEFORE UPDATE ON peliculas 
FOR EACH ROW EXECUTE FUNCTION set_last_update_func();

-- Relación Película - Directores
CREATE TABLE pelicula_directores (
  pelicula_id INT NOT NULL,
  director_id SMALLINT NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (pelicula_id, director_id),
  CONSTRAINT fk_pel_dir_pelicula FOREIGN KEY (pelicula_id) REFERENCES peliculas(pelicula_id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_pel_dir_director FOREIGN KEY (director_id) REFERENCES directores(director_id) ON DELETE RESTRICT ON UPDATE CASCADE
);
CREATE TRIGGER pelicula_directores_before_update BEFORE UPDATE ON pelicula_directores 
FOR EACH ROW EXECUTE FUNCTION set_last_update_func();
CREATE INDEX idx_pel_dir_pelicula ON pelicula_directores(pelicula_id);
CREATE INDEX idx_pel_dir_director ON pelicula_directores(director_id);

-- Relación Película - Actores
CREATE TABLE pelicula_actores (
  pelicula_id INT NOT NULL,
  actor_id SMALLINT NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (pelicula_id, actor_id),
  CONSTRAINT fk_pel_act_pelicula FOREIGN KEY (pelicula_id) REFERENCES peliculas(pelicula_id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_pel_act_actor FOREIGN KEY (actor_id) REFERENCES actores(actor_id) ON DELETE RESTRICT ON UPDATE CASCADE
);
CREATE TRIGGER pelicula_actores_before_update BEFORE UPDATE ON pelicula_actores 
FOR EACH ROW EXECUTE FUNCTION set_last_update_func();
CREATE INDEX idx_pel_act_pelicula ON pelicula_actores(pelicula_id);
CREATE INDEX idx_pel_act_actor ON pelicula_actores(actor_id);

-- Relación Película - Géneros
CREATE TABLE pelicula_generos (
  pelicula_id INT NOT NULL,
  genero_id SMALLINT NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (pelicula_id, genero_id),
  CONSTRAINT fk_pel_gen_pelicula FOREIGN KEY (pelicula_id) REFERENCES peliculas(pelicula_id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_pel_gen_genero FOREIGN KEY (genero_id) REFERENCES generos(genero_id) ON DELETE RESTRICT ON UPDATE CASCADE
);
CREATE TRIGGER pelicula_generos_before_update BEFORE UPDATE ON pelicula_generos 
FOR EACH ROW EXECUTE FUNCTION set_last_update_func();
CREATE INDEX idx_pel_gen_pelicula ON pelicula_generos(pelicula_id);
CREATE INDEX idx_pel_gen_genero ON pelicula_generos(genero_id);



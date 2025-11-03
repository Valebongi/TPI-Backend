-- Script SQL para crear la base de datos del ServicioLogistica
-- Ejecutar este script con privilegios de administrador en PostgreSQL

-- Crear la base de datos si no existe
CREATE DATABASE tpi_backend_logistics_db
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Spanish_Argentina.1252'
    LC_CTYPE = 'Spanish_Argentina.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Comentario sobre la base de datos
COMMENT ON DATABASE tpi_backend_logistics_db 
    IS 'Base de datos para el microservicio de logística del TPI Backend';

-- Conectar a la nueva base de datos
\c tpi_backend_logistics_db

-- Las tablas se crearán automáticamente por Hibernate con ddl-auto=update
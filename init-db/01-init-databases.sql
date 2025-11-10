-- Script de inicialización para PostgreSQL
-- Se ejecuta automáticamente cuando se crea el contenedor de PostgreSQL

-- Crear las bases de datos para cada microservicio
CREATE DATABASE tpi_backend_administracion_db;
CREATE DATABASE tpi_backend_logistica_db;
CREATE DATABASE tpi_backend_pedidos_db;

-- Crear base de datos para Keycloak
CREATE DATABASE keycloak_db;

-- Confirmar que las bases de datos se crearon
\l
-- Script para crear múltiples bases de datos en PostgreSQL
-- Este script inicializa las tres bases de datos separadas para cada microservicio

-- Crear base de datos para el servicio de Administración
CREATE DATABASE tpi_backend_administracion_db;
GRANT ALL PRIVILEGES ON DATABASE tpi_backend_administracion_db TO tpi_user;

-- Crear base de datos para el servicio de Logística
CREATE DATABASE tpi_backend_logistica_db;
GRANT ALL PRIVILEGES ON DATABASE tpi_backend_logistica_db TO tpi_user;

-- Crear base de datos para el servicio de Pedidos
CREATE DATABASE tpi_backend_pedidos_db;
GRANT ALL PRIVILEGES ON DATABASE tpi_backend_pedidos_db TO tpi_user;

-- Verificar que las bases fueron creadas
SELECT datname FROM pg_database WHERE datname LIKE 'tpi_backend%';
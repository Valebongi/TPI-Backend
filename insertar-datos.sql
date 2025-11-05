-- Script para insertar datos después de que Spring Boot cree las tablas
-- Ejecutar después de que las aplicaciones arranquen

-- DATOS PARA SERVICIO ADMINISTRACIÓN (tpi_backend_administracion_db)
\c tpi_backend_administracion_db;

-- Datos para CAMIONES (ServicioAdministracion)
INSERT INTO CAMIONES (PATENTE, ANIO_MODELO, PESO_TARA, PESO_MAXIMO_PERMITIDO, VOLUMEN_MAXIMO_PERMITIDO, ACTIVO) VALUES
('ABC123', 2020, 4500.0, 24000.0, 45.0, true),
('DEF456', 2019, 4200.0, 22000.0, 42.0, true),
('GHI789', 2021, 4800.0, 26000.0, 48.0, true),
('JKL012', 2018, 4300.0, 23000.0, 44.0, true),
('MNO345', 2022, 4600.0, 25000.0, 46.0, true),
('PQR678', 2020, 4400.0, 24500.0, 45.5, true);

-- Datos para DEPOSITOS (ServicioAdministracion)
INSERT INTO DEPOSITOS (NOMBRE, DIRECCION, ACTIVO) VALUES
('Depósito Central Buenos Aires', 'Av. Corrientes 1234, CABA', true),
('Depósito Norte Tigre', 'Ruta Panamericana Km 28, Tigre', true),
('Depósito Oeste Morón', 'Av. San Martin 567, Morón', true),
('Depósito Sur La Plata', 'Calle 7 890, La Plata', true),
('Depósito Este Quilmes', 'Av. Calchaquí 345, Quilmes', true);

-- Datos para PARAMETROS_GLOBALES (ServicioAdministracion)
INSERT INTO PARAMETROS_GLOBALES (CLAVE, VALOR, DESCRIPCION, ACTIVO) VALUES
('VALOR_LITRO', 150.00, 'Costo por litro de combustible', true),
('CARGO_GESTION_TRAMO', 500.00, 'Cargo fijo de gestión por tramo', true),
('COSTO_ESTADIA_BASE', 1200.00, 'Costo base de estadía en depósito por día', true),
('FACTOR_PESO_MAXIMO', 2.0, 'Factor multiplicador para peso máximo', true),
('FACTOR_VOLUMEN_MAXIMO', 1.8, 'Factor multiplicador para volumen máximo', true);

-- DATOS PARA SERVICIO LOGÍSTICA (tpi_backend_logistica_db)
\c tpi_backend_logistica_db;

-- Datos para RUTAS (ServicioLogistica)
INSERT INTO RUTAS (ORIGEN, DESTINO, ESTADO_RUTA) VALUES
('Depósito Central Buenos Aires', 'Depósito Norte Tigre', 'ACTIVA'),
('Depósito Norte Tigre', 'Depósito Oeste Morón', 'ACTIVA'),
('Depósito Oeste Morón', 'Depósito Sur La Plata', 'ACTIVA'),
('Depósito Sur La Plata', 'Depósito Este Quilmes', 'ACTIVA'),
('Depósito Este Quilmes', 'Depósito Central Buenos Aires', 'ACTIVA');

-- Datos para TRAMOS (ServicioLogistica)
INSERT INTO TRAMOS (RUTA_ID, LATITUD_ORIGEN, LONGITUD_ORIGEN, LATITUD_DESTINO, LONGITUD_DESTINO, DISTANCIA_KM, TIEMPO_ESTIMADO_HORAS, ESTADO_TRAMO) VALUES
(1, -34.6037, -58.3816, -34.4264, -58.5739, 35.2, 0.8, 'ACTIVO'),
(1, -34.4264, -58.5739, -34.4500, -58.5200, 8.5, 0.3, 'ACTIVO'),
(2, -34.4264, -58.5739, -34.6503, -58.5606, 28.7, 0.7, 'ACTIVO'),
(2, -34.6503, -58.5606, -34.6600, -58.5500, 5.2, 0.2, 'ACTIVO'),
(3, -34.6503, -58.5606, -34.9214, -57.9544, 42.1, 1.0, 'ACTIVO'),
(3, -34.9214, -57.9544, -34.9300, -57.9400, 3.8, 0.1, 'ACTIVO'),
(4, -34.9214, -57.9544, -34.7208, -58.2682, 31.6, 0.8, 'ACTIVO'),
(4, -34.7208, -58.2682, -34.7300, -58.2600, 4.1, 0.2, 'ACTIVO'),
(5, -34.7208, -58.2682, -34.6037, -58.3816, 18.9, 0.5, 'ACTIVO'),
(5, -34.6037, -58.3816, -34.6100, -58.3700, 2.7, 0.1, 'ACTIVO');

-- DATOS PARA SERVICIO PEDIDOS (tpi_backend_pedidos_db)
\c tpi_backend_pedidos_db;

-- Datos para CLIENTES (ServicioPedidos)
INSERT INTO CLIENTES (NOMBRE, DOMICILIO, TELEFONO, EMAIL, ACTIVO) VALUES
('Logística Argentina S.A.', 'Av. 9 de Julio 1500, CABA', '011-4123-4567', 'contacto@logistica-arg.com.ar', true),
('Transportes del Norte S.R.L.', 'Ruta Nacional 9 Km 45, San Isidro', '011-4789-0123', 'ventas@transportes-norte.com.ar', true),
('Distribuidora Central Ltda.', 'Av. Rivadavia 8900, CABA', '011-4456-7890', 'pedidos@distribuidora-central.com', true),
('Comercial Sur Unidos', 'Camino Centenario 1200, Berazategui', '011-4234-5678', 'admin@comercial-sur.com.ar', true),
('Empresa Oeste Cargo', 'Av. San Martín 3400, Morón', '011-4567-8901', 'info@oeste-cargo.com.ar', true),
('Servicios Este Express', 'Av. Calchaquí 2100, Quilmes', '011-4890-1234', 'operaciones@este-express.com', true);

-- Datos para CONTENEDORES (ServicioPedidos)
INSERT INTO CONTENEDORES (NUMERO_CONTENEDOR, TIPO, CAPACIDAD_VOLUMEN, CAPACIDAD_PESO, ACTIVO) VALUES
('CONT001ARG', 'STANDARD', 33.2, 28000.0, true),
('CONT002ARG', 'REFRIGERADO', 29.5, 26000.0, true),
('CONT003ARG', 'STANDARD', 33.2, 28000.0, true),
('CONT004ARG', 'HIGH_CUBE', 76.3, 30000.0, true),
('CONT005ARG', 'STANDARD', 33.2, 28000.0, true),
('CONT006ARG', 'REFRIGERADO', 29.5, 26000.0, true);

-- Datos para SOLICITUDES (ServicioPedidos)
INSERT INTO SOLICITUDES (CLIENTE_ID, CONTENEDOR_ID, FECHA_SOLICITUD, ESTADO, PESO_CARGA, VOLUMEN_CARGA, DESCRIPCION_CARGA, DIRECCION_RETIRO, DIRECCION_ENTREGA) VALUES
(1, 1, '2024-11-01 09:00:00', 'PENDIENTE', 15000.0, 25.5, 'Productos alimenticios secos', 'Puerto Buenos Aires, Dock Sud', 'Av. Corrientes 1234, CABA'),
(2, 2, '2024-11-01 10:30:00', 'ASIGNADO', 18000.0, 28.0, 'Productos refrigerados', 'Mercado Central, La Matanza', 'Ruta Panamericana Km 28, Tigre'),
(3, 3, '2024-11-01 11:15:00', 'EN_TRANSITO', 22000.0, 30.1, 'Materiales de construcción', 'Zona Franca La Plata', 'Av. San Martin 567, Morón'),
(4, 4, '2024-11-01 14:20:00', 'PENDIENTE', 25000.0, 45.8, 'Maquinaria industrial', 'Polo Industrial Ezeiza', 'Calle 7 890, La Plata'),
(5, 5, '2024-11-01 15:45:00', 'ASIGNADO', 16500.0, 22.3, 'Productos textiles', 'Parque Industrial Pilar', 'Av. Calchaquí 345, Quilmes'),
(6, 6, '2024-11-01 16:30:00', 'PENDIENTE', 19800.0, 27.6, 'Productos farmacéuticos', 'Centro de Distribución Tigre', 'Av. 9 de Julio 1500, CABA');
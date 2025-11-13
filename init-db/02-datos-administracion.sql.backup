-- ============================================
-- DATOS DE EJEMPLO PARA SERVICIO ADMINISTRACION
-- Base de datos: tpi_backend_administracion_db
-- ============================================

-- Datos de ejemplo para PARAMETROS_GLOBALES
INSERT INTO PARAMETROS_GLOBALES (CLAVE, VALOR, DESCRIPCION, ACTIVO) VALUES
('VALOR_LITRO', 150.00, 'Costo por litro de combustible', true),
('CARGO_GESTION_TRAMO', 500.00, 'Cargo fijo de gestión por tramo', true),
('COSTO_ESTADIA_BASE', 1200.00, 'Costo base de estadía en depósito por día', true),
('FACTOR_PESO_MAXIMO', 2.0, 'Factor multiplicador para peso máximo', true),
('FACTOR_VOLUMEN_MAXIMO', 1.8, 'Factor multiplicador para volumen máximo', true);

-- Datos de ejemplo para DEPOSITOS
INSERT INTO DEPOSITOS (NOMBRE, DIRECCION, LATITUD, LONGITUD, COSTO_DIARIO, CAPACIDAD_MAX) VALUES
('Depósito Central Buenos Aires', 'Av. Warnes 1234, Ciudad Autónoma de Buenos Aires', -34.603450, -58.381729, 2500.00, 100),
('Depósito Norte Córdoba', 'Av. Colón 5678, Córdoba Capital', -31.416775, -64.183318, 1800.00, 75),
('Depósito Sur Rosario', 'Ruta Nacional 9 Km 15, Rosario', -32.944264, -60.650538, 2200.00, 90),
('Depósito Oeste Mendoza', 'Acceso Este 890, Mendoza Capital', -32.889459, -68.845839, 1600.00, 60),
('Depósito Este Mar del Plata', 'Ruta 2 Km 405, Mar del Plata', -38.002281, -57.556478, 1400.00, 45);

-- Datos de ejemplo para CAMIONES
INSERT INTO CAMIONES (DOMINIO, MARCA, MODELO, CAPACIDAD_PESO, CAPACIDAD_VOLUMEN, CONSUMO_KM, COSTO_KM_BASE, ESTADO, TRANSPORTISTA, TELEFONO, DISPONIBLE) VALUES
('AB123CD', 'Mercedes-Benz', 'Actros 2644', 24000.00, 80.00, 0.35, 120.00, 'DISPONIBLE', 'Transportes Rodriguez SRL', '+54-11-4567-8901', true),
('EF456GH', 'Volvo', 'FH 440', 18000.00, 65.00, 0.32, 110.00, 'DISPONIBLE', 'Logística del Sur SA', '+54-351-234-5678', true),
('IJ789KL', 'Scania', 'R 450', 20000.00, 70.00, 0.33, 115.00, 'DISPONIBLE', 'Transportes Córdoba', '+54-341-567-8901', true),
('MN012OP', 'Iveco', 'Stralis 460', 22000.00, 75.00, 0.36, 125.00, 'EN_RUTA', 'Mendoza Cargas Express', '+54-261-890-1234', false),
('QR345ST', 'Ford', 'Cargo 1722', 12000.00, 45.00, 0.28, 95.00, 'DISPONIBLE', 'Transporte Atlántico', '+54-223-345-6789', true),
('UV678WX', 'Mercedes-Benz', 'Atego 1726', 14000.00, 50.00, 0.30, 100.00, 'MANTENIMIENTO', 'Rodriguez Hermanos', '+54-11-2345-6789', false);

-- Crear secuencias si no existen
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = 'seq_deposito_id') THEN
        CREATE SEQUENCE SEQ_DEPOSITO_ID START WITH 100;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = 'seq_camion_id') THEN
        CREATE SEQUENCE SEQ_CAMION_ID START WITH 100;
    END IF;
END $$;

-- Actualizar secuencias para evitar conflictos con IDs existentes
SELECT setval('SEQ_DEPOSITO_ID', COALESCE((SELECT MAX(id_deposito) FROM DEPOSITOS), 0) + 1, false);
SELECT setval('SEQ_CAMION_ID', COALESCE((SELECT MAX(id_camion) FROM CAMIONES), 0) + 1, false);
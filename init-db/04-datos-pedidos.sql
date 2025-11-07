-- ============================================
-- DATOS DE EJEMPLO PARA SERVICIO PEDIDOS
-- Base de datos: tpi_backend_pedidos_db
-- ============================================

-- Datos de ejemplo para CLIENTES
INSERT INTO CLIENTES (NOMBRE, APELLIDO, EMAIL, TELEFONO, DIRECCION, KEYCLOAK_ID, ACTIVO) VALUES
('Juan Carlos', 'Rodríguez', 'juan.rodriguez@empresaabc.com.ar', '+54-11-4567-8901', 'Av. Corrientes 1234, CABA', 'kc_001', true),
('María Elena', 'González', 'maria.gonzalez@logisticasur.com', '+54-341-234-5678', 'San Martín 567, Rosario', 'kc_002', true),
('Roberto', 'Fernández', 'rfernandez@mendozacargas.com', '+54-261-890-1234', 'Las Heras 890, Mendoza', 'kc_003', true),
('Ana Lucía', 'Martínez', 'ana.martinez@comercialmdp.com.ar', '+54-223-345-6789', 'Independencia 345, Mar del Plata', 'kc_004', true),
('Carlos Alberto', 'Silva', 'csilva@transportescba.com.ar', '+54-351-567-8901', 'Av. Colón 1567, Córdoba', 'kc_005', true),
('Laura Beatriz', 'López', 'llopez@empresarios.org.ar', '+54-11-8901-2345', 'Puerto Madero 2345, CABA', 'kc_006', true);

-- Datos de ejemplo para CONTENEDORES
INSERT INTO CONTENEDORES (CODIGO, PESO, VOLUMEN, ESTADO, DESCRIPCION, ID_CLIENTE, ID_DEPOSITO) VALUES
('CNT-001', 15000.00, 45.5, 'EN_TRANSITO', 'Maquinaria industrial pesada', 1, 1),
('CNT-002', 8500.00, 28.2, 'EN_DEPOSITO', 'Productos alimenticios refrigerados', 2, 2),
('CNT-003', 22000.00, 65.8, 'ENTREGADO', 'Materiales de construcción', 3, 1),
('CNT-004', 12500.00, 35.4, 'PENDIENTE', 'Equipamiento médico', 4, 3),
('CNT-005', 18750.00, 52.3, 'EN_TRANSITO', 'Autopartes y repuestos', 5, 1),
('CNT-006', 9200.00, 31.7, 'PENDIENTE', 'Textiles y confecciones', 6, 2);

-- Datos de ejemplo para SOLICITUDES
INSERT INTO SOLICITUDES (NUMERO, ID_CONTENEDOR, ID_CLIENTE, COSTO_ESTIMADO, TIEMPO_ESTIMADO_HORAS, COSTO_FINAL, TIEMPO_REAL_HORAS, ESTADO, FECHA_CREACION, FECHA_PROGRAMACION, FECHA_INICIO_TRANSITO, FECHA_ENTREGA, OBSERVACIONES, RUTA_ID) VALUES
('SOL-1730671234001', 1, 1, 45500.00, 36, 47250.00, 38, 'EN_TRANSITO', '2025-11-01 08:30:00', '2025-11-02 06:00:00', '2025-11-02 06:30:00', null, 'Carga pesada - requiere grúa especial', 1),
('SOL-1730671234002', 2, 2, 28900.00, 24, null, null, 'PROGRAMADA', '2025-11-02 14:15:00', '2025-11-03 08:00:00', null, null, 'Mantener cadena de frío', 2),
('SOL-1730671234003', 3, 3, 67200.00, 48, 69150.00, 50, 'ENTREGADA', '2025-10-28 10:00:00', '2025-10-31 05:00:00', '2025-10-31 06:00:00', '2025-11-02 14:30:00', 'Material frágil - manejo cuidadoso', 3),
('SOL-1730671234004', 4, 4, 32400.00, 28, null, null, 'CONFIRMADA', '2025-11-02 16:45:00', '2025-11-04 07:00:00', null, null, 'Equipo médico delicado - seguro especial', 4),
('SOL-1730671234005', 5, 5, 41800.00, 32, null, null, 'EN_TRANSITO', '2025-11-01 12:00:00', '2025-11-02 20:00:00', '2025-11-02 20:30:00', null, 'Entrega urgente - cliente prioritario', 5),
('SOL-1730671234006', 6, 6, 35600.00, 40, null, null, 'BORRADOR', '2025-11-03 09:20:00', null, null, null, 'Solicitud en proceso de validación', null);

-- Crear secuencias si no existen
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = 'seq_cliente_id') THEN
        CREATE SEQUENCE SEQ_CLIENTE_ID START WITH 100;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = 'seq_contenedor_id') THEN
        CREATE SEQUENCE SEQ_CONTENEDOR_ID START WITH 100;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = 'seq_solicitud_id') THEN
        CREATE SEQUENCE SEQ_SOLICITUD_ID START WITH 100;
    END IF;
END $$;

-- Actualizar secuencias para evitar conflictos con IDs existentes
SELECT setval('SEQ_CLIENTE_ID', COALESCE((SELECT MAX(id_cliente) FROM CLIENTES), 0) + 1, false);
SELECT setval('SEQ_CONTENEDOR_ID', COALESCE((SELECT MAX(id_contenedor) FROM CONTENEDORES), 0) + 1, false);
SELECT setval('SEQ_SOLICITUD_ID', COALESCE((SELECT MAX(id_solicitud) FROM SOLICITUDES), 0) + 1, false);
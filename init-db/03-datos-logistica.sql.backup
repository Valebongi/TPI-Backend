-- ============================================
-- DATOS DE EJEMPLO PARA SERVICIO LOGISTICA  
-- Base de datos: tpi_backend_logistica_db
-- ============================================

-- Datos de ejemplo para RUTAS
INSERT INTO rutas (solicitud_id, cantidad_tramos, cantidad_depositos, estado, costo_total_aproximado, costo_total_real) VALUES
(1, 3, 2, 'PLANIFICADA', 45500.00, null),
(2, 2, 1, 'EN_PROGRESO', 28900.00, null),
(3, 4, 3, 'COMPLETADA', 67200.00, 69150.00),
(4, 2, 1, 'PLANIFICADA', 32400.00, null),
(5, 3, 2, 'EN_PROGRESO', 41800.00, null);

-- Datos de ejemplo para TRAMOS
INSERT INTO tramos (origen_coordenadas, destino_coordenadas, origen_descripcion, destino_descripcion, tipo, estado, distancia_km, costo_aproximado, costo_real, fecha_hora_inicio, fecha_hora_fin, camion_id, ruta_id) VALUES
-- Tramos para Ruta 1 (solicitud_id=1)
('-34.6037,-58.3816', '-31.4168,-64.1833', 'Buenos Aires - Puerto Madero', 'Córdoba - Depósito Norte', 'RECOGIDA', 'ASIGNADO', 704.5, 15200.00, null, null, null, 1, 1),
('-31.4168,-64.1833', '-32.9443,-60.6505', 'Córdoba - Depósito Norte', 'Rosario - Depósito Sur', 'TRANSPORTE', 'PENDIENTE', 412.3, 8900.00, null, null, null, null, 1),
('-32.9443,-60.6505', '-32.8895,-68.8458', 'Rosario - Depósito Sur', 'Mendoza - Cliente Final', 'ENTREGA', 'PENDIENTE', 625.8, 13500.00, null, null, null, null, 1),

-- Tramos para Ruta 2 (solicitud_id=2) 
('-34.6037,-58.3816', '-32.9443,-60.6505', 'Buenos Aires Centro', 'Rosario - Depósito Sur', 'RECOGIDA', 'EN_PROGRESO', 298.4, 12400.00, null, '2025-11-03 08:30:00', null, 2, 2),
('-32.9443,-60.6505', '-38.0023,-57.5565', 'Rosario - Depósito Sur', 'Mar del Plata - Cliente', 'ENTREGA', 'PENDIENTE', 456.2, 16500.00, null, null, null, null, 2),

-- Tramos para Ruta 3 (solicitud_id=3) - COMPLETADA
('-34.6037,-58.3816', '-31.4168,-64.1833', 'Buenos Aires - Retiro', 'Córdoba - Depósito Norte', 'RECOGIDA', 'COMPLETADO', 704.5, 15200.00, 15680.00, '2025-11-01 06:00:00', '2025-11-01 18:30:00', 1, 3),
('-31.4168,-64.1833', '-32.8895,-68.8458', 'Córdoba - Depósito Norte', 'Mendoza - Depósito Oeste', 'TRANSPORTE', 'COMPLETADO', 478.9, 18900.00, 19200.00, '2025-11-02 07:00:00', '2025-11-02 16:45:00', 3, 3),
('-32.8895,-68.8458', '-32.9443,-60.6505', 'Mendoza - Depósito Oeste', 'Rosario - Depósito Sur', 'TRANSPORTE', 'COMPLETADO', 625.8, 19800.00, 20100.00, '2025-11-02 20:00:00', '2025-11-03 14:20:00', 3, 3),
('-32.9443,-60.6505', '-38.0023,-57.5565', 'Rosario - Depósito Sur', 'Mar del Plata - Destino Final', 'ENTREGA', 'COMPLETADO', 456.2, 13300.00, 14170.00, '2025-11-03 16:00:00', '2025-11-04 01:30:00', 5, 3),

-- Tramos para Ruta 4 (solicitud_id=4)
('-32.9443,-60.6505', '-31.4168,-64.1833', 'Rosario - Puerto', 'Córdoba - Depósito Norte', 'RECOGIDA', 'PENDIENTE', 412.3, 14200.00, null, null, null, null, 4),
('-31.4168,-64.1833', '-34.6037,-58.3816', 'Córdoba - Depósito Norte', 'Buenos Aires - Microcentro', 'ENTREGA', 'PENDIENTE', 704.5, 18200.00, null, null, null, null, 4),

-- Tramos para Ruta 5 (solicitud_id=5)
('-38.0023,-57.5565', '-32.9443,-60.6505', 'Mar del Plata - Puerto', 'Rosario - Depósito Sur', 'RECOGIDA', 'ASIGNADO', 456.2, 16800.00, null, null, null, 2, 5),
('-32.9443,-60.6505', '-31.4168,-64.1833', 'Rosario - Depósito Sur', 'Córdoba - Depósito Norte', 'TRANSPORTE', 'PENDIENTE', 412.3, 12500.00, null, null, null, null, 5),
('-31.4168,-64.1833', '-34.6037,-58.3816', 'Córdoba - Depósito Norte', 'Buenos Aires - Villa Crespo', 'ENTREGA', 'PENDIENTE', 704.5, 12500.00, null, null, null, null, 5);
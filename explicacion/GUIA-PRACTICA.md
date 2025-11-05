# 🔧 Guía Práctica de Uso - Sistema TPI Backend

## 🚀 **Inicio Rápido**

### **1. Levantar el Sistema**
```bash
cd c:\Codigo\TPI-Backend\TPI
.\start.bat
```

### **2. Verificar que Todo Funciona**
```bash
# Verificar servicios activos
docker ps

# Probar API Gateway
curl http://localhost:8080

# Probar cada microservicio
curl http://localhost:8082/camiones      # Administración
curl http://localhost:8083/api/rutas     # Logística  
curl http://localhost:8084/clientes      # Pedidos
curl http://localhost:8081/actuator/health # Geolocalización
```

---

## 📋 **Ejemplos de Uso con Postman**

### **🚛 Servicio Administración - Camiones**

#### **Crear Camión:**
```
POST http://localhost:8082/camiones
Content-Type: application/json

{
  "dominio": "ABC123",
  "marca": "Mercedes-Benz",
  "modelo": "Actros 2651",
  "capacidadPeso": 26000.00,
  "capacidadVolumen": 82.50,
  "consumoKm": 8.5,
  "transportista": "Transportes del Norte SA",
  "telefono": "+54-11-4567-8901"
}
```

#### **Listar Todos los Camiones:**
```
GET http://localhost:8082/camiones
```

#### **Obtener Camión Específico:**
```
GET http://localhost:8082/camiones/1
```

#### **Eliminar Camión:**
```
DELETE http://localhost:8082/camiones/1
```

### **🏢 Servicio Administración - Depósitos**

#### **Crear Depósito:**
```
POST http://localhost:8082/depositos
Content-Type: application/json

{
  "nombre": "Depósito Central CABA",
  "direccion": "Av. Corrientes 1234, CABA",
  "latitud": -34.6037,
  "longitud": -58.3816,
  "capacidadMaxima": 10000.0,
  "activo": true
}
```

### **👥 Servicio Pedidos - Clientes**

#### **Crear Cliente:**
```
POST http://localhost:8084/clientes
Content-Type: application/json

{
  "nombre": "María",
  "apellido": "González",
  "email": "maria.gonzalez@empresa.com",
  "telefono": "+54-11-1234-5678",
  "direccion": "Av. Santa Fe 2000, CABA"
}
```

#### **Listar Clientes Activos:**
```
GET http://localhost:8084/clientes?soloActivos=true
```

#### **Buscar Cliente por Filtro:**
```
GET http://localhost:8084/clientes?filtro=María
```

### **📦 Servicio Pedidos - Contenedores**

#### **Crear Contenedor:**
```
POST http://localhost:8084/contenedores
Content-Type: application/json

{
  "numero": "CONT001234",
  "tipo": "ESTANDAR",
  "peso": 2500.75,
  "volumen": 33.2,
  "descripcionCarga": "Productos electrónicos para distribución",
  "valorDeclarado": 150000.00
}
```

### **📋 Servicio Pedidos - Solicitudes**

#### **Crear Solicitud:**
```
POST http://localhost:8084/solicitudes
Content-Type: application/json

{
  "clienteId": 1,
  "fechaRetiro": "2025-11-05T09:00:00",
  "fechaEntregaEstimada": "2025-11-07T17:00:00",
  "direccionOrigen": "Av. Córdoba 500, CABA",
  "direccionDestino": "Ruta 9 Km 45, La Plata",
  "observaciones": "Entrega urgente - Productos frágiles",
  "contenedores": [
    {
      "numero": "CONT001234",
      "tipo": "ESTANDAR",
      "peso": 2500.75,
      "volumen": 33.2
    }
  ]
}
```

#### **Listar Solicitudes por Cliente:**
```
GET http://localhost:8084/solicitudes?clienteId=1
```

#### **Filtrar por Estado:**
```
GET http://localhost:8084/solicitudes?estado=PENDIENTE
```

### **🗺️ Servicio Logística - Rutas**

#### **Crear Ruta:**
```
POST http://localhost:8083/api/rutas
Content-Type: application/json

{
  "solicitudId": 1,
  "cantidadTramos": 2,
  "cantidadDepositos": 1,
  "estado": "PLANIFICADA",
  "costoTotalAproximado": 8500.50
}
```

#### **Agregar Tramo a Ruta:**
```
POST http://localhost:8083/api/rutas/1/tramos
Content-Type: application/json

{
  "origen": "Depósito Central CABA",
  "destino": "Cliente La Plata",
  "distanciaKm": 65.5,
  "tiempoEstimadoHoras": 1.5,
  "costo": 4250.25,
  "orden": 1
}
```

#### **Listar Rutas Activas:**
```
GET http://localhost:8083/api/rutas?estado=EN_PROGRESO
```

#### **Actualizar Estado de Ruta:**
```
PUT http://localhost:8083/api/rutas/1/estado?estado=EN_PROGRESO
```

### **🌍 Servicio Geolocalización**

#### **Calcular Distancia:**
```
GET http://localhost:8081/api/geo/distancia?origen=Buenos Aires&destino=La Plata
```

#### **Geocodificar Dirección:**
```
GET http://localhost:8081/api/geo/geocode?direccion=Av. Corrientes 1234, CABA
```

---

## 🔗 **Uso a través del API Gateway**

Todos los endpoints también pueden ser accedidos a través del API Gateway agregando el prefijo correspondiente:

### **Rutas del Gateway:**
```
# Administración
http://localhost:8080/api/admin/camiones
http://localhost:8080/api/admin/depositos

# Logística  
http://localhost:8080/api/logistica/rutas
http://localhost:8080/api/logistica/rutas/1/tramos

# Pedidos
http://localhost:8080/api/pedidos/clientes
http://localhost:8080/api/pedidos/solicitudes

# Geolocalización
http://localhost:8080/api/geo/distancia?origen=X&destino=Y
```

---

## 🔍 **Consultas de Base de Datos**

### **Verificar Datos Directamente en PostgreSQL:**

```sql
-- Conectar a PostgreSQL
docker exec -it tpi-postgres psql -U tpi_user -d postgres

-- Ver todas las bases
SELECT datname FROM pg_database WHERE datname LIKE 'tpi_backend%';

-- Conectar a base específica
\c tpi_backend_administracion_db

-- Ver tablas
\dt

-- Consultar camiones
SELECT * FROM camiones;

-- Cambiar a otra base
\c tpi_backend_pedidos_db
SELECT * FROM clientes;

-- Cambiar a logística  
\c tpi_backend_logistica_db
SELECT * FROM rutas;
```

---

## 📊 **Flujo de Trabajo Típico**

### **Escenario: Crear un Pedido Completo**

#### **1. Crear Cliente:**
```json
POST /clientes
{
  "nombre": "Empresa XYZ",
  "email": "contacto@empresa.xyz",
  "telefono": "+54-11-5555-0000"
}
```

#### **2. Crear Solicitud:**
```json
POST /solicitudes  
{
  "clienteId": 1,
  "fechaRetiro": "2025-11-10T08:00:00",
  "direccionOrigen": "Warehouse Norte",
  "direccionDestino": "Centro Distribución Sur"
}
```

#### **3. Verificar Camiones Disponibles:**
```
GET /camiones?disponible=true
```

#### **4. Crear Ruta Asociada:**
```json
POST /api/rutas
{
  "solicitudId": 1,
  "cantidadTramos": 1,
  "estado": "PLANIFICADA"
}
```

#### **5. Calcular Distancia:**
```
GET /api/geo/distancia?origen=Warehouse Norte&destino=Centro Distribución Sur
```

#### **6. Iniciar Ruta:**
```
PUT /api/rutas/1/estado?estado=EN_PROGRESO
```

#### **7. Finalizar:**
```
PUT /api/rutas/1/estado?estado=COMPLETADA
```

---

## 🐛 **Debugging y Logs**

### **Ver Logs en Tiempo Real:**
```bash
# Todos los servicios
.\logs.bat

# Servicio específico
docker logs tpi-servicio-administracion -f
docker logs tpi-servicio-logistica -f
docker logs tpi-servicio-pedidos -f
docker logs tpi-api-gateway -f
```

### **Verificar Estado de Salud:**
```bash
# PostgreSQL
docker exec tpi-postgres pg_isready -U tpi_user

# Servicios Spring Boot
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health  
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
curl http://localhost:8084/actuator/health
```

---

## 🛠️ **Troubleshooting Común**

### **Problema: Servicio no responde**
```bash
# 1. Verificar que el contenedor esté corriendo
docker ps

# 2. Ver logs del servicio
docker logs [nombre-contenedor]

# 3. Reiniciar servicio específico
docker restart [nombre-contenedor]
```

### **Problema: Error de base de datos**
```bash
# 1. Verificar PostgreSQL
docker exec tpi-postgres pg_isready -U tpi_user

# 2. Verificar conexiones
docker logs tpi-postgres

# 3. Reiniciar base de datos
docker restart tpi-postgres
```

### **Problema: Puerto ocupado**
```bash
# Ver qué proceso usa el puerto
netstat -ano | findstr :8080

# Detener sistema y reiniciar
.\stop.bat
.\start.bat
```

---

## 🎯 **Testing y Validación**

### **Colección de Postman Recomendada:**

1. **Crear Workspace:** `TPI Backend Testing`
2. **Importar Environment:**
   ```json
   {
     "name": "TPI Local",
     "values": [
       {"key": "base_url", "value": "http://localhost"},
       {"key": "gateway_port", "value": "8080"},
       {"key": "admin_port", "value": "8082"},
       {"key": "logistics_port", "value": "8083"},
       {"key": "orders_port", "value": "8084"},
       {"key": "geo_port", "value": "8081"}
     ]
   }
   ```

3. **Crear Colecciones por Servicio:**
   - `Administración Tests`
   - `Logística Tests`  
   - `Pedidos Tests`
   - `Geolocalización Tests`
   - `Integration Tests`

---

**📅 Última actualización:** Noviembre 2025  
**🔧 Guía creada por:** Equipo de Desarrollo TPI
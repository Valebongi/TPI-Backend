# 📋 Documentación Completa - Sistema TPI Backend

## 🏗️ **Arquitectura General del Sistema**

El sistema está diseñado como una **arquitectura de microservicios** utilizando **Spring Boot** y **Docker**, con una base de datos **PostgreSQL** compartida pero con esquemas separados para mantener el aislamiento de datos.

```
┌─────────────────────────────────────────────────────────────┐
│                    CLIENTE (Frontend)                      │
└─────────────────────┬───────────────────────────────────────┘
                      │ HTTP Requests
                      ▼
┌─────────────────────────────────────────────────────────────┐
│                 API GATEWAY (Puerto 8080)                  │
│              Gateway Central de Enrutamiento               │
└─┬─────────┬─────────────┬─────────────┬───────────────────┘
  │         │             │             │
  ▼         ▼             ▼             ▼
┌───────┐ ┌─────────┐ ┌─────────────┐ ┌─────────────┐
│ GEO   │ │ ADMIN   │ │  LOGÍSTICA  │ │   PEDIDOS   │
│ 8081  │ │  8082   │ │    8083     │ │    8084     │
└───────┘ └─────────┘ └─────────────┘ └─────────────┘
              │             │             │
              └─────────────┼─────────────┘
                            ▼
                    ┌─────────────────┐
                    │   PostgreSQL    │
                    │   (Puerto 5432) │
                    │                 │
                    │ 3 Bases Separadas│
                    │ ✓ administracion │
                    │ ✓ logistica     │
                    │ ✓ pedidos       │
                    └─────────────────┘
```

---

## 🚀 **Microservicios Detallados**

### 📡 **1. API Gateway (Puerto 8080)**
**Directorio:** `ApiGateway/`

#### **🎯 Propósito:**
- **Punto de entrada único** para todas las peticiones
- **Enrutamiento** de requests a microservicios específicos
- **Balanceador de carga** y gestión de tráfico
- **Centralización** de políticas de seguridad

#### **📂 Estructura Interna:**
```
ApiGateway/
├── src/main/java/utnfc/isi/back/
│   ├── AppMain.java                    # Punto de entrada principal
│   ├── config/
│   │   └── GatewayConfig.java         # Configuración de rutas
│   └── controller/
│       └── GatewayController.java     # Controlador principal
├── src/main/resources/
│   ├── application.properties         # Configuración local
│   └── application-docker.properties  # Configuración Docker
├── pom.xml                           # Dependencias Maven
└── Dockerfile                        # Imagen Docker
```

#### **🔗 Rutas de Enrutamiento:**
```
http://localhost:8080/api/admin/*      → Servicio Administración (8082)
http://localhost:8080/api/logistica/*  → Servicio Logística (8083)
http://localhost:8080/api/pedidos/*    → Servicio Pedidos (8084)
http://localhost:8080/api/geo/*        → Servicio Geolocalización (8081)
```

---

### 🏢 **2. Servicio Administración (Puerto 8082)**
**Directorio:** `ServicioAdministracion/`

#### **🎯 Propósito:**
- **Gestión de camiones** y flota de transporte
- **Administración de depósitos** y ubicaciones
- **Configuración de tarifas** y precios
- **Parámetros globales** del sistema

#### **📂 Estructura Interna:**
```
ServicioAdministracion/
├── src/main/java/utnfc/isi/back/sim/
│   ├── AppMain.java                    # Aplicación principal
│   ├── controller/                     # Controladores REST
│   │   ├── CamionController.java       # CRUD camiones
│   │   ├── DepositoController.java     # CRUD depósitos
│   │   ├── TarifaController.java       # CRUD tarifas
│   │   └── ParametroController.java    # CRUD parámetros
│   ├── domain/                         # Entidades JPA
│   │   ├── Camion.java                 # Entidad camión
│   │   ├── Deposito.java               # Entidad depósito
│   │   ├── Tarifa.java                 # Entidad tarifa
│   │   └── ParametroGlobal.java        # Entidad parámetro
│   ├── repository/                     # Repositorios JPA
│   │   ├── CamionRepository.java       # Acceso a datos camiones
│   │   ├── DepositoRepository.java     # Acceso a datos depósitos
│   │   ├── TarifaRepository.java       # Acceso a datos tarifas
│   │   └── ParametroRepository.java    # Acceso a datos parámetros
│   └── service/                        # Lógica de negocio
│       ├── CamionService.java          # Servicios camiones
│       ├── DepositoService.java        # Servicios depósitos
│       ├── TarifaService.java          # Servicios tarifas
│       └── ParametroService.java       # Servicios parámetros
├── src/main/resources/
│   ├── application.properties          # Configuración local
│   ├── application-docker.properties   # Configuración Docker
│   └── data.sql                        # Datos de inicialización
└── pom.xml                            # Dependencias Maven
```

#### **🗃️ Base de Datos: `tpi_backend_administracion_db`**
```sql
-- Tablas principales:
camiones            # Flota de vehículos de transporte
depositos           # Centros de distribución y almacenes
tarifas             # Precios y costos por servicio
parametros_globales # Configuraciones del sistema
```

#### **🔗 Endpoints Principales:**
```
GET/POST/PUT/DELETE /camiones          # CRUD camiones
GET/POST/PUT/DELETE /depositos         # CRUD depósitos  
GET/POST/PUT/DELETE /tarifas           # CRUD tarifas
GET/POST/PUT/DELETE /parametros        # CRUD parámetros
```

---

### 🗺️ **3. Servicio Logística (Puerto 8083)**
**Directorio:** `ServicioLogistica/`

#### **🎯 Propósito:**
- **Planificación de rutas** de transporte
- **Gestión de tramos** entre ubicaciones
- **Optimización de recorridos** y costos
- **Seguimiento de estado** de rutas

#### **📂 Estructura Interna:**
```
ServicioLogistica/
├── src/main/java/utnfc/isi/back/sim/
│   ├── AppMain.java                    # Aplicación principal
│   ├── controller/                     # Controladores REST
│   │   ├── RutaController.java         # CRUD rutas
│   │   └── TramoController.java        # CRUD tramos
│   ├── domain/                         # Entidades JPA
│   │   ├── Ruta.java                   # Entidad ruta completa
│   │   └── Tramo.java                  # Entidad tramo individual
│   ├── repository/                     # Repositorios JPA
│   │   ├── RutaRepository.java         # Acceso a datos rutas
│   │   └── TramoRepository.java        # Acceso a datos tramos
│   └── service/                        # Lógica de negocio
│       ├── RutaService.java            # Servicios de rutas
│       └── TramoService.java           # Servicios de tramos
├── src/main/resources/
│   ├── application.properties          # Configuración local
│   └── application-docker.properties   # Configuración Docker
└── pom.xml                            # Dependencias Maven
```

#### **🗃️ Base de Datos: `tpi_backend_logistica_db`**
```sql
-- Tablas principales:
rutas     # Rutas completas de transporte (solicitud_id, estado, costos)
tramos    # Segmentos individuales de cada ruta (origen, destino, distancia)
```

#### **🔗 Endpoints Principales:**
```
GET/POST/PUT /api/rutas               # CRUD rutas
GET/POST/PUT /api/rutas/{id}/tramos   # CRUD tramos de ruta específica
GET /api/rutas?estado=ACTIVA          # Filtros por estado
PUT /api/rutas/{id}/estado            # Actualizar estado de ruta
```

#### **📊 Estados de Ruta:**
```java
PLANIFICADA   # Ruta creada pero no iniciada
EN_PROGRESO   # Ruta en ejecución
COMPLETADA    # Ruta finalizada exitosamente  
CANCELADA     # Ruta cancelada o abortada
```

---

### 📦 **4. Servicio Pedidos (Puerto 8084)**
**Directorio:** `ServicioPedidos/`

#### **🎯 Propósito:**
- **Gestión de clientes** y usuarios
- **Administración de contenedores** para transporte
- **Creación y seguimiento de solicitudes** de transporte
- **Estados y lifecycle** de pedidos

#### **📂 Estructura Interna:**
```
ServicioPedidos/
├── src/main/java/utnfc/isi/back/sim/
│   ├── AppMain.java                    # Aplicación principal
│   ├── controller/                     # Controladores REST
│   │   ├── ClienteController.java      # CRUD clientes
│   │   ├── ContenedorController.java   # CRUD contenedores
│   │   ├── SolicitudController.java    # CRUD solicitudes
│   │   └── HomeController.java         # Endpoint raíz
│   ├── domain/                         # Entidades JPA
│   │   ├── Cliente.java                # Entidad cliente
│   │   ├── Contenedor.java             # Entidad contenedor
│   │   └── Solicitud.java              # Entidad solicitud
│   ├── repository/                     # Repositorios JPA
│   │   ├── ClienteRepository.java      # Acceso a datos clientes
│   │   ├── ContenedorRepository.java   # Acceso a datos contenedores
│   │   └── SolicitudRepository.java    # Acceso a datos solicitudes
│   └── service/                        # Lógica de negocio
│       ├── ClienteService.java         # Servicios clientes
│       ├── ContenedorService.java      # Servicios contenedores
│       └── SolicitudService.java       # Servicios solicitudes
├── src/main/resources/
│   ├── application.properties          # Configuración local
│   └── application-docker.properties   # Configuración Docker
└── pom.xml                            # Dependencias Maven
```

#### **🗃️ Base de Datos: `tpi_backend_pedidos_db`**
```sql
-- Tablas principales:
clientes      # Usuarios del sistema (nombre, email, teléfono)
contenedores  # Contenedores disponibles para transporte
solicitudes   # Requests de transporte de los clientes
```

#### **🔗 Endpoints Principales:**
```
GET/POST/PUT/DELETE /clientes          # CRUD clientes
GET/POST/PUT/DELETE /contenedores      # CRUD contenedores
GET/POST/PUT/DELETE /solicitudes       # CRUD solicitudes
GET /solicitudes?clienteId=X           # Filtros por cliente
GET /solicitudes?estado=PENDIENTE      # Filtros por estado
```

---

### 🌍 **5. Servicio Geolocalización (Puerto 8081)**
**Directorio:** `ServicioGeolocalizacion/`

#### **🎯 Propósito:**
- **Cálculo de distancias** entre ubicaciones
- **Integración con Google Maps API** para rutas reales
- **Geocodificación** de direcciones
- **Estimación de tiempos** de viaje

#### **📂 Estructura Interna:**
```
ServicioGeolocalizacion/
├── src/main/java/utnfc/isi/back/sim/
│   ├── AppMain.java                    # Aplicación principal
│   ├── controller/                     # Controladores REST
│   │   └── GeoController.java          # API de geolocalización
│   ├── service/                        # Lógica de negocio
│   │   └── GeoService.java             # Servicios de geo
│   └── config/                         # Configuraciones
│       └── GoogleMapsConfig.java       # Config Google Maps
├── src/main/resources/
│   ├── application.properties          # Configuración local
│   └── application-docker.properties   # Configuración Docker
└── pom.xml                            # Dependencias Maven
```

#### **🔗 Endpoints Principales:**
```
GET /api/geo/distancia?origen=X&destino=Y    # Calcular distancia
GET /api/geo/geocode?direccion=X             # Geocodificar dirección
GET /api/geo/ruta?origen=X&destino=Y         # Obtener ruta completa
GET /api/geo/tiempo?origen=X&destino=Y       # Estimar tiempo viaje
```

**⚠️ Nota:** Este servicio **NO** usa base de datos, solo APIs externas.

---

## 🔄 **Interconexión Entre Microservicios**

### **🔗 Flujo de Comunicación:**

```
1. CLIENTE → API GATEWAY
   └─ Todas las peticiones pasan por el gateway

2. PEDIDOS ←→ ADMINISTRACIÓN
   └─ Las solicitudes consultan camiones disponibles

3. LOGÍSTICA ←→ ADMINISTRACIÓN  
   └─ Las rutas usan datos de depósitos y camiones

4. LOGÍSTICA ←→ GEOLOCALIZACIÓN
   └─ Cálculo de distancias y rutas óptimas

5. LOGÍSTICA ←→ PEDIDOS
   └─ Las rutas se asocian a solicitudes específicas
```

### **📊 Ejemplo de Flujo Completo:**

```
1. Cliente crea SOLICITUD de transporte (Pedidos)
2. Sistema consulta CAMIONES disponibles (Administración)  
3. Se calcula RUTA óptima (Logística + Geolocalización)
4. Se asigna CAMIÓN específico (Administración)
5. Se inicia seguimiento de RUTA (Logística)
```

---

## 🐳 **Configuración Docker**

### **📋 Archivo Principal: `docker-compose-definitivo.yml`**

```yaml
# Arquitectura de contenedores:
postgres                    # Base de datos única con 3 esquemas
├── tpi_backend_administracion_db
├── tpi_backend_logistica_db  
└── tpi_backend_pedidos_db

api-gateway                 # Puerto 8080 - Punto de entrada
servicio-administracion     # Puerto 8082 - Gestión de flota
servicio-logistica         # Puerto 8083 - Rutas y tramos
servicio-pedidos           # Puerto 8084 - Clientes y solicitudes
servicio-geolocalizacion   # Puerto 8081 - APIs geográficas
```

### **🚀 Comandos de Ejecución:**

```bash
# Levantar sistema completo
./start.bat

# Ver logs en tiempo real  
./logs.bat

# Detener sistema
./stop.bat

# Verificar estado
docker ps
```

---

## 🗃️ **Separación de Bases de Datos**

### **🔒 Aislamiento Garantizado:**

Cada microservicio tiene su **propia base de datos lógica** dentro de la misma instancia PostgreSQL:

```sql
-- Verificar separación:
docker exec -it tpi-postgres psql -U tpi_user -d postgres 
    -c "SELECT datname FROM pg_database WHERE datname LIKE 'tpi_backend%';"

-- Resultado:
tpi_backend_administracion_db  ← Solo camiones, depósitos, tarifas
tpi_backend_logistica_db       ← Solo rutas y tramos  
tpi_backend_pedidos_db         ← Solo clientes, contenedores, solicitudes
```

### **🛡️ Beneficios del Aislamiento:**
- ✅ **Escalabilidad independiente** por servicio
- ✅ **Fallos aislados** - si un servicio falla, otros siguen
- ✅ **Despliegues independientes** de cada microservicio
- ✅ **Tecnologías específicas** por dominio de negocio
- ✅ **Equipos independientes** pueden trabajar en paralelo

---

## 🛠️ **Tecnologías Utilizadas**

| Componente | Tecnología | Versión | Propósito |
|------------|------------|---------|-----------|
| **Framework** | Spring Boot | 3.3.5 | Microservicios REST |
| **Base de Datos** | PostgreSQL | 15-alpine | Persistencia de datos |
| **ORM** | Spring Data JPA | - | Mapeo objeto-relacional |
| **Containerización** | Docker | - | Despliegue y orquestación |
| **Build Tool** | Maven | 3.9 | Gestión de dependencias |
| **Java Runtime** | Eclipse Temurin | 21-JRE | Ejecución de aplicaciones |

---

## 📞 **Endpoints de Prueba**

### **🔗 Acceso Principal (API Gateway):**
```
http://localhost:8080                    # Información del sistema
http://localhost:8080/api/admin/camiones # Camiones via gateway
http://localhost:8080/api/pedidos/clientes # Clientes via gateway
```

### **🔗 Acceso Directo (Testing):**
```
http://localhost:8082/camiones           # Administración directa
http://localhost:8083/api/rutas          # Logística directa  
http://localhost:8084/clientes           # Pedidos directa
http://localhost:8081/api/geo/health     # Geolocalización directa
```

---

## 🎯 **Casos de Uso Principales**

### **📋 Gestión de Flota:**
1. Registrar nuevos camiones
2. Asignar camiones a rutas
3. Controlar disponibilidad
4. Gestionar costos y tarifas

### **📦 Gestión de Pedidos:**
1. Registrar clientes
2. Crear solicitudes de transporte
3. Asignar contenedores
4. Seguimiento de estados

### **🗺️ Planificación Logística:**
1. Crear rutas optimizadas
2. Calcular distancias reales
3. Estimar costos de transporte
4. Monitorear progreso

---

## 🚀 **Próximos Pasos y Extensiones**

### **🔧 Mejoras Técnicas:**
- [ ] Implementar **Spring Security** para autenticación
- [ ] Agregar **circuit breakers** con Hystrix
- [ ] Configurar **service discovery** con Eureka
- [ ] Implementar **distributed tracing** con Zipkin

### **📊 Mejoras Funcionales:**
- [ ] Dashboard de **monitoreo en tiempo real**
- [ ] **Notificaciones push** de estado de rutas
- [ ] **Optimización automática** de rutas con IA
- [ ] **Integración con sistemas ERP** externos

---

**📅 Última actualización:** Noviembre 2025  
**👥 Desarrollado por:** Equipo TPI Backend  
**📧 Soporte:** [Contactar al equipo de desarrollo]
# TPI Backend - Sistema de Microservicios con Docker

## 🚀 Inicio Rápido

### Prerequisitos
- Docker Desktop instalado y funcionando
- 8GB de RAM disponible (recomendado)
- Puertos 8081-8085 y 5432 libres

### Iniciar todo el sistema
```bash
# Opción 1: Usar el script
start.bat

# Opción 2: Comando manual
docker-compose up -d --build
```

### Parar el sistema
```bash
# Opción 1: Usar el script
stop.bat

# Opción 2: Comando manual
docker-compose down
```

### Ver logs
```bash
# Opción 1: Usar el script
logs.bat

# Opción 2: Comando manual
docker-compose logs -f
```

## 🌐 Endpoints

| Servicio | Puerto | URL | Descripción |
|----------|--------|-----|-------------|
| **API Gateway** | 8085 | http://localhost:8085 | Punto de entrada principal |
| **Administración** | 8081 | http://localhost:8081 | Gestión de camiones, depósitos, tarifas |
| **Logística** | 8082 | http://localhost:8082 | Rutas y planificación |
| **Pedidos** | 8083 | http://localhost:8083 | Clientes, contenedores, solicitudes |
| **Geolocalización** | 8084 | http://localhost:8084 | Google Maps Distance Matrix |
| **PostgreSQL** | 5432 | localhost:5432 | Base de datos |

## 🔗 Rutas del API Gateway

- `http://localhost:8085/api/admin/*` → Servicio Administración
- `http://localhost:8085/api/logistica/*` → Servicio Logística
- `http://localhost:8085/api/pedidos/*` → Servicio Pedidos
- `http://localhost:8085/api/geo/*` → Servicio Geolocalización

## 🧪 Probar el sistema

### 1. Verificar que todo está funcionando
```bash
curl http://localhost:8085
```

### 2. Probar Geolocalización
```bash
curl "http://localhost:8085/api/geo/distancia?origen=-31.4167,-64.1833&destino=-32.8908,-68.8272"
```

### 3. Health Checks
```bash
curl http://localhost:8085/actuator/health
curl http://localhost:8084/actuator/health
curl http://localhost:8083/actuator/health
```

## 🛠️ Comandos útiles

### Ver estado de contenedores
```bash
docker-compose ps
```

### Reiniciar un servicio específico
```bash
docker-compose restart servicio-pedidos
```

### Ver logs de un servicio específico
```bash
docker-compose logs -f servicio-geolocalizacion
```

### Reconstruir un servicio
```bash
docker-compose up -d --build servicio-pedidos
```

### Limpiar todo (incluyendo base de datos)
```bash
docker-compose down -v
docker system prune -f
```

## 🗃️ Base de Datos

- **Motor**: PostgreSQL 15
- **Usuario**: postgres
- **Contraseña**: Bongi45694
- **Bases de datos**:
  - `tpi_backend_administracion_db`
  - `tpi_backend_logistica_db`
  - `tpi_backend_pedidos_db`

### Conectar a la base de datos
```bash
# Desde fuera del contenedor
psql -h localhost -p 5432 -U postgres -d tpi_backend

# Desde dentro del contenedor
docker exec -it tpi-postgres psql -U postgres
```

## 🐛 Troubleshooting

### El servicio no inicia
1. Verificar logs: `docker-compose logs nombre-servicio`
2. Verificar que Docker Desktop esté funcionando
3. Verificar que los puertos no estén ocupados

### No se puede conectar a la base de datos
1. Esperar a que PostgreSQL esté completamente iniciado (30-60 segundos)
2. Verificar health check: `docker-compose ps`

### Memoria insuficiente
1. Cerrar otros programas
2. Aumentar memoria de Docker Desktop
3. Ajustar `JAVA_OPTS` en docker-compose.yml

### Limpiar caché de Docker
```bash
docker system prune -a
docker volume prune
```

## 📊 Monitoreo

Todos los servicios exponen métricas de Actuator:
- `/actuator/health` - Estado del servicio
- `/actuator/info` - Información del servicio
- `/actuator/metrics` - Métricas de rendimiento

## 🔧 Desarrollo

### Modificar un servicio
1. Hacer cambios en el código
2. Reconstruir: `docker-compose up -d --build nombre-servicio`
3. Ver logs: `docker-compose logs -f nombre-servicio`

### Variables de entorno
Configurables en `docker-compose.yml`:
- `GOOGLE_MAPS_API_KEY` - API key de Google Maps
- `SPRING_DATASOURCE_*` - Configuración de base de datos
- `JAVA_OPTS` - Opciones de JVM
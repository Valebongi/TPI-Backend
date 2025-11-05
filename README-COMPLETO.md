# 🚚 TPI Backend - Sistema de Gestión Logística

Sistema de microservicios para gestión logística implementado con Spring Boot y Docker.

## 🏗️ Arquitectura

El sistema está compuesto por los siguientes microservicios:

- **🗄️ PostgreSQL**: Base de datos principal
- **📍 Servicio Geolocalización** (Puerto 8081): Integración con Google Maps Distance Matrix API
- **⚙️ Servicio Administración** (Puerto 8082): Gestión de camiones, parámetros, tarifas y depósitos
- **🚛 Servicio Logística** (Puerto 8083): Gestión de rutas y tramos de transporte
- **📦 Servicio Pedidos** (Puerto 8084): Gestión de solicitudes, contenedores y clientes
- **🌐 API Gateway** (Puerto 8080): Punto de entrada unificado al sistema

## 🚀 Instalación y Uso

### Prerrequisitos

- Docker Desktop
- Git

### Inicio Rápido

1. **Clonar el repositorio:**
```bash
git clone [URL_DEL_REPOSITORIO]
cd TPI-Backend/TPI
```

2. **Levantar el sistema completo:**
```bash
# Windows
start-full-system.bat

# O manualmente con Docker Compose
docker-compose -f docker-compose-completo.yml up --build
```

3. **Detener el sistema:**
```bash
# Windows
stop-full-system.bat

# O manualmente
docker-compose -f docker-compose-completo.yml down
```

### ⚡ Primera Ejecución

La primera vez puede tardar 5-10 minutos ya que Docker necesita:
- Descargar las imágenes base (PostgreSQL, Maven, OpenJDK)
- Compilar cada microservicio
- Inicializar la base de datos

### 🔗 URLs de Acceso

Una vez levantado el sistema:

- **API Gateway**: http://localhost:8080
- **Servicio Geolocalización**: http://localhost:8081
- **Servicio Administración**: http://localhost:8082  
- **Servicio Logística**: http://localhost:8083
- **Servicio Pedidos**: http://localhost:8084
- **PostgreSQL**: localhost:5432 (usuario: `tpi_user`, password: `tpi_pass`, base: `tpi_db`)

### 🏥 Health Checks

Todos los servicios implementan health checks:
- http://localhost:808X/actuator/health

## 📊 Base de Datos

El sistema utiliza PostgreSQL con las siguientes características:

- **Host**: localhost:5432
- **Base de datos**: tpi_db
- **Usuario**: tpi_user
- **Contraseña**: tpi_pass
- **Scripts de inicialización**: Se encuentran en `init-db/`

## 🛠️ Desarrollo

### Estructura del Proyecto

```
TPI/
├── ApiGateway/              # API Gateway con Spring Cloud Gateway
├── ServicioAdministracion/  # Gestión administrativa
├── ServicioGeolocalizacion/ # Integración con Google Maps
├── ServicioLogistica/       # Gestión de rutas y logística
├── ServicioPedidos/         # Gestión de pedidos y clientes
├── init-db/                 # Scripts SQL de inicialización
├── docker-compose-completo.yml
├── start-full-system.bat
└── stop-full-system.bat
```

### Tecnologías Utilizadas

- **Spring Boot 3.3.5** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **PostgreSQL 15** - Base de datos
- **Docker & Docker Compose** - Containerización
- **Maven** - Gestión de dependencias
- **Java 21** - Lenguaje de programación

### 🔧 Compilación Individual

Para compilar un servicio específico:

```bash
cd ServicioXXX
docker build -t servicio-xxx .
```

### 📝 Configuración

Cada servicio tiene su configuración en:
- `application.properties` - Configuración por defecto
- `application-docker.properties` - Configuración para Docker

## 🌍 API de Geolocalización

Para usar la funcionalidad de geolocalización con Google Maps:

1. Obtener una API Key de Google Maps Distance Matrix
2. Configurar la variable de entorno `GOOGLE_MAPS_API_KEY` en el docker-compose

## 🚨 Solución de Problemas

### Error de compilación Lombok
✅ **Solucionado**: Todas las dependencias de Lombok han sido removidas y reemplazadas por código explícito para compatibilidad con Docker.

### Problemas de conexión a base de datos
- Verificar que PostgreSQL esté ejecutándose
- Comprobar las credenciales en docker-compose-completo.yml
- Revisar logs: `docker-compose logs postgres`

### Servicios que no inician
- Verificar logs: `docker-compose logs [nombre-servicio]`
- Los servicios esperan a que sus dependencias estén saludables (health checks)

### Puertos ocupados
- Verificar que los puertos 8080-8084 y 5432 estén libres
- Modificar puertos en docker-compose-completo.yml si es necesario

## 📈 Monitoreo

Todos los servicios exponen endpoints de actuator:
- `/actuator/health` - Estado del servicio
- `/actuator/info` - Información del servicio

## 🤝 Contribución

1. Fork el proyecto
2. Crear una rama feature (`git checkout -b feature/nueva-caracteristica`)
3. Commit los cambios (`git commit -am 'Agregar nueva característica'`)
4. Push a la rama (`git push origin feature/nueva-caracteristica`)
5. Crear un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

---

🚀 **¡El sistema está listo para usar! Ejecuta `start-full-system.bat` y accede a http://localhost:8080**
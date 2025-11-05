# 📚 Índice de Documentación - Sistema TPI Backend

## 🎯 **¿Qué es este sistema?**

El **Sistema TPI Backend** es una solución de **microservicios** para la gestión integral de **transporte de contenedores**, desarrollado con **Spring Boot**, **Docker** y **PostgreSQL**. 

Permite gestionar flotas de camiones, planificar rutas logísticas, administrar pedidos de clientes y calcular geolocalización, todo a través de una **arquitectura distribuida** y **escalable**.

---

## 📖 **Documentación Disponible**

### 📋 **[README.md](./README.md)** - *Documentación Principal*
**🎯 Para:** Desarrolladores y arquitectos  
**📝 Contiene:**
- Arquitectura general del sistema
- Descripción detallada de cada microservicio
- Estructura de bases de datos separadas
- Configuración Docker completa
- Tecnologías utilizadas
- Endpoints principales

### 🔧 **[GUIA-PRACTICA.md](./GUIA-PRACTICA.md)** - *Manual de Uso*
**🎯 Para:** Testers, QA y desarrolladores  
**📝 Contiene:**
- Instrucciones de inicio rápido
- Ejemplos completos para Postman
- Casos de uso paso a paso
- Scripts de PowerShell
- Troubleshooting común
- Comandos de debugging

### 🏗️ **[DIAGRAMAS-ARQUITECTURA.md](./DIAGRAMAS-ARQUITECTURA.md)** - *Diagramas Técnicos*
**🎯 Para:** Arquitectos de software y equipos técnicos  
**📝 Contiene:**
- Diagramas de arquitectura de alto nivel
- Flujos de comunicación entre servicios
- Modelo de datos completo
- Estados y flujos de negocio
- Arquitectura de contenedores Docker
- Planes de escalabilidad

### 📦 **[DATOS-EJEMPLO.md](./DATOS-EJEMPLO.md)** - *Data de Testing*
**🎯 Para:** Testers y desarrolladores  
**📝 Contiene:**
- JSONs listos para Postman
- Datos de ejemplo para todos los servicios
- Scripts de carga masiva
- Configuración de environments
- Ejemplos de cada entidad

---

## 🚀 **Inicio Rápido - 5 Minutos**

### **1. ¿Primera vez? Lee esto:**
1. **[README.md](./README.md)** - Entiende la arquitectura
2. **[GUIA-PRACTICA.md](./GUIA-PRACTICA.md)** - Sección "Inicio Rápido"

### **2. Levantar el sistema:**
```bash
cd c:\Codigo\TPI-Backend\TPI
.\start.bat
```

### **3. Verificar que funciona:**
```bash
# API Gateway principal
curl http://localhost:8080

# Servicios individuales
curl http://localhost:8082/camiones      # Administración
curl http://localhost:8084/clientes      # Pedidos  
curl http://localhost:8083/api/rutas     # Logística
```

### **4. Cargar datos de prueba:**
- Abrir **[DATOS-EJEMPLO.md](./DATOS-EJEMPLO.md)**
- Copiar JSONs en Postman
- Crear camiones, clientes, rutas, etc.

---

## 🎯 **¿Qué quieres hacer?**

### 🔍 **Entender el Sistema**
- **[README.md](./README.md)** → Arquitectura completa
- **[DIAGRAMAS-ARQUITECTURA.md](./DIAGRAMAS-ARQUITECTURA.md)** → Visualizar componentes

### 🧪 **Probar/Testing**  
- **[GUIA-PRACTICA.md](./GUIA-PRACTICA.md)** → Ejemplos paso a paso
- **[DATOS-EJEMPLO.md](./DATOS-EJEMPLO.md)** → Data lista para usar

### 🐛 **Resolver Problemas**
- **[GUIA-PRACTICA.md](./GUIA-PRACTICA.md)** → Sección "Troubleshooting"
- **Logs:** `.\logs.bat`

### 🏗️ **Desarrollar/Extender**
- **[README.md](./README.md)** → Estructura de código
- **[DIAGRAMAS-ARQUITECTURA.md](./DIAGRAMAS-ARQUITECTURA.md)** → Patrones de integración

---

## 🌐 **Arquitectura en Resumen**

```
Cliente → API Gateway (8080) → Microservicios → PostgreSQL (3 DBs separadas)
           ↓
    ┌─────────────────────────────────────────┐
    │ 🏢 Administración (8082)                │ → administracion_db
    │ 📦 Logística (8083)                     │ → logistica_db  
    │ 📋 Pedidos (8084)                       │ → pedidos_db
    │ 🗺️ Geolocalización (8081)              │ → Google Maps API
    └─────────────────────────────────────────┘
```

---

## 🛠️ **Stack Tecnológico**

| Componente | Tecnología | Puerto | Base de Datos |
|------------|------------|--------|---------------|
| **API Gateway** | Spring Cloud Gateway | 8080 | - |
| **Administración** | Spring Boot 3.3.5 | 8082 | ✅ PostgreSQL |
| **Logística** | Spring Boot 3.3.5 | 8083 | ✅ PostgreSQL |
| **Pedidos** | Spring Boot 3.3.5 | 8084 | ✅ PostgreSQL |
| **Geolocalización** | Spring Boot 3.3.5 | 8081 | - |
| **Base de Datos** | PostgreSQL 15 | 5432 | 3 esquemas separados |
| **Containerización** | Docker + Compose | - | - |

---

## 📞 **Endpoints Principales**

### **🌐 A través del API Gateway (Recomendado):**
```
http://localhost:8080                           # Info del sistema
http://localhost:8080/api/admin/camiones        # Gestión de flota
http://localhost:8080/api/pedidos/clientes      # Gestión de clientes
http://localhost:8080/api/logistica/rutas       # Planificación logística
http://localhost:8080/api/geo/distancia         # Servicios geográficos
```

### **🔗 Acceso directo a servicios (Testing):**
```
http://localhost:8082/camiones                  # Administración
http://localhost:8083/api/rutas                 # Logística
http://localhost:8084/clientes                  # Pedidos
http://localhost:8081/actuator/health           # Geolocalización
```

---

## 🎓 **¿Eres nuevo en microservicios?**

### **📚 Conceptos clave:**
- **Microservicio:** Aplicación pequeña con responsabilidad específica
- **API Gateway:** Punto de entrada único que enruta peticiones
- **Base de datos separada:** Cada servicio tiene su propia BD para aislamiento
- **Docker:** Containerización para despliegue fácil y consistente

### **🔄 Flujo típico:**
1. Cliente hace petición al **Gateway** (puerto 8080)
2. Gateway enruta al **microservicio** correspondiente
3. Microservicio procesa y consulta su **base de datos**
4. Respuesta regresa por el mismo camino

---

## 💡 **Tips para Desarrolladores**

### **🔧 Comandos útiles:**
```bash
# Ver todos los contenedores
docker ps

# Ver logs de un servicio específico
docker logs tpi-servicio-administracion

# Reiniciar un servicio
docker restart tpi-servicio-logistica

# Acceder a PostgreSQL
docker exec -it tpi-postgres psql -U tpi_user -d postgres
```

### **🧪 Testing rápido:**
```bash
# Probar que todo responde
curl http://localhost:8080 && echo "✅ Gateway OK"
curl http://localhost:8082/camiones && echo "✅ Admin OK" 
curl http://localhost:8084/clientes && echo "✅ Pedidos OK"
```

---

## 🆘 **¿Problemas? ¿Preguntas?**

### **🔍 Debugging paso a paso:**
1. **¿Los contenedores están corriendo?** → `docker ps`
2. **¿Hay errores en los logs?** → `.\logs.bat`
3. **¿PostgreSQL está OK?** → `docker exec tpi-postgres pg_isready -U tpi_user`
4. **¿Los puertos están libres?** → `netstat -ano | findstr :8080`

### **📖 Consultar documentación:**
- **Error de conexión:** [GUIA-PRACTICA.md](./GUIA-PRACTICA.md) - Troubleshooting
- **No entiendo la arquitectura:** [README.md](./README.md) - Arquitectura 
- **Necesito datos de prueba:** [DATOS-EJEMPLO.md](./DATOS-EJEMPLO.md)
- **Quiero ver diagramas:** [DIAGRAMAS-ARQUITECTURA.md](./DIAGRAMAS-ARQUITECTURA.md)

---

## 📅 **Información de Versión**

- **Versión Sistema:** 1.0.0
- **Spring Boot:** 3.3.5  
- **Java:** 21 (Eclipse Temurin)
- **PostgreSQL:** 15-alpine
- **Docker Compose:** 3.8+

**📅 Última actualización:** Noviembre 2025  
**👥 Desarrollado por:** Equipo TPI Backend  
**📧 Soporte:** Documentación en este mismo directorio

---

## 🎉 **¡Listo para empezar!**

1. 📖 **Lee** el [README.md](./README.md) para entender el sistema
2. 🚀 **Levanta** el sistema con `.\start.bat` 
3. 🧪 **Prueba** con los ejemplos de [GUIA-PRACTICA.md](./GUIA-PRACTICA.md)
4. 📦 **Carga datos** desde [DATOS-EJEMPLO.md](./DATOS-EJEMPLO.md)
5. 🎯 **¡Comienza a desarrollar!**
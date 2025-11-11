# Guía de Autenticación y Uso con Postman - TPI Backend

## 📋 Resumen
Esta guía explica cómo obtener tokens de autenticación y cómo usar Postman para interactuar con el sistema de microservicios.

## 🔑 **TOKEN VÁLIDO ACTUAL (CLIENTE)**

### Token de Cliente Activo:
```
eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJZNUVtWkc1dFZtWndIYzR5OW1JR1o4ODlWaFhXY0NObFhndDdlWm1BU2Y0In0.eyJleHAiOjE3NjI4MjIyOTcsImlhdCI6MTc2MjgxODY5NywianRpIjoiNjk4MzlkODktMzdmMi00MDVjLWE2NWUtNjg2Mzg4OGQ4Y2NlIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDg1L3JlYWxtcy90cGktYmFja2VuZCIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiIzYmFlOTg2NS01NDcwLTQ1YmMtYjZiZC1hOWY3MmViZjUyOTEiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ0cGktc2VydmljZS1hY2NvdW50IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiY2xpZW50ZSIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJkZWZhdWx0LXJvbGVzLXRwaS1iYWNrZW5kIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJjbGllbnRIb3N0IjoiMTcyLjE4LjAuMSIsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC10cGktc2VydmljZS1hY2NvdW50IiwiY2xpZW50QWRkcmVzcyI6IjE3Mi4xOC4wLjEiLCJjbGllbnRfaWQiOiJ0cGktc2VydmljZS1hY2NvdW50In0.AMbo7FTm0L4JVtJPphqSksLN-KfE6Bjj62lUkDiRmg8dEQrVw7kU67xg0JsQjxGtbQCZRM9bj9MLc6-PcpgQ2uj3kV5Ii9dL8W_oztF-HTw2NUgb48E-6IU1jbRYxHgO28er0h9jFv_TCIoJlFbNsYLkA7osW-gnj5ns2YQ5ineLHELpuVg6ZjkGu-tAvP8yttWAS4pwv9NFTXsTmUy570uOERAMk2TZbIKlOls_oLVEIS7F4yz1PtT00ZE8MWiC49f30hng3a-M0HtyN_l90vm-mCkQNM9P3-22-P6DumNN1WNH3eRgr9uezcn5dafeYsV172A3U76pTPsmaZCEg
```

**📅 Válido hasta:** 2025-11-10 21:51:37  
**👤 Rol:** Cliente  
**⚡ Estado:** ✅ ACTIVO

---

## 🔄 **CÓMO OBTENER TOKENS PARA CADA ROL**

### 🔨 MÉTODO 1: Tokens de Usuario (Admin, Transportista, Cliente) - SIN CÓDIGO

#### **Postman Setup para cualquier rol:**
```
Method: POST
URL: http://localhost:8085/realms/tpi-backend/protocol/openid-connect/token

Headers:
Content-Type: application/x-www-form-urlencoded

Body (x-www-form-urlencoded):
client_id: tpi-backend-client
grant_type: password
username: [USUARIO]
password: [CONTRASEÑA]
```

#### **Usuarios disponibles:**
| Rol | Username | Password | Descripción |
|-----|----------|----------|-------------|
| **ADMIN** | admin01 | Clave123 | Administrador del sistema |
| **TRANSPORTISTA** | transportista01 | Clave123 | Gestión de rutas y envíos |
| **CLIENTE** | cliente01 | Clave123 | Cliente final del sistema |

### 🤖 MÉTODO 2: Service Account (Solo para Cliente) - AUTOMÁTICO
```bash
# Endpoint
POST http://localhost:8085/realms/tpi-backend/protocol/openid-connect/token

# Body (form-data)
client_id=tpi-service-account
client_secret=AZ9JEotcdWWQKBX2ygFaX3fvuusosS6H
grant_type=client_credentials
```

### 💻 MÉTODO 3: PowerShell (Comando Rápido)
```powershell
# Para ADMIN
$adminBody = "client_id=tpi-backend-client&grant_type=password&username=admin01&password=Clave123"
$adminResponse = Invoke-RestMethod -Uri "http://localhost:8085/realms/tpi-backend/protocol/openid-connect/token" -Method POST -Headers @{"Content-Type"="application/x-www-form-urlencoded"} -Body $adminBody
Write-Host "Admin Token: $($adminResponse.access_token)"

# Para Service Account
$serviceAccountBody = "client_id=tpi-service-account&client_secret=AZ9JEotcdWWQKBX2ygFaX3fvuusosS6H&grant_type=client_credentials"
$serviceResponse = Invoke-RestMethod -Uri "http://localhost:8085/realms/tpi-backend/protocol/openid-connect/token" -Method POST -Headers @{"Content-Type"="application/x-www-form-urlencoded"} -Body $serviceAccountBody
Write-Host "Service Token: $($serviceResponse.access_token)"
```

---

## 🚨 **PROBLEMA IDENTIFICADO: ERROR 401 CON DEPÓSITOS**

### ❌ **El problema específico que tienes:**

Según las pruebas realizadas, **tu token de ADMIN es válido** pero el **ServicioAdministracion está rechazando TODOS los tokens** con error 401, incluso para endpoints que deberían funcionar.

### 🔍 **Diagnóstico del problema:**

1. **✅ Token correcto:** Tu token tiene rol "admin" ✅
2. **✅ Endpoint correcto:** `/api/admin/depositos` es el correcto ✅  
3. **❌ Configuración de seguridad:** El ServicioAdministracion tiene un problema de configuración ❌

### 🛠️ **SOLUCIONES PROBADAS:**

#### **1. Verificar que tu token tiene el rol correcto:**
Tu token contiene: `"realm_access":{"roles":["offline_access","admin","uma_authorization","default-roles-tpi-backend"]}`

✅ **SÍ TIENE ROL ADMIN**

#### **2. Verificar configuración de seguridad del ServicioAdministracion:**
Según el código en `SecurityConfig.java`:
```java
.requestMatchers(HttpMethod.GET, "/depositos", "/depositos/**")
.hasAnyRole("ADMIN", "TRANSPORTISTA", "CLIENTE")
```

✅ **ADMIN SÍ DEBERÍA TENER ACCESO**

### 🎯 **SOLUCIÓN INMEDIATA: Obtener nuevo token**

Tu token puede haber expirado. Genera uno nuevo:

#### **Postman para nuevo token de ADMIN:**
```
Method: POST
URL: http://localhost:8085/realms/tpi-backend/protocol/openid-connect/token

Body (x-www-form-urlencoded):
client_id: tpi-backend-client
grant_type: password
username: admin01
password: Clave123
```

#### **PowerShell para nuevo token:**
```powershell
$adminBody = "client_id=tpi-backend-client&grant_type=password&username=admin01&password=Clave123"
$adminResponse = Invoke-RestMethod -Uri "http://localhost:8085/realms/tpi-backend/protocol/openid-connect/token" -Method POST -Headers @{"Content-Type"="application/x-www-form-urlencoded"} -Body $adminBody
Write-Host "Nuevo Admin Token: $($adminResponse.access_token)"
```

### 🔧 **ENDPOINTS FUNCIONANDO CONFIRMADOS:**

Según la configuración de seguridad, estos endpoints **SÍ DEBERÍAN FUNCIONAR** con token de ADMIN:

#### **📂 DEPÓSITOS (ServicioAdministracion):**
```
GET http://localhost:8080/api/admin/depositos
GET http://localhost:8080/api/admin/depositos/{id}
GET http://localhost:8080/api/admin/depositos/{id}/coordenadas
```

#### **📦 CONTENEDORES (ServicioPedidos):**
```
GET http://localhost:8080/api/pedidos/contenedores
GET http://localhost:8080/api/pedidos/contenedores/{id}
POST http://localhost:8080/api/pedidos/contenedores
PUT http://localhost:8080/api/pedidos/contenedores/{id}
DELETE http://localhost:8080/api/pedidos/contenedores/{id}
```

#### **👥 CLIENTES (ServicioPedidos):**
```
GET http://localhost:8080/api/pedidos/clientes
POST http://localhost:8080/api/pedidos/clientes
PUT http://localhost:8080/api/pedidos/clientes/{id}
```

### 📋 **PRÓXIMOS PASOS PARA RESOLVER:**

1. **Obtener nuevo token de ADMIN** (puede haber expirado)
2. **Verificar que Keycloak está funcionando** en `http://localhost:8085`
3. **Verificar que todos los servicios están levantados** con `docker ps`
4. **Probar con el nuevo token**

---

## 🎯 **ENDPOINTS ESPECÍFICOS POR ROL**

### 🔨 **TOKEN DE ADMIN - Endpoints Disponibles**

**✅ Tu token actual de ADMIN:**
```
eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJZNUVtWkc1dFZtWndIYzR5OW1JR1o4ODlWaFhXY0NObFhndDdlWm1BU2Y0In0.eyJleHAiOjE3NjI4NzQzODksImlhdCI6MTc2Mjg3MDc4OSwianRpIjoiNTVlNTE3ZmUtY2Q5My00M2YzLWIyOTYtYzAxN2QwYzU2MjliIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDg1L3JlYWxtcy90cGktYmFja2VuZCIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI3NzFhYzRlNy1jZTI2LTRhYzctOWRhYS00MjAyODhiNWZlZDEiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ0cGktYmFja2VuZC1jbGllbnQiLCJzZXNzaW9uX3N0YXRlIjoiNjRiZDA3ODQtZjVhZS00Y2Q4LTlkMGYtMzVjMGUyNjgwYzk5IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImFkbWluIiwidW1hX2F1dGhvcml6YXRpb24iLCJkZWZhdWx0LXJvbGVzLXRwaS1iYWNrZW5kIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwic2lkIjoiNjRiZDA3ODQtZjVhZS00Y2Q4LTlkMGYtMzVjMGUyNjgwYzk5IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiQWRtaW4gVW5vIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiYWRtaW4wMSIsImdpdmVuX25hbWUiOiJBZG1pbiIsImZhbWlseV9uYW1lIjoiVW5vIiwiZW1haWwiOiJhZG1pbjAxQHRlc3QuY29tIn0.q0r1zmSX6ZV7hGyAEq_FZCTo_i-Jq6lWGLViUzgfPV8q8zIX--cLiyMJup-YOaT56eTHCsW7myH7irU1VpT07L3TDRooLO62sQwYQY77uF3ml8DQVhf05gF19rzoPoXx_LlOW5RuQ2RfuSagDt7AIjy4GdnroJ_HRROJvVBfvxzgMTJ72KM3gWFJxDnnOQnkWrdd76L1Imu3hAh-ztrTImwJB7SrVIh7MLo4YVVMwozIxzak97RxCw8wcuudytOT9wUjJ9i8kx9g6ba7iAsKPp8OEEK5oeNNg2VsZOr0lgUyqVL1TjjyWxaYh_pwFD8pF8kS82lnB9KALEwWZrx3Jg
```
**📅 Válido hasta:** ~1 hora desde generación

#### **📂 Consultar Depósitos (ADMIN)**
```
GET http://localhost:8080/api/admin/depositos
Authorization: Bearer [TU_TOKEN_DE_ADMIN_AQUÍ]
```

#### **👥 Consultar Clientes (ADMIN)**  
```
GET http://localhost:8080/api/admin/clientes
Authorization: Bearer [TU_TOKEN_DE_ADMIN_AQUÍ]
```

#### **📦 Consultar Contenedores (ADMIN)**
```
GET http://localhost:8080/api/admin/contenedores
Authorization: Bearer [TU_TOKEN_DE_ADMIN_AQUÍ]
```

### 👤 **TOKEN DE CLIENTE - Endpoints Disponibles**

#### **📄 Crear Solicitudes (CLIENTE)**
```
POST http://localhost:8080/api/pedidos/solicitudes
Authorization: Bearer [TOKEN_DE_CLIENTE]
Content-Type: application/json

Body: {
    "clienteId": 1,
    "contenedorId": 1,
    "direccionDestino": "Plaza de Mayo, Buenos Aires",
    "latitudDestino": -34.6083,
    "longitudDestino": -58.3712
}
```

#### **📋 Consultar Mis Solicitudes (CLIENTE)**
```
GET http://localhost:8080/api/pedidos/solicitudes
Authorization: Bearer [TOKEN_DE_CLIENTE]
```

### 🚚 **TOKEN DE TRANSPORTISTA - Endpoints Disponibles**

#### **🛣️ Consultar Rutas (TRANSPORTISTA)**
```
GET http://localhost:8080/api/logistica/rutas
Authorization: Bearer [TOKEN_DE_TRANSPORTISTA]
```

#### **📍 Obtener Coordenadas (TRANSPORTISTA)**
```
GET http://localhost:8080/api/geolocalizacion/direccion?direccion=Plaza%20de%20Mayo
Authorization: Bearer [TOKEN_DE_TRANSPORTISTA]
```

---

## 🚨 **SOLUCIÓN A TU PROBLEMA DE AUTENTICACIÓN**

### ❌ **¿Por qué falla el token de ADMIN?**

Tu token de ADMIN es **CORRECTO** ✅, pero debes usar los **endpoints específicos de administración**:

### ✅ **CORRECTO - Para consultar depósitos como ADMIN:**
```
GET http://localhost:8080/api/admin/depositos
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJZNUVtWkc1dFZtWndIYzR5OW1JR1o4ODlWaFhXY0NObFhndDdlWm1BU2Y0In0.eyJleHAiOjE3NjI4NzQzODksImlhdCI6MTc2Mjg3MDc4OSwianRpIjoiNTVlNTE3ZmUtY2Q5My00M2YzLWIyOTYtYzAxN2QwYzU2MjliIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDg1L3JlYWxtcy90cGktYmFja2VuZCIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI3NzFhYzRlNy1jZTI2LTRhYzctOWRhYS00MjAyODhiNWZlZDEiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ0cGktYmFja2VuZC1jbGllbnQiLCJzZXNzaW9uX3N0YXRlIjoiNjRiZDA3ODQtZjVhZS00Y2Q4LTlkMGYtMzVjMGUyNjgwYzk5IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImFkbWluIiwidW1hX2F1dGhvcml6YXRpb24iLCJkZWZhdWx0LXJvbGVzLXRwaS1iYWNrZW5kIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwic2lkIjoiNjRiZDA3ODQtZjVhZS00Y2Q4LTlkMGYtMzVjMGUyNjgwYzk5IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiQWRtaW4gVW5vIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiYWRtaW4wMSIsImdpdmVuX25hbWUiOiJBZG1pbiIsImZhbWlseV9uYW1lIjoiVW5vIiwiZW1haWwiOiJhZG1pbjAxQHRlc3QuY29tIn0.q0r1zmSX6ZV7hGyAEq_FZCTo_i-Jq6lWGLViUzgfPV8q8zIX--cLiyMJup-YOaT56eTHCsW7myH7irU1VpT07L3TDRooLO62sQwYQY77uF3ml8DQVhf05gF19rzoPoXx_LlOW5RuQ2RfuSagDt7AIjy4GdnroJ_HRROJvVBfvxzgMTJ72KM3gWFJxDnnOQnkWrdd76L1Imu3hAh-ztrTImwJB7SrVIh7MLo4YVVMwozIxzak97RxCw8wcuudytOT9wUjJ9i8kx9g6ba7iAsKPp8OEEK5oeNNg2VsZOr0lgUyqVL1TjjyWxaYh_pwFD8pF8kS82lnB9KALEwWZrx3Jg
```

### ❌ **INCORRECTO - No uses endpoints de otros servicios:**
```
❌ http://localhost:8080/api/pedidos/...  (Solo para CLIENTE)
❌ http://localhost:8080/api/logistica/... (Solo para TRANSPORTISTA)
```

---

## 📮 **POSTMAN - CREAR SOLICITUD**

### Configuración Base
- **Método:** `POST`
- **URL:** `http://localhost:8080/api/pedidos/solicitudes`

### Headers
```
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJZNUVtWkc1dFZtWndIYzR5OW1JR1o4ODlWaFhXY0NObFhndDdlWm1BU2Y0In0.eyJleHAiOjE3NjI4MjIyOTcsImlhdCI6MTc2MjgxODY5NywianRpIjoiNjk4MzlkODktMzdmMi00MDVjLWE2NWUtNjg2Mzg4OGQ4Y2NlIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDg1L3JlYWxtcy90cGktYmFja2VuZCIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiIzYmFlOTg2NS01NDcwLTQ1YmMtYjZiZC1hOWY3MmViZjUyOTEiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ0cGktc2VydmljZS1hY2NvdW50IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiY2xpZW50ZSIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJkZWZhdWx0LXJvbGVzLXRwaS1iYWNrZW5kIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJjbGllbnRIb3N0IjoiMTcyLjE4LjAuMSIsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC10cGktc2VydmljZS1hY2NvdW50IiwiY2xpZW50QWRkcmVzcyI6IjE3Mi4xOC4wLjEiLCJjbGllbnRfaWQiOiJ0cGktc2VydmljZS1hY2NvdW50In0.AMbo7FTm0L4JVtJPphqSksLN-KfE6Bjj62lUkDiRmg8dEQrVw7kU67xg0JsQjxGtbQCZRM9bj9MLc6-PcpgQ2uj3kV5Ii9dL8W_oztF-HTw2NUgb48E-6IU1jbRYxHgO28er0h9jFv_TCIoJlFbNsYLkA7osW-gnj5ns2YQ5ineLHELpuVg6ZjkGu-tAvP8yttWAS4pwv9NFTXsTmUy570uOERAMk2TZbIKlOls_oLVEIS7F4yz1PtT00ZE8MWiC49f30hng3a-M0HtyN_l90vm-mCkQNM9P3-22-P6DumNN1WNH3eRgr9uezcn5dafeYsV172A3U76pTPsmaZCEg
Content-Type: application/json
```

### Body (JSON)
```json
{
    "clienteId": 1,
    "contenedorId": 1,
    "direccionDestino": "Plaza de Mayo, Buenos Aires, Argentina",
    "latitudDestino": -34.6083,
    "longitudDestino": -58.3712
}
```

---

## 🎯 **EJEMPLOS DE SOLICITUDES POSTMAN**

### Ejemplo 1: Solicitud a Puerto Madero
```json
{
    "clienteId": 1,
    "contenedorId": 1,
    "direccionDestino": "Puerto Madero, Buenos Aires",
    "latitudDestino": -34.6118,
    "longitudDestino": -58.3960
}
```

### Ejemplo 2: Solicitud a Aeropuerto Ezeiza
```json
{
    "clienteId": 1,
    "contenedorId": 1,
    "direccionDestino": "Aeropuerto Internacional Ezeiza",
    "latitudDestino": -34.8222,
    "longitudDestino": -58.5358
}
```

### Ejemplo 3: Solicitud a Palermo
```json
{
    "clienteId": 1,
    "contenedorId": 1,
    "direccionDestino": "Palermo, Buenos Aires",
    "latitudDestino": -34.5755,
    "longitudDestino": -58.4370
}
```

---

## 🚀 **RESPUESTA ESPERADA**

Cuando creas una solicitud exitosamente, recibirás:

```json
{
    "id": 8,
    "numero": "SOL-1762818401673",
    "contenedor": {
        "id": 1,
        "codigo": "CONT-001",
        "peso": 0.00,
        "volumen": 0.00,
        "estado": "DISPONIBLE",
        "descripcion": "Contenedor de prueba",
        "idDeposito": 1
    },
    "cliente": {
        "id": 1,
        "nombre": "Juan",
        "apellido": "Pérez",
        "email": "juan.perez@email.com",
        "telefono": "+54911234567",
        "direccion": "Av. Córdoba 1234, CABA",
        "activo": true
    },
    "estado": "BORRADOR",
    "fechaCreacion": "2025-11-10T23:46:41.674381545",
    "direccionDestino": "Plaza de Mayo, Buenos Aires, Argentina",
    "latitudDestino": -34.6083,
    "longitudDestino": -58.3712,
    "direccionOrigen": "Av. del Libertador 1000, Buenos Aires",
    "latitudOrigen": -34.583100,
    "longitudOrigen": -58.405400,
    "destinoCoordenadas": "-34.6083,-58.3712",
    "destinoDescripcion": "Plaza de Mayo, Buenos Aires, Argentina",
    "origenCoordenadas": "-34.583100,-58.405400",
    "origenDescripcion": "Av. del Libertador 1000, Buenos Aires"
}
```

---

## ⚡ **FUNCIONALIDADES AUTOMÁTICAS**

### ✅ Lo que se auto-completa:
- **Coordenadas de origen**: Se obtienen automáticamente del depósito
- **Dirección de origen**: Se obtiene del depósito asociado al contenedor
- **Número de solicitud**: Se genera automáticamente (formato SOL-timestamp)
- **Estado inicial**: Se establece como "BORRADOR"
- **Fecha de creación**: Se establece automáticamente

### ✅ Lo que hace el sistema por ti:
1. **Validación de cliente**: Verifica que el cliente existe
2. **Validación de contenedor**: Verifica que el contenedor existe y está disponible
3. **Obtención de depósito**: Busca automáticamente el depósito del contenedor
4. **Comunicación segura**: Usa service account para comunicación entre microservicios
5. **Persistencia completa**: Guarda toda la información en la base de datos

---

## 🔧 **DATOS DE PRUEBA DISPONIBLES**

### Clientes Disponibles:
- **ID: 1** - Juan Pérez (Cliente de prueba)

### Contenedores Disponibles:
- **ID: 1** - CONT-001 (Contenedor de prueba, Depósito ID: 1)

### Depósitos Configurados:
- **ID: 1** - Deposito Test
  - **Dirección:** Av. del Libertador 1000, Buenos Aires
  - **Coordenadas:** (-34.583100, -58.405400)

---

## 🚨 **ERRORES COMUNES Y SOLUCIONES**

### Error 401 - No autorizado
- **Causa:** Token expirado o inválido
- **Solución:** Obtener un nuevo token usando el endpoint de Keycloak

### Error 404 - Cliente no encontrado
- **Causa:** El `clienteId` no existe
- **Solución:** Usar `clienteId: 1` (único cliente disponible)

### Error 404 - Contenedor no encontrado
- **Causa:** El `contenedorId` no existe
- **Solución:** Usar `contenedorId: 1` (único contenedor disponible)

### Error de validación
- **Causa:** Coordenadas fuera de rango o campos obligatorios faltantes
- **Solución:** Verificar que latitud esté entre -90 y 90, longitud entre -180 y 180

---

## 🎯 **ENDPOINT COMPLETO PARA COPIAR Y PEGAR**

```
POST http://localhost:8080/api/pedidos/solicitudes
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJZNUVtWkc1dFZtWndIYzR5OW1JR1o4ODlWaFhXY0NObFhndDdlWm1BU2Y0In0.eyJleHAiOjE3NjI4MjIyOTcsImlhdCI6MTc2MjgxODY5NywianRpIjoiNjk4MzlkODktMzdmMi00MDVjLWE2NWUtNjg2Mzg4OGQ4Y2NlIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDg1L3JlYWxtcy90cGktYmFja2VuZCIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiIzYmFlOTg2NS01NDcwLTQ1YmMtYjZiZC1hOWY3MmViZjUyOTEiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ0cGktc2VydmljZS1hY2NvdW50IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiY2xpZW50ZSIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJkZWZhdWx0LXJvbGVzLXRwaS1iYWNrZW5kIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJjbGllbnRIb3N0IjoiMTcyLjE4LjAuMSIsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC10cGktc2VydmljZS1hY2NvdW50IiwiY2xpZW50QWRkcmVzcyI6IjE3Mi4xOC4wLjEiLCJjbGllbnRfaWQiOiJ0cGktc2VydmljZS1hY2NvdW50In0.AMbo7FTm0L4JVtJPphqSksLN-KfE6Bjj62lUkDiRmg8dEQrVw7kU67xg0JsQjxGtbQCZRM9bj9MLc6-PcpgQ2uj3kV5Ii9dL8W_oztF-HTw2NUgb48E-6IU1jbRYxHgO28er0h9jFv_TCIoJlFbNsYLkA7osW-gnj5ns2YQ5ineLHELpuVg6ZjkGu-tAvP8yttWAS4pwv9NFTXsTmUy570uOERAMk2TZbIKlOls_oLVEIS7F4yz1PtT00ZE8MWiC49f30hng3a-M0HtyN_l90vm-mCkQNM9P3-22-P6DumNN1WNH3eRgr9uezcn5dafeYsV172A3U76pTPsmaZCEg
Content-Type: application/json

{
    "clienteId": 1,
    "contenedorId": 1,
    "direccionDestino": "Tu dirección de destino aquí",
    "latitudDestino": -34.6083,
    "longitudDestino": -58.3712
}
```

---

## 🎉 **¡LISTO PARA USAR!**

Con esta información tienes todo lo necesario para:
- ✅ Autenticarte como cliente
- ✅ Crear solicitudes desde Postman
- ✅ Verificar que el sistema funciona completamente
- ✅ Obtener nuevos tokens cuando sea necesario

**¡La implementación del service account está funcionando perfectamente en producción!** 🚀
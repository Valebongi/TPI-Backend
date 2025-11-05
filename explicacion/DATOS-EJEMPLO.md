# 📦 Datos de Ejemplo para Testing - Sistema TPI Backend

## ⚠️ **ORDEN RECOMENDADO DE CREACIÓN**

1. **Primero:** Tarifas (Servicio Administración)
2. **Segundo:** Depósitos (Servicio Administración) 
3. **Tercero:** Camiones (Servicio Administración)
4. **Cuarto:** Clientes (Servicio Pedidos)
5. **Quinto:** Contenedores (Servicio Pedidos) - *Requieren clientes existentes*
6. **Sexto:** Solicitudes (Servicio Pedidos) - *Requieren clientes y contenedores*
7. **Último:** Rutas y Tramos (Servicio Logística) - *Requieren solicitudes*

> 💡 **Tip:** Los datos están corregidos para coincidir exactamente con las entidades Java del sistema.

## 🚛 **CAMIONES - Servicio Administración**

### **Endpoint:** `POST http://localhost:8082/camiones`

#### **Camión 1 - Mercedes Actros**
```json
{
  "dominio": "ABC123",
  "marca": "Mercedes-Benz", 
  "modelo": "Actros 2651",
  "capacidadPeso": 26000.00,
  "capacidadVolumen": 82.50,
  "consumoKm": 8.5,
  "costoKmBase": 45.00,
  "estado": "DISPONIBLE",
  "transportista": "Transportes del Norte SA",
  "telefono": "+54-11-4567-8901",
  "disponible": true
}
```

#### **Camión 2 - Volvo FH**
```json
{
  "dominio": "DEF456",
  "marca": "Volvo",
  "modelo": "FH 460", 
  "capacidadPeso": 18500.00,
  "capacidadVolumen": 65.75,
  "consumoKm": 9.2,
  "costoKmBase": 38.50,
  "estado": "DISPONIBLE",
  "transportista": "Logística Express Ltda",
  "telefono": "+54-11-2345-6789",
  "disponible": true
}
```

#### **Camión 3 - Scania R-Series**
```json
{
  "dominio": "GHI789",
  "marca": "Scania",
  "modelo": "R 450",
  "capacidadPeso": 22000.00, 
  "capacidadVolumen": 75.25,
  "consumoKm": 8.8,
  "costoKmBase": 42.00,
  "estado": "DISPONIBLE",
  "transportista": "Cargas Patagónicas SRL",
  "telefono": "+54-261-987-6543",
  "disponible": true
}
```

#### **Camión 4 - Iveco Stralis**
```json
{
  "dominio": "JKL012",
  "marca": "Iveco",
  "modelo": "Stralis Hi-Way",
  "capacidadPeso": 19500.00,
  "capacidadVolumen": 68.90,
  "consumoKm": 9.0,
  "costoKmBase": 40.00,
  "estado": "DISPONIBLE",
  "transportista": "Transporte Litoral SA", 
  "telefono": "+54-341-567-8912",
  "disponible": true
}
```

#### **Camión 5 - MAN TGX**
```json
{
  "dominio": "MNO345", 
  "marca": "MAN",
  "modelo": "TGX 28.480",
  "capacidadPeso": 28000.00,
  "capacidadVolumen": 95.00,
  "consumoKm": 8.3,
  "costoKmBase": 48.50,
  "estado": "DISPONIBLE",
  "transportista": "Mega Transportes SRL",
  "telefono": "+54-223-456-7890",
  "disponible": true
}
```

#### **Camión 6 - Ford Cargo**
```json
{
  "dominio": "PQR678",
  "marca": "Ford", 
  "modelo": "Cargo 2628E",
  "capacidadPeso": 16800.00,
  "capacidadVolumen": 58.40,
  "consumoKm": 10.5,
  "costoKmBase": 35.00,
  "estado": "DISPONIBLE",
  "transportista": "Distribuciones del Sur SA",
  "telefono": "+54-221-345-6789",
  "disponible": true
}
```

---

## 🏢 **DEPÓSITOS - Servicio Administración**

### **Endpoint:** `POST http://localhost:8082/depositos`

#### **Depósito 1 - CABA Central**
```json
{
  "nombre": "Depósito Central CABA",
  "direccion": "Av. Corrientes 1234, Ciudad Autónoma de Buenos Aires",
  "latitud": -34.6037,
  "longitud": -58.3816,
  "capacidadMax": 15000,
  "costoDiario": 250.00
}
```

#### **Depósito 2 - La Plata**
```json
{
  "nombre": "Centro Distribución La Plata",
  "direccion": "Ruta 36 Km 45, La Plata, Buenos Aires", 
  "latitud": -34.9214,
  "longitud": -57.9544,
  "capacidadMax": 12000,
  "costoDiario": 180.00
}
```

#### **Depósito 3 - Rosario**
```json
{
  "nombre": "Terminal Logística Rosario",
  "direccion": "Av. de Circunvalación 8500, Rosario, Santa Fe",
  "latitud": -32.9442,
  "longitud": -60.6505,
  "capacidadMax": 20000,
  "costoDiario": 320.00
}
```

#### **Depósito 4 - Córdoba**
```json
{
  "nombre": "Hub Córdoba Norte", 
  "direccion": "Ruta Nacional 9 Km 695, Córdoba",
  "latitud": -31.3734,
  "longitud": -64.2092,
  "capacidadMax": 18000,
  "costoDiario": 290.00
}
```

---

## 💰 **TARIFAS - Servicio Administración**

### **Endpoint:** `POST http://localhost:8082/tarifas`

#### **Tarifa 1 - Estándar**
```json
{
  "nombre": "Tarifa Estándar", 
  "descripcion": "Tarifa base para servicios regulares de transporte",
  "valorBase": 85.50,
  "tipoCalculo": "POR_KM"
}
```

#### **Tarifa 2 - Express**
```json
{
  "nombre": "Tarifa Express",
  "descripcion": "Tarifa premium para entregas urgentes (24-48hs)",
  "valorBase": 125.75,
  "tipoCalculo": "POR_KM_EXPRESS"
}
```

#### **Tarifa 3 - Volumen Alto**
```json
{
  "nombre": "Tarifa Volumen Alto",
  "descripcion": "Tarifa especial para cargas de gran volumen", 
  "valorBase": 95.25,
  "tipoCalculo": "POR_VOLUMEN"
}
```

---

## 👥 **CLIENTES - Servicio Pedidos**

### **Endpoint:** `POST http://localhost:8084/clientes`

#### **Cliente 1 - Empresa Comercial**
```json
{
  "nombre": "María Elena",
  "apellido": "González",
  "email": "maria.gonzalez@comercialdelplata.com",
  "telefono": "+54-221-445-7890",
  "direccion": "Av. 7 #1234, La Plata, Buenos Aires"
}
```

#### **Cliente 2 - Distribuidor Industrial**
```json
{
  "nombre": "Carlos Roberto", 
  "apellido": "Fernández",
  "email": "carlos.fernandez@industriasarg.com",
  "telefono": "+54-341-567-8901",
  "direccion": "Parque Industrial Norte, Lote 45, Rosario, Santa Fe"
}
```

#### **Cliente 3 - Retail Chain**
```json
{
  "nombre": "Ana Victoria",
  "apellido": "Rodríguez", 
  "email": "ana.rodriguez@supermercadossur.com",
  "telefono": "+54-11-5555-0123",
  "direccion": "Av. Rivadavia 5678, Ciudad Autónoma de Buenos Aires"
}
```

#### **Cliente 4 - Importador**
```json
{
  "nombre": "José Luis",
  "apellido": "Martínez",
  "email": "jl.martinez@importacionesltd.com",
  "telefono": "+54-351-789-0123", 
  "direccion": "Zona Franca Córdoba, Módulo 12, Córdoba"
}
```

#### **Cliente 5 - E-commerce**
```json
{
  "nombre": "Lucía Beatriz",
  "apellido": "Silva",
  "email": "lucia.silva@ecommerce360.com",
  "telefono": "+54-223-456-7891",
  "direccion": "Parque Tecnológico del Sur, Mar del Plata, Buenos Aires"
}
```

---

## 📦 **CONTENEDORES - Servicio Pedidos**

### **Endpoint:** `POST http://localhost:8084/contenedores`

> ⚠️ **IMPORTANTE:** Los contenedores requieren un `clienteId` válido. Debe crear primero los clientes antes de crear contenedores.

#### **Contenedor 1 - Productos Electrónicos** (Cliente ID: 1)
```json
{
  "codigo": "CONT001234",
  "peso": 2850.75,
  "volumen": 35.2,
  "estado": "REGISTRADO",
  "descripcion": "Equipos electrónicos - Notebooks y tablets para retail",
  "cliente": {"id": 1},
  "direccionOrigen": "Depósito Central CABA, Av. Corrientes 1234",
  "latitudOrigen": -34.6037,
  "longitudOrigen": -58.3816,
  "direccionDestino": "Centro Comercial La Plata, Av. 7 #1234",
  "latitudDestino": -34.9214,
  "longitudDestino": -57.9544
}
```

#### **Contenedor 2 - Alimentos** (Cliente ID: 2)
```json
{
  "codigo": "CONT002468", 
  "peso": 4250.50,
  "volumen": 28.8,
  "estado": "REGISTRADO",
  "descripcion": "Productos alimenticios congelados para supermercados",
  "cliente": {"id": 2},
  "direccionOrigen": "Terminal Logística Rosario, Av. Circunvalación 8500",
  "latitudOrigen": -32.9442,
  "longitudOrigen": -60.6505,
  "direccionDestino": "Hub Córdoba Norte, Ruta Nacional 9 Km 695",
  "latitudDestino": -31.3734,
  "longitudDestino": -64.2092
}
```

#### **Contenedor 3 - Textiles** (Cliente ID: 3)
```json
{
  "codigo": "CONT003579", 
  "peso": 1850.25,
  "volumen": 45.6,
  "estado": "REGISTRADO",
  "descripcion": "Indumentaria y textiles para tiendas de ropa",
  "cliente": {"id": 3},
  "direccionOrigen": "Depósito Central CABA, Av. Corrientes 1234",
  "latitudOrigen": -34.6037,
  "longitudOrigen": -58.3816,
  "direccionDestino": "Shopping Los Gallegos, Mar del Plata",
  "latitudDestino": -38.0055,
  "longitudDestino": -57.5426
}
```

#### **Contenedor 4 - Materiales Construcción** (Cliente ID: 4)
```json
{
  "codigo": "CONT004680",
  "peso": 8750.00,
  "volumen": 22.4,
  "estado": "REGISTRADO",
  "descripcion": "Materiales de construcción - Herramientas y equipos",
  "cliente": {"id": 4},
  "direccionOrigen": "Hub Córdoba Norte, Ruta Nacional 9 Km 695",
  "latitudOrigen": -31.3734,
  "longitudOrigen": -64.2092,
  "direccionDestino": "Centro Distribución La Plata, Ruta 36 Km 45",
  "latitudDestino": -34.9214,
  "longitudDestino": -57.9544
}
```

#### **Contenedor 5 - Farmacéuticos** (Cliente ID: 5)
```json
{
  "codigo": "CONT005791",
  "peso": 950.50,
  "volumen": 12.8,
  "estado": "REGISTRADO",
  "descripcion": "Productos farmacéuticos - Medicamentos y suplementos",
  "cliente": {"id": 5},
  "direccionOrigen": "Terminal Logística Rosario, Av. Circunvalación 8500",
  "latitudOrigen": -32.9442,
  "longitudOrigen": -60.6505,
  "direccionDestino": "Depósito Central CABA, Av. Corrientes 1234",
  "latitudDestino": -34.6037,
  "longitudDestino": -58.3816
}
```

---

## 📋 **SOLICITUDES - Servicio Pedidos**

### **Endpoint:** `POST http://localhost:8084/solicitudes`

#### **Solicitud 1 - Electrónicos CABA → La Plata**
```json
{
  "clienteId": 1,
  "fechaRetiro": "2025-11-10T08:00:00",
  "fechaEntregaEstimada": "2025-11-10T18:00:00", 
  "direccionOrigen": "Depósito Central CABA, Av. Corrientes 1234",
  "direccionDestino": "Centro Comercial La Plata, Av. 7 #1234",
  "observaciones": "Entrega urgente - Productos electrónicos frágiles - Manejar con cuidado",
  "contenedores": [
    {
      "numero": "CONT001234",
      "tipo": "ESTANDAR", 
      "peso": 2850.75,
      "volumen": 35.2
    }
  ]
}
```

#### **Solicitud 2 - Alimentos Rosario → Córdoba**
```json
{
  "clienteId": 2,
  "fechaRetiro": "2025-11-11T06:00:00",
  "fechaEntregaEstimada": "2025-11-11T16:00:00",
  "direccionOrigen": "Terminal Logística Rosario, Av. Circunvalación 8500",
  "direccionDestino": "Supermercado Central Córdoba, Av. Colón 2500",
  "observaciones": "Cadena de frío - Productos congelados - Temperatura: -18°C",
  "contenedores": [
    {
      "numero": "CONT002468",
      "tipo": "REFRIGERADO",
      "peso": 4250.50, 
      "volumen": 28.8
    }
  ]
}
```

#### **Solicitud 3 - Textiles CABA → Mar del Plata**
```json
{
  "clienteId": 3,
  "fechaRetiro": "2025-11-12T09:00:00",
  "fechaEntregaEstimada": "2025-11-13T15:00:00",
  "direccionOrigen": "Depósito Textil CABA, Av. Warnes 1500", 
  "direccionDestino": "Shopping Los Gallegos, Mar del Plata",
  "observaciones": "Temporada verano - Productos de moda - Entrega en horario comercial",
  "contenedores": [
    {
      "numero": "CONT003579",
      "tipo": "ESTANDAR",
      "peso": 1850.25,
      "volumen": 45.6
    }
  ]
}
```

---

## 🗺️ **RUTAS - Servicio Logística**

### **Endpoint:** `POST http://localhost:8083/api/rutas`

#### **Ruta 1 - Para Solicitud de Electrónicos**
```json
{
  "solicitudId": 1,
  "cantidadTramos": 1,
  "cantidadDepositos": 2,
  "estado": "PLANIFICADA",
  "costoTotalAproximado": 4850.50
}
```

#### **Ruta 2 - Para Solicitud de Alimentos**
```json
{
  "solicitudId": 2,
  "cantidadTramos": 2, 
  "cantidadDepositos": 3,
  "estado": "PLANIFICADA",
  "costoTotalAproximado": 8750.75
}
```

#### **Ruta 3 - Para Solicitud de Textiles**
```json
{
  "solicitudId": 3,
  "cantidadTramos": 3,
  "cantidadDepositos": 2,
  "estado": "PLANIFICADA",
  "costoTotalAproximado": 12500.25
}
```

---

## 🛣️ **TRAMOS - Servicio Logística**

### **Endpoint:** `POST http://localhost:8083/api/rutas/{ruta_id}/tramos`

#### **Tramo 1 - CABA → La Plata**
```json
{
  "origen": "Depósito Central CABA",
  "destino": "Centro Distribución La Plata", 
  "distanciaKm": 65.5,
  "tiempoEstimadoHoras": 1.5,
  "costo": 4850.50,
  "orden": 1
}
```

#### **Tramo 2 - Rosario → Córdoba (Parte 1)**
```json
{
  "origen": "Terminal Logística Rosario",
  "destino": "Parada Intermedia Villa María",
  "distanciaKm": 180.2,
  "tiempoEstimadoHoras": 2.8, 
  "costo": 4250.25,
  "orden": 1
}
```

#### **Tramo 3 - Rosario → Córdoba (Parte 2)**
```json
{
  "origen": "Parada Intermedia Villa María", 
  "destino": "Hub Córdoba Norte",
  "distanciaKm": 95.8,
  "tiempoEstimadoHoras": 1.5,
  "costo": 4500.50,
  "orden": 2
}
```

---

## 🔧 **PARÁMETROS GLOBALES - Servicio Administración**

### **Endpoint:** `POST http://localhost:8082/parametros`

#### **Parámetro 1 - Precio Combustible**
```json
{
  "clave": "PRECIO_COMBUSTIBLE_LITRO",
  "valor": 850.50,
  "descripcion": "Precio promedio del combustible por litro en pesos argentinos",
  "activo": true
}
```

#### **Parámetro 2 - Factor Seguro**
```json
{
  "clave": "FACTOR_SEGURO_TRANSPORTE", 
  "valor": 0.025,
  "descripcion": "Factor de seguro aplicado sobre el valor declarado de la carga",
  "activo": true
}
```

#### **Parámetro 3 - Tiempo Carga/Descarga**
```json
{
  "clave": "TIEMPO_CARGA_DESCARGA_HORAS",
  "valor": 1.5,
  "descripcion": "Tiempo promedio estimado para operaciones de carga y descarga",
  "activo": true
}
```

---

## 🧪 **Colección Postman - Environment Variables**

### **Configuración de Environment:**
```json
{
  "name": "TPI Backend Local",
  "values": [
    {
      "key": "base_url",
      "value": "http://localhost",
      "enabled": true
    },
    {
      "key": "gateway_port", 
      "value": "8080",
      "enabled": true
    },
    {
      "key": "admin_port",
      "value": "8082", 
      "enabled": true
    },
    {
      "key": "logistics_port",
      "value": "8083",
      "enabled": true
    },
    {
      "key": "orders_port",
      "value": "8084",
      "enabled": true
    },
    {
      "key": "geo_port",
      "value": "8081", 
      "enabled": true
    }
  ]
}
```

### **URLs con Variables:**
```
# Administración
{{base_url}}:{{admin_port}}/camiones
{{base_url}}:{{admin_port}}/depositos  
{{base_url}}:{{admin_port}}/tarifas

# Pedidos
{{base_url}}:{{orders_port}}/clientes
{{base_url}}:{{orders_port}}/contenedores
{{base_url}}:{{orders_port}}/solicitudes

# Logística
{{base_url}}:{{logistics_port}}/api/rutas
{{base_url}}:{{logistics_port}}/api/rutas/1/tramos

# Gateway (Alternativo)
{{base_url}}:{{gateway_port}}/api/admin/camiones
{{base_url}}:{{gateway_port}}/api/pedidos/clientes
{{base_url}}:{{gateway_port}}/api/logistica/rutas
```

---

## ⚡ **Scripts de PowerShell para Carga Masiva**

### **Script para Crear Múltiples Camiones:**
```powershell
# crear_camiones.ps1
$camiones = @(
    @{dominio="ABC123"; marca="Mercedes-Benz"; modelo="Actros 2651"},
    @{dominio="DEF456"; marca="Volvo"; modelo="FH 460"},
    @{dominio="GHI789"; marca="Scania"; modelo="R 450"}
)

foreach ($camion in $camiones) {
    $body = $camion | ConvertTo-Json
    Invoke-RestMethod -Uri "http://localhost:8082/camiones" -Method POST -Body $body -ContentType "application/json"
    Write-Host "Camión $($camion.dominio) creado ✅"
}
```

### **Script para Verificar Datos:**
```powershell
# verificar_datos.ps1
Write-Host "=== VERIFICANDO DATOS DEL SISTEMA ===" -ForegroundColor Green

Write-Host "`nCamiones:" -ForegroundColor Yellow
$camiones = Invoke-RestMethod -Uri "http://localhost:8082/camiones"
Write-Host "Total: $($camiones.Count)" -ForegroundColor Cyan

Write-Host "`nClientes:" -ForegroundColor Yellow  
$clientes = Invoke-RestMethod -Uri "http://localhost:8084/clientes"
Write-Host "Total: $($clientes.Count)" -ForegroundColor Cyan

Write-Host "`nRutas:" -ForegroundColor Yellow
try {
    $rutas = Invoke-RestMethod -Uri "http://localhost:8083/api/rutas"
    Write-Host "Total: $($rutas.Count)" -ForegroundColor Cyan
} catch {
    Write-Host "Error al obtener rutas: $($_.Exception.Message)" -ForegroundColor Red
}
```

---

**📦 Datos preparados para:** Postman, Insomnia, PowerShell, curl  
**🎯 Cobertura:** Todos los microservicios y entidades principales  
**📅 Última actualización:** Noviembre 2025
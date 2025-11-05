# 🏗️ Diagramas de Arquitectura - Sistema TPI Backend

## 🎯 **Arquitectura de Alto Nivel**

```mermaid
graph TB
    Client[👤 Cliente/Frontend] --> Gateway[🌐 API Gateway<br/>:8080]
    
    Gateway --> Admin[🏢 Servicio Administración<br/>:8082]
    Gateway --> Logistics[📦 Servicio Logística<br/>:8083]  
    Gateway --> Orders[📋 Servicio Pedidos<br/>:8084]
    Gateway --> Geo[🗺️ Servicio Geolocalización<br/>:8081]
    
    Admin --> DB1[(🗃️ administracion_db)]
    Logistics --> DB2[(🗃️ logistica_db)]
    Orders --> DB3[(🗃️ pedidos_db)]
    
    DB1 -.-> Postgres[🐘 PostgreSQL<br/>:5432]
    DB2 -.-> Postgres
    DB3 -.-> Postgres
    
    Geo --> GoogleAPI[🌍 Google Maps API]
    
    classDef service fill:#e1f5fe,stroke:#0277bd,stroke-width:2px
    classDef database fill:#f3e5f5,stroke:#7b1fa2,stroke-width:2px
    classDef external fill:#fff3e0,stroke:#f57c00,stroke-width:2px
    
    class Gateway,Admin,Logistics,Orders,Geo service
    class DB1,DB2,DB3,Postgres database  
    class Client,GoogleAPI external
```

---

## 🔄 **Flujo de Comunicación Entre Servicios**

```mermaid
sequenceDiagram
    participant C as 👤 Cliente
    participant G as 🌐 Gateway
    participant P as 📋 Pedidos
    participant A as 🏢 Administración
    participant L as 📦 Logística
    participant Geo as 🗺️ Geolocalización
    
    C->>G: 1. Crear solicitud de transporte
    G->>P: 2. POST /api/pedidos/solicitudes
    P->>P: 3. Guardar solicitud en BD
    
    P->>G: 4. Solicitar camiones disponibles
    G->>A: 5. GET /api/admin/camiones?disponible=true
    A-->>G: 6. Lista de camiones
    G-->>P: 7. Camiones disponibles
    
    P->>G: 8. Crear ruta para solicitud
    G->>L: 9. POST /api/logistica/rutas
    L->>L: 10. Crear ruta en BD
    
    L->>G: 11. Calcular distancia
    G->>Geo: 12. GET /api/geo/distancia
    Geo->>Geo: 13. Consultar Google Maps
    Geo-->>G: 14. Distancia calculada
    G-->>L: 15. Distancia
    
    L->>L: 16. Actualizar costos de ruta
    L-->>G: 17. Ruta creada
    G-->>P: 18. Confirmación
    P-->>G: 19. Solicitud procesada
    G-->>C: 20. ✅ Pedido confirmado
```

---

## 🏗️ **Arquitectura de Microservicios Detallada**

```mermaid
graph LR
    subgraph "🖥️ Capa de Presentación"
        Frontend[Frontend App]
        Mobile[Mobile App]  
        API_Client[API Clients]
    end
    
    subgraph "🌐 Capa de Gateway"
        Gateway[API Gateway<br/>Spring Cloud Gateway<br/>:8080]
    end
    
    subgraph "🔧 Capa de Servicios"
        subgraph "🏢 Administración"
            AdminAPI[REST API<br/>:8082]
            AdminLogic[Business Logic]
            AdminRepo[Repository Layer]
        end
        
        subgraph "📦 Logística"  
            LogisticAPI[REST API<br/>:8083]
            LogisticLogic[Business Logic]
            LogisticRepo[Repository Layer]
        end
        
        subgraph "📋 Pedidos"
            OrderAPI[REST API<br/>:8084] 
            OrderLogic[Business Logic]
            OrderRepo[Repository Layer]
        end
        
        subgraph "🗺️ Geolocalización"
            GeoAPI[REST API<br/>:8081]
            GeoLogic[Business Logic]
        end
    end
    
    subgraph "🗄️ Capa de Datos"
        subgraph "PostgreSQL Container"
            DB1[(administracion_db)]
            DB2[(logistica_db)] 
            DB3[(pedidos_db)]
        end
    end
    
    subgraph "🌍 Servicios Externos"
        GoogleMaps[Google Maps API]
    end
    
    Frontend --> Gateway
    Mobile --> Gateway
    API_Client --> Gateway
    
    Gateway --> AdminAPI
    Gateway --> LogisticAPI  
    Gateway --> OrderAPI
    Gateway --> GeoAPI
    
    AdminAPI --> AdminLogic --> AdminRepo --> DB1
    LogisticAPI --> LogisticLogic --> LogisticRepo --> DB2
    OrderAPI --> OrderLogic --> OrderRepo --> DB3
    GeoAPI --> GeoLogic --> GoogleMaps
```

---

## 🗃️ **Modelo de Datos - Vista General**

```mermaid
erDiagram
    %% Base Administración
    CAMIONES {
        int id_camion PK
        string dominio UK
        string marca
        string modelo
        decimal capacidad_peso
        decimal capacidad_volumen
        decimal consumo_km
        string transportista
        string telefono
        boolean disponible
        int id_tarifa FK
    }
    
    DEPOSITOS {
        int id_deposito PK
        string nombre
        string direccion
        decimal latitud
        decimal longitud
        decimal capacidad_maxima
        boolean activo
    }
    
    TARIFAS {
        int id_tarifa PK
        string nombre
        string descripcion
        decimal valor_base
    }
    
    PARAMETROS_GLOBALES {
        int id_parametro PK
        string clave UK
        string valor
        string descripcion
        string tipo
    }
    
    %% Base Pedidos
    CLIENTES {
        int id_cliente PK
        string nombre
        string apellido
        string email UK
        string telefono
        string direccion
        string keycloak_id
        boolean activo
    }
    
    CONTENEDORES {
        int id_contenedor PK
        string numero UK
        string tipo
        decimal peso
        decimal volumen
        string descripcion_carga
        decimal valor_declarado
        boolean disponible
    }
    
    SOLICITUDES {
        int id_solicitud PK
        int cliente_id FK
        datetime fecha_solicitud
        datetime fecha_retiro
        datetime fecha_entrega_estimada
        string direccion_origen
        string direccion_destino
        string estado
        string observaciones
        decimal costo_total
    }
    
    %% Base Logística
    RUTAS {
        int id_ruta PK
        int solicitud_id FK
        int cantidad_tramos
        int cantidad_depositos
        string estado
        decimal costo_total_aproximado
        decimal costo_total_real
    }
    
    TRAMOS {
        int id_tramo PK
        int ruta_id FK
        string origen
        string destino
        decimal distancia_km
        decimal tiempo_estimado_horas
        decimal costo
        int orden
    }
    
    %% Relaciones
    CAMIONES ||--o{ TARIFAS : "tiene"
    CLIENTES ||--o{ SOLICITUDES : "realiza"
    SOLICITUDES ||--o{ RUTAS : "genera"
    RUTAS ||--o{ TRAMOS : "contiene"
```

---

## 🔄 **Estados y Flujos de Negocio**

```mermaid
stateDiagram-v2
    [*] --> SOLICITUD_CREADA: Cliente crea solicitud
    
    SOLICITUD_CREADA --> EVALUANDO: Sistema evalúa factibilidad
    EVALUANDO --> APROBADA: Camiones disponibles
    EVALUANDO --> RECHAZADA: Sin recursos disponibles
    
    APROBADA --> RUTA_PLANIFICADA: Se crea ruta óptima
    RUTA_PLANIFICADA --> CAMION_ASIGNADO: Se asigna camión
    CAMION_ASIGNADO --> EN_TRANSITO: Camión inicia viaje
    
    EN_TRANSITO --> COMPLETADA: Entrega exitosa
    EN_TRANSITO --> INCIDENCIA: Problema durante transporte
    
    INCIDENCIA --> RESOLUCION: Se gestiona problema
    RESOLUCION --> EN_TRANSITO: Continúa viaje
    RESOLUCION --> CANCELADA: No se puede resolver
    
    RECHAZADA --> [*]
    COMPLETADA --> [*] 
    CANCELADA --> [*]
```

---

## 🐳 **Arquitectura de Contenedores Docker**

```mermaid
graph TB
    subgraph "🐳 Docker Host"
        subgraph "🌐 Network: backend"
            
            subgraph "📊 Database Layer"
                PostgresContainer[🐘 tpi-postgres<br/>postgres:15-alpine<br/>Port: 5432]
            end
            
            subgraph "🚀 Application Layer"
                GatewayContainer[🌐 tpi-api-gateway<br/>Port: 8080<br/>Gateway de entrada]
                AdminContainer[🏢 tpi-servicio-administracion<br/>Port: 8082<br/>Gestión de flota]
                LogisticsContainer[📦 tpi-servicio-logistica<br/>Port: 8083<br/>Rutas y tramos] 
                OrdersContainer[📋 tpi-servicio-pedidos<br/>Port: 8084<br/>Clientes y solicitudes]
                GeoContainer[🗺️ tpi-servicio-geolocalizacion<br/>Port: 8081<br/>APIs geográficas]
            end
        end
        
        subgraph "💾 Volumes"
            DataVolume[./database/data<br/>Datos PostgreSQL]
            InitVolume[./database/init-scripts<br/>Scripts de inicialización]
        end
    end
    
    subgraph "🌍 External"
        GoogleAPI[Google Maps API]
        Client[👤 Cliente Web/Mobile]
    end
    
    %% Connections
    Client --> GatewayContainer
    
    GatewayContainer --> AdminContainer
    GatewayContainer --> LogisticsContainer  
    GatewayContainer --> OrdersContainer
    GatewayContainer --> GeoContainer
    
    AdminContainer --> PostgresContainer
    LogisticsContainer --> PostgresContainer
    OrdersContainer --> PostgresContainer
    
    GeoContainer --> GoogleAPI
    
    PostgresContainer --> DataVolume
    PostgresContainer --> InitVolume
    
    %% Styling
    classDef container fill:#e3f2fd,stroke:#1976d2,stroke-width:2px
    classDef database fill:#f3e5f5,stroke:#7b1fa2,stroke-width:2px
    classDef volume fill:#e8f5e8,stroke:#388e3c,stroke-width:2px
    classDef external fill:#fff3e0,stroke:#f57c00,stroke-width:2px
    
    class GatewayContainer,AdminContainer,LogisticsContainer,OrdersContainer,GeoContainer container
    class PostgresContainer database
    class DataVolume,InitVolume volume
    class GoogleAPI,Client external
```

---

## 📡 **Matriz de Comunicación Entre Servicios**

```mermaid
graph LR
    subgraph "🔄 Comunicación Directa"
        API[API Gateway] --> |HTTP REST| ADMIN[Administración]
        API --> |HTTP REST| LOGISTICS[Logística]
        API --> |HTTP REST| ORDERS[Pedidos]
        API --> |HTTP REST| GEO[Geolocalización]
    end
    
    subgraph "🔗 Comunicación Indirecta"
        ORDERS -.-> |Solicita camiones| ADMIN
        LOGISTICS -.-> |Consulta depósitos| ADMIN
        LOGISTICS -.-> |Calcula distancias| GEO
        LOGISTICS -.-> |Asocia solicitudes| ORDERS
    end
    
    subgraph "🗄️ Acceso a Datos"
        ADMIN --> |JDBC| DB1[(administracion_db)]
        LOGISTICS --> |JDBC| DB2[(logistica_db)]
        ORDERS --> |JDBC| DB3[(pedidos_db)]
    end
    
    subgraph "🌍 APIs Externas"
        GEO --> |HTTPS| GOOGLE[Google Maps API]
    end
    
    style API fill:#ff9800,color:white
    style ADMIN fill:#2196f3,color:white  
    style LOGISTICS fill:#4caf50,color:white
    style ORDERS fill:#9c27b0,color:white
    style GEO fill:#f44336,color:white
```

---

## 🔐 **Arquitectura de Seguridad (Futura)**

```mermaid
graph TB
    Client[👤 Cliente] --> Auth[🔐 Keycloak<br/>Authentication Server]
    Auth --> JWT[🎫 JWT Token]
    
    JWT --> Gateway[🌐 API Gateway<br/>+ Security Filter]
    
    Gateway --> |Authorized Request| AdminSvc[🏢 Administración]
    Gateway --> |Authorized Request| LogisticsSvc[📦 Logística] 
    Gateway --> |Authorized Request| OrdersSvc[📋 Pedidos]
    Gateway --> |Authorized Request| GeoSvc[🗺️ Geolocalización]
    
    subgraph "🛡️ Security Layers"
        Gateway --> RateLimit[⚡ Rate Limiting]
        Gateway --> CORS[🌐 CORS Policy]
        Gateway --> Validation[✅ Input Validation]
    end
    
    subgraph "🔍 Monitoring"
        Gateway --> Metrics[📊 Metrics Collection]
        Gateway --> Logging[📝 Centralized Logging]
        Gateway --> Tracing[🔍 Distributed Tracing]
    end
```

---

## 📈 **Arquitectura de Escalabilidad**

```mermaid
graph TB
    LB[⚖️ Load Balancer] --> Gateway1[Gateway Instance 1]
    LB --> Gateway2[Gateway Instance 2]
    LB --> Gateway3[Gateway Instance 3]
    
    Gateway1 --> AdminCluster[🏢 Admin Service Cluster]
    Gateway2 --> LogisticsCluster[📦 Logistics Service Cluster]
    Gateway3 --> OrdersCluster[📋 Orders Service Cluster]
    
    subgraph "🏢 Admin Cluster"
        Admin1[Admin-1]
        Admin2[Admin-2]
        Admin3[Admin-3]
    end
    
    subgraph "📦 Logistics Cluster"  
        Logistics1[Logistics-1]
        Logistics2[Logistics-2]
    end
    
    subgraph "📋 Orders Cluster"
        Orders1[Orders-1]  
        Orders2[Orders-2]
        Orders3[Orders-3]
    end
    
    AdminCluster --> MasterDB[(🗄️ PostgreSQL Master)]
    LogisticsCluster --> MasterDB
    OrdersCluster --> MasterDB
    
    MasterDB --> ReplicaDB1[(📖 Read Replica 1)]
    MasterDB --> ReplicaDB2[(📖 Read Replica 2)]
```

---

## 🔄 **Pipeline de CI/CD (Conceptual)**

```mermaid
graph LR
    DEV[👨‍💻 Developer] --> GIT[📝 Git Repository]
    GIT --> |Push/PR| CI[🔄 CI Pipeline]
    
    subgraph "🔨 CI Steps"
        CI --> BUILD[🏗️ Maven Build]
        BUILD --> TEST[🧪 Unit Tests]
        TEST --> SCAN[🔍 Security Scan]
        SCAN --> DOCKER[🐳 Docker Build]
    end
    
    DOCKER --> REGISTRY[📦 Container Registry]
    REGISTRY --> |Deploy| CD[🚀 CD Pipeline]
    
    subgraph "🌍 Environments"
        CD --> DEV_ENV[🔧 Development]
        CD --> STAGE_ENV[🎯 Staging]  
        CD --> PROD_ENV[🏭 Production]
    end
    
    subgraph "📊 Monitoring"
        PROD_ENV --> METRICS[📈 Metrics]
        PROD_ENV --> LOGS[📝 Logs]
        PROD_ENV --> ALERTS[🚨 Alerts]
    end
```

---

**📐 Diagramas generados con:** Mermaid  
**📅 Última actualización:** Noviembre 2025  
**🎨 Estilo:** Material Design + TPI Branding
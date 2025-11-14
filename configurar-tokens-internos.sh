#!/bin/bash
# Script para copiar la configuración de TokenProvider a todos los servicios
# Ejecutar después de configurar el service account en Keycloak

echo "=== CONFIGURANDO COMUNICACIÓN INTERNA CON TOKENS AUTOMÁTICOS ==="

# Servicios que necesitan comunicación interna
SERVICIOS=("ServicioAdministracion" "ServicioLogistica" "ServicioGeolocalizacion" "ApiGateway")

for servicio in "${SERVICIOS[@]}"; do
    echo "Configurando $servicio..."
    
    # 1. Copiar TokenProvider.java
    echo "  - Copiando TokenProvider.java"
    cp ServicioPedidos/src/main/java/utnfc/isi/back/sim/config/TokenProvider.java \
       $servicio/src/main/java/utnfc/isi/back/sim/config/TokenProvider.java
    
    # 2. Verificar si existe RestTemplateConfig y actualizarlo
    echo "  - Actualizando RestTemplateConfig.java"
    if [ -f "$servicio/src/main/java/utnfc/isi/back/sim/config/RestTemplateConfig.java" ]; then
        # Si existe, reemplazarlo con la versión con interceptor
        cp ServicioPedidos/src/main/java/utnfc/isi/back/sim/config/RestTemplateConfig.java \
           $servicio/src/main/java/utnfc/isi/back/sim/config/RestTemplateConfig.java
    else
        # Si no existe, crearlo
        cp ServicioPedidos/src/main/java/utnfc/isi/back/sim/config/RestTemplateConfig.java \
           $servicio/src/main/java/utnfc/isi/back/sim/config/RestTemplateConfig.java
    fi
    
    # 3. Añadir configuración a application-docker.properties
    echo "  - Añadiendo propiedades de configuración"
    if ! grep -q "keycloak.service-account.client-id" $servicio/src/main/resources/application-docker.properties 2>/dev/null; then
        echo "" >> $servicio/src/main/resources/application-docker.properties
        echo "# === CONFIGURACIÓN SERVICE ACCOUNT PARA COMUNICACIÓN INTERNA ===" >> $servicio/src/main/resources/application-docker.properties
        echo "keycloak.service-account.client-id=tpi-service-account" >> $servicio/src/main/resources/application-docker.properties
        echo "keycloak.service-account.client-secret=AZ9JEotcdWWQKBX2ygFaX3fvuusosS6H" >> $servicio/src/main/resources/application-docker.properties
        echo "keycloak.service-account.token-uri=http://tpi-keycloak:8080/realms/tpi-backend/protocol/openid-connect/token" >> $servicio/src/main/resources/application-docker.properties
    fi
    
    echo "  ✅ $servicio configurado"
done

echo ""
echo "🎉 CONFIGURACIÓN COMPLETADA"
echo ""
echo "📋 RESUMEN:"
echo "  ✅ TokenProvider copiado a todos los servicios"
echo "  ✅ RestTemplateConfig actualizado con interceptor"
echo "  ✅ Properties de service account añadidas"
echo ""
echo "⚠️  PENDIENTE:"
echo "  1. Crear 'tpi-service-account' en Keycloak Admin Console"
echo "  2. Configurar Client Secret: AZ9JEotcdWWQKBX2ygFaX3fvuusosS6H"
echo "  3. Asignar roles: realm-management → view-clients, view-users"
echo "  4. Reconstruir contenedores Docker"
echo ""
echo "🚀 DESPUÉS DEL SETUP:"
echo "  - Todos los servicios podrán comunicarse internamente con tokens automáticos"
echo "  - Los RestTemplate añadirán automáticamente 'Authorization: Bearer {token}'"
echo "  - Renovación automática de tokens cada expiración"
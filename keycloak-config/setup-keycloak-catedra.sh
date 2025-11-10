#!/bin/bash

# Script para configurar Keycloak automáticamente según especificaciones de la cátedra
# Configuración: realm tpi-backend, cliente tpi-backend-client, roles cliente/admin

echo "Esperando que Keycloak esté disponible..."
until curl -f http://tpi-keycloak:8080/; do
    sleep 5
done

echo "Configurando Keycloak según especificaciones de cátedra..."

# Variables de configuración según documento de cátedra
KEYCLOAK_URL="http://tpi-keycloak:8080"
REALM_NAME="tpi-backend"
CLIENT_ID="tpi-backend-client"

# Token de administración
ADMIN_TOKEN=$(curl -s -X POST "$KEYCLOAK_URL/realms/master/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin" \
  -d "password=admin123" \
  -d "grant_type=password" \
  -d "client_id=admin-cli" | jq -r '.access_token')

echo "Token obtenido: ${ADMIN_TOKEN:0:20}..."

# 1. Crear realm tpi-backend
echo "Creando realm $REALM_NAME..."
curl -s -X POST "$KEYCLOAK_URL/admin/realms" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "realm": "'$REALM_NAME'",
    "enabled": true,
    "displayName": "TPI Backend Realm",
    "accessTokenLifespan": 3600
  }'

# 2. Crear cliente tpi-backend-client (público para permitir autenticación directa)
echo "Creando cliente $CLIENT_ID..."
curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/clients" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "'$CLIENT_ID'",
    "enabled": true,
    "publicClient": true,
    "directAccessGrantsEnabled": true,
    "serviceAccountsEnabled": false,
    "standardFlowEnabled": true,
    "implicitFlowEnabled": false,
    "redirectUris": ["*"],
    "webOrigins": ["*"]
  }'

# 3. Crear roles del realm según especificaciones de cátedra + transportista
echo "Creando roles cliente, admin y transportista..."
curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/roles" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "cliente",
    "description": "Cliente del sistema que puede crear solicitudes"
  }'

curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/roles" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "admin",
    "description": "Administrador con acceso completo al sistema"
  }'

curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/roles" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "transportista",
    "description": "Transportista que gestiona rutas y logística"
  }'

# 4. Crear usuarios de prueba según especificaciones de cátedra + transportista
echo "Creando usuarios de prueba..."

# Cliente de prueba - cliente01
curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "cliente01",
    "email": "cliente01@test.com",
    "firstName": "Cliente",
    "lastName": "Uno",
    "enabled": true,
    "credentials": [{
      "type": "password",
      "value": "Clave123",
      "temporary": false
    }]
  }'

# Administrador de prueba - admin01
curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin01",
    "email": "admin01@test.com",
    "firstName": "Admin",
    "lastName": "Uno",
    "enabled": true,
    "credentials": [{
      "type": "password",
      "value": "Clave123",
      "temporary": false
    }]
  }'

# Transportista de prueba - transportista01
curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "transportista01",
    "email": "transportista01@test.com",
    "firstName": "Transportista",
    "lastName": "Uno",
    "enabled": true,
    "credentials": [{
      "type": "password",
      "value": "Clave123",
      "temporary": false
    }]
  }'

echo "Esperando que los usuarios sean creados..."
sleep 2

# 5. Asignar roles a usuarios
echo "Asignando roles a usuarios..."

# Obtener IDs de usuarios
USER_CLIENTE_ID=$(curl -s "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users?username=cliente01" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq -r '.[0].id')

USER_ADMIN_ID=$(curl -s "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users?username=admin01" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq -r '.[0].id')

USER_TRANSPORTISTA_ID=$(curl -s "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users?username=transportista01" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq -r '.[0].id')

# Obtener IDs de roles
ROLE_CLIENTE=$(curl -s "$KEYCLOAK_URL/admin/realms/$REALM_NAME/roles/cliente" \
  -H "Authorization: Bearer $ADMIN_TOKEN")

ROLE_ADMIN=$(curl -s "$KEYCLOAK_URL/admin/realms/$REALM_NAME/roles/admin" \
  -H "Authorization: Bearer $ADMIN_TOKEN")

ROLE_TRANSPORTISTA=$(curl -s "$KEYCLOAK_URL/admin/realms/$REALM_NAME/roles/transportista" \
  -H "Authorization: Bearer $ADMIN_TOKEN")

echo "Asignando rol 'cliente' al usuario cliente01..."
curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users/$USER_CLIENTE_ID/role-mappings/realm" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d "[$ROLE_CLIENTE]"

echo "Asignando rol 'admin' al usuario admin01..."
curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users/$USER_ADMIN_ID/role-mappings/realm" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d "[$ROLE_ADMIN]"

echo "Asignando rol 'transportista' al usuario transportista01..."
curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users/$USER_TRANSPORTISTA_ID/role-mappings/realm" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d "[$ROLE_TRANSPORTISTA]"

echo ""
echo "========================================="
echo "Configuración de Keycloak completada con 3 roles!"
echo "========================================="
echo "Realm: $REALM_NAME"
echo "Cliente: $CLIENT_ID"
echo "Puerto Keycloak: 8085 (adaptado para evitar conflicto con puerto 8081)"
echo ""
echo "Usuarios creados:"
echo "  - cliente01 / Clave123 (rol: cliente)"
echo "  - admin01 / Clave123 (rol: admin)"
echo "  - transportista01 / Clave123 (rol: transportista)"
echo ""
echo "URLs importantes:"
echo "  - Admin Console: http://localhost:8085/admin/"
echo "  - Realm: http://localhost:8085/realms/tpi-backend"
echo "  - Token endpoint: http://localhost:8085/realms/tpi-backend/protocol/openid-connect/token"
echo ""
echo "Comando para obtener token de cliente:"
echo "curl -X POST http://localhost:8085/realms/tpi-backend/protocol/openid-connect/token \\"
echo "  -H 'Content-Type: application/x-www-form-urlencoded' \\"
echo "  -d 'username=cliente01&password=Clave123&grant_type=password&client_id=tpi-backend-client'"
echo ""
echo "Comando para obtener token de admin:"
echo "curl -X POST http://localhost:8085/realms/tpi-backend/protocol/openid-connect/token \\"
echo "  -H 'Content-Type: application/x-www-form-urlencoded' \\"
echo "  -d 'username=admin01&password=Clave123&grant_type=password&client_id=tpi-backend-client'"
echo ""
echo "Comando para obtener token de transportista:"
echo "curl -X POST http://localhost:8085/realms/tpi-backend/protocol/openid-connect/token \\"
echo "  -H 'Content-Type: application/x-www-form-urlencoded' \\"
echo "  -d 'username=transportista01&password=Clave123&grant_type=password&client_id=tpi-backend-client'"
echo "========================================="
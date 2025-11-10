#!/bin/bash

# Script para configurar Keycloak automáticamente según material de cátedra
# Configuración: realm tpi-backend, cliente tpi-backend-client, roles cliente/admin

echo "Esperando que Keycloak esté disponible..."
until curl -f http://keycloak:8080/; do
    sleep 5
done

echo "Configurando Keycloak según especificaciones de cátedra..."

# Variables de configuración según material cátedra
KEYCLOAK_URL="http://keycloak:8080"
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

# 1. Crear realm
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

# 2. Crear cliente público según especificaciones cátedra
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
    "redirectUris": ["http://localhost:8080/*"],
    "webOrigins": ["*"],
    "rootUrl": "http://localhost:8080"
  }'

# 3. Crear roles según material cátedra (cliente y admin)
echo "Creando roles..."
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

# 4. Crear usuarios según patrón de cátedra
echo "Creando usuarios de prueba según material cátedra..."

# Usuarios cliente (cliente01, cliente02, etc.)
for i in {01..03}; do
  curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
      "username": "cliente'$i'",
      "email": "cliente'$i'@example.com",
      "firstName": "Cliente",
      "lastName": "'$i'",
      "enabled": true,
      "credentials": [{
        "type": "password",
        "value": "Clave123",
        "temporary": false
      }]
    }'
done

# Usuarios admin (admin01, admin02)
for i in {01..02}; do
  curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
      "username": "admin'$i'",
      "email": "admin'$i'@example.com",
      "firstName": "Admin", 
      "lastName": "'$i'",
      "enabled": true,
      "credentials": [{
        "type": "password",
        "value": "Clave123",
        "temporary": false
      }]
    }'
done

# 5. Asignar roles a usuarios
echo "Asignando roles a usuarios..."

# Obtener IDs de usuarios
USER_CLIENTE_ID=$(curl -s "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users?username=cliente1" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq -r '.[0].id')

USER_OPERADOR_ID=$(curl -s "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users?username=operador1" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq -r '.[0].id')

USER_TRANSPORTISTA_ID=$(curl -s "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users?username=transportista1" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq -r '.[0].id')

# Obtener IDs de roles
ROLE_CLIENTE_ID=$(curl -s "$KEYCLOAK_URL/admin/realms/$REALM_NAME/roles/CLIENTE" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq -r '.id')

ROLE_OPERADOR_ID=$(curl -s "$KEYCLOAK_URL/admin/realms/$REALM_NAME/roles/OPERADOR" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq -r '.id')

ROLE_TRANSPORTISTA_ID=$(curl -s "$KEYCLOAK_URL/admin/realms/$REALM_NAME/roles/TRANSPORTISTA" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq -r '.id')

# Asignar rol CLIENTE al usuario cliente1
curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users/$USER_CLIENTE_ID/role-mappings/realm" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '[{
    "id": "'$ROLE_CLIENTE_ID'",
    "name": "CLIENTE"
  }]'

# Asignar rol OPERADOR al usuario operador1
curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users/$USER_OPERADOR_ID/role-mappings/realm" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '[{
    "id": "'$ROLE_OPERADOR_ID'",
    "name": "OPERADOR"
  }]'

# Asignar rol TRANSPORTISTA al usuario transportista1
curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/users/$USER_TRANSPORTISTA_ID/role-mappings/realm" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '[{
    "id": "'$ROLE_TRANSPORTISTA_ID'",
    "name": "TRANSPORTISTA"
  }]'

echo "Configuración de Keycloak completada!"
echo "Realm: $REALM_NAME"
echo "Cliente: $CLIENT_ID"
echo "Usuarios creados:"
echo "  - cliente1 / cliente123 (rol: CLIENTE)"
echo "  - operador1 / operador123 (rol: OPERADOR)"  
echo "  - transportista1 / transportista123 (rol: TRANSPORTISTA)"
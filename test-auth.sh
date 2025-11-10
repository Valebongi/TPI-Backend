#!/bin/bash

# Script para probar la autenticación Keycloak desde dentro de la red Docker

echo "=== PRUEBA DE INTEGRACIÓN KEYCLOAK + SERVICIO PEDIDOS ==="
echo ""

# URLs internas de Docker
KEYCLOAK_URL="http://tpi-keycloak:8080"
PEDIDOS_URL="http://tpi-servicio-pedidos:8080"
REALM_NAME="tpi-backend"
CLIENT_ID="tpi-backend-client"

echo "1. Obteniendo token para cliente01..."
CLIENTE_TOKEN=$(curl -s -X POST "$KEYCLOAK_URL/realms/$REALM_NAME/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=cliente01&password=Clave123&grant_type=password&client_id=$CLIENT_ID" | jq -r '.access_token')

if [ "$CLIENTE_TOKEN" = "null" ] || [ -z "$CLIENTE_TOKEN" ]; then
    echo "❌ Error: No se pudo obtener token para cliente01"
    exit 1
else
    echo "✅ Token de cliente obtenido: ${CLIENTE_TOKEN:0:20}..."
fi

echo ""
echo "2. Obteniendo token para admin01..."
ADMIN_TOKEN=$(curl -s -X POST "$KEYCLOAK_URL/realms/$REALM_NAME/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin01&password=Clave123&grant_type=password&client_id=$CLIENT_ID" | jq -r '.access_token')

if [ "$ADMIN_TOKEN" = "null" ] || [ -z "$ADMIN_TOKEN" ]; then
    echo "❌ Error: No se pudo obtener token para admin01"
    exit 1
else
    echo "✅ Token de admin obtenido: ${ADMIN_TOKEN:0:20}..."
fi

echo ""
echo "3. Probando acceso SIN autenticación (debe fallar con 401)..."
RESPONSE=$(curl -s -w "%{http_code}" -o /dev/null "$PEDIDOS_URL/solicitudes")
if [ "$RESPONSE" = "401" ]; then
    echo "✅ Correcto: Acceso denegado sin token (401)"
else
    echo "❌ Error: Esperaba 401 pero obtuve $RESPONSE"
fi

echo ""
echo "4. Probando acceso CON token de cliente..."
RESPONSE=$(curl -s -w "%{http_code}" -o /dev/null -H "Authorization: Bearer $CLIENTE_TOKEN" "$PEDIDOS_URL/solicitudes")
if [ "$RESPONSE" = "200" ]; then
    echo "✅ Correcto: Cliente puede acceder a solicitudes (200)"
elif [ "$RESPONSE" = "403" ]; then
    echo "⚠️  Acceso denegado por permisos (403) - Verificar configuración de roles"
else
    echo "❌ Error: Respuesta inesperada $RESPONSE"
fi

echo ""
echo "5. Probando acceso CON token de admin..."
RESPONSE=$(curl -s -w "%{http_code}" -o /dev/null -H "Authorization: Bearer $ADMIN_TOKEN" "$PEDIDOS_URL/solicitudes")
if [ "$RESPONSE" = "200" ]; then
    echo "✅ Correcto: Admin puede acceder a solicitudes (200)"
elif [ "$RESPONSE" = "403" ]; then
    echo "⚠️  Acceso denegado por permisos (403) - Verificar configuración de roles"
else
    echo "❌ Error: Respuesta inesperada $RESPONSE"
fi

echo ""
echo "6. Verificando contenido del token de cliente..."
echo "Decodificando payload del JWT..."
PAYLOAD=$(echo $CLIENTE_TOKEN | cut -d. -f2 | base64 -d 2>/dev/null | jq . 2>/dev/null)
if [ $? -eq 0 ]; then
    echo "Roles del cliente:"
    echo "$PAYLOAD" | jq '.realm_access.roles'
    echo "Issuer:"
    echo "$PAYLOAD" | jq '.iss'
else
    echo "⚠️  No se pudo decodificar el token"
fi

echo ""
echo "=== FIN DE PRUEBAS ==="
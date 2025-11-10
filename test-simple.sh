#!/bin/sh
echo "=== PRUEBA DE AUTENTICACIÓN JWT ==="
echo ""

# 1. Obtener token
echo "1. Obteniendo token para cliente01..."
TOKEN_RESPONSE=$(curl -s -X POST "http://tpi-keycloak:8080/realms/tpi-backend/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=cliente01&password=Clave123&grant_type=password&client_id=tpi-backend-client")

echo "Token response: $TOKEN_RESPONSE"
echo ""

# 2. Probar acceso sin token
echo "2. Probando acceso SIN token (debe dar 401)..."
STATUS_NO_AUTH=$(curl -s -w "%{http_code}" -o /dev/null http://tpi-servicio-pedidos:8080/solicitudes)
echo "Status sin token: $STATUS_NO_AUTH"
echo ""

# 3. Extraer token
echo "3. Extrayendo access token..."
TOKEN=$(echo "$TOKEN_RESPONSE" | sed -n 's/.*"access_token":"\([^"]*\)".*/\1/p')
if [ -n "$TOKEN" ]; then
  echo "Token extraído exitosamente: ${TOKEN:0:30}..."
  
  # 4. Probar acceso con token
  echo ""
  echo "4. Probando acceso CON token..."
  STATUS_WITH_AUTH=$(curl -s -w "%{http_code}" -o /dev/null -H "Authorization: Bearer $TOKEN" http://tpi-servicio-pedidos:8080/solicitudes)
  echo "Status con token: $STATUS_WITH_AUTH"
  
  if [ "$STATUS_WITH_AUTH" = "200" ]; then
    echo "✅ ÉXITO: Autenticación JWT funcionando correctamente!"
  elif [ "$STATUS_WITH_AUTH" = "401" ]; then
    echo "❌ ERROR: Token rechazado - problema de validación JWT"
  elif [ "$STATUS_WITH_AUTH" = "403" ]; then
    echo "⚠️ PARCIAL: Token válido pero sin permisos para este endpoint"
  else
    echo "❓ INESPERADO: Status $STATUS_WITH_AUTH"
  fi
else
  echo "❌ Error: No se pudo extraer el token"
fi
echo ""
echo "=== FIN DE PRUEBAS ==="
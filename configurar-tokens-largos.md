# 🔧 Configuración de Tokens de Larga Duración en Keycloak

## 📋 Opciones para Tokens Más Duraderos

### **Opción 1: Modificar via Admin Console (Interfaz Web)**
1. Acceder a: http://localhost:8085/admin/
2. Login con usuario: admin / clave: admin
3. Ir a: Realms → tpi-backend → Realm Settings → Tokens
4. Modificar valores:
   - **Access Token Lifespan**: 24h (86400 segundos)
   - **Refresh Token Max Reuse**: 0 (ilimitado)  
   - **SSO Session Idle**: 24h
   - **SSO Session Max**: 24h

### **Opción 2: Via API REST (Automatizado)**
```bash
# 1. Obtener token de admin de Keycloak
ADMIN_TOKEN=$(curl -s -X POST "http://localhost:8085/realms/master/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin&password=admin&grant_type=password&client_id=admin-cli" \
  | jq -r '.access_token')

# 2. Actualizar configuración del realm
curl -X PUT "http://localhost:8085/admin/realms/tpi-backend" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "accessTokenLifespan": 86400,
    "accessTokenLifespanForImplicitFlow": 86400,
    "ssoSessionIdleTimeout": 86400,
    "ssoSessionMaxLifespan": 86400,
    "offlineSessionIdleTimeout": 2592000,
    "refreshTokenMaxReuse": 0
  }'
```

### **Opción 3: Tokens de Refresh (Recomendado)**
Los tokens de refresh permiten obtener nuevos access tokens sin re-autenticación:

```bash
# Usar refresh_token para obtener nuevo access_token
curl -X POST "http://localhost:8085/realms/tpi-backend/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=refresh_token&client_id=tpi-backend-client&refresh_token=REFRESH_TOKEN_AQUI"
```

### **Opción 4: Script de Auto-renovación**
```powershell
# Script PowerShell para renovar automáticamente
function Get-FreshToken {
    $response = Invoke-RestMethod -Uri "http://localhost:8085/realms/tpi-backend/protocol/openid-connect/token" `
        -Method POST `
        -Headers @{"Content-Type" = "application/x-www-form-urlencoded"} `
        -Body "grant_type=password&client_id=tpi-backend-client&username=admin01&password=Clave123"
    
    return $response.access_token
}

# Renovar token cada 50 minutos (antes de que expire en 60)
while ($true) {
    $global:adminToken = Get-FreshToken
    Write-Host "Token renovado: $(Get-Date)"
    Start-Sleep -Seconds 3000  # 50 minutos
}
```

## 🎯 **Recomendación para Desarrollo**

Para **fines de desarrollo y testing**, la mejor opción es:

1. **Configurar tokens de 24 horas** via Admin Console
2. **Usar refresh tokens** para renovación automática  
3. **Script de renovación** para pruebas largas

## ⚠️ **Consideraciones de Seguridad**

- **Desarrollo**: Tokens largos son convenientes
- **Producción**: Mantener tokens cortos (15-60 minutos) + refresh tokens
- **Nunca**: Tokens que no expiren (infinitos) en producción
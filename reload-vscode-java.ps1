# Comandos para ejecutar en la paleta de comandos de VS Code (Ctrl+Shift+P):
# 1. Java: Clean Workspace
# 2. Java: Reload Projects  
# 3. Maven: Reload Projects
# 4. Developer: Reload Window

# O alternativamente, cerrar VS Code y ejecutar este script PowerShell:

Write-Host "=== LIMPIEZA COMPLETA DE WORKSPACE JAVA ===" 
Write-Host "1. Eliminando cache de workspace Java..."

# Limpiar targets de Maven
Get-ChildItem -Path . -Recurse -Name "target" -Directory | ForEach-Object {
    $targetPath = Join-Path $PWD $_
    if (Test-Path $targetPath) {
        Write-Host "Limpiando: $targetPath"
        Remove-Item -Path $targetPath -Recurse -Force -ErrorAction SilentlyContinue
    }
}

Write-Host "2. Ejecutando mvn clean en cada proyecto..."
$proyectos = @("ApiGateway", "ServicioAdministracion", "ServicioLogistica", "ServicioPedidos", "ServicioGeolocalizacion")

foreach ($proyecto in $proyectos) {
    if (Test-Path $proyecto) {
        Write-Host "Limpiando: $proyecto"
        Push-Location $proyecto
        mvn clean -q 2>$null
        Pop-Location
    }
}

Write-Host "3. Limpieza completada. Reinicie VS Code para aplicar cambios."
Write-Host "   - Cierre VS Code completamente"
Write-Host "   - Abra VS Code"  
Write-Host "   - Abra la carpeta TPI"
Write-Host "   - Ejecute: Ctrl+Shift+P -> 'Java: Reload Projects'"
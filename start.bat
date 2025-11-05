@echo off
REM Script para iniciar todo el ecosistema con Docker
echo ========================================
echo  TPI Backend - Sistema de Microservicios
echo ========================================
echo.
echo Iniciando todos los servicios con Docker...
echo.

REM Verificar que Docker está funcionando
docker --version > nul 2>&1
if errorlevel 1 (
    echo ERROR: Docker no está instalado o no está funcionando
    echo Por favor, instale Docker Desktop y asegurese de que este ejecutandose
    pause
    exit /b 1
)

echo Docker detectado correctamente
echo.

REM Construir y levantar todos los servicios
echo Construyendo e iniciando servicios...
docker-compose -f docker-compose-definitivo.yml up -d --build

if errorlevel 1 (
    echo ERROR: Fallo al iniciar los servicios
    pause
    exit /b 1
)

echo.
echo ========================================
echo  Servicios iniciados correctamente!
echo ========================================
echo.
echo API Gateway:        http://localhost:8080
echo Administracion:     http://localhost:8082
echo Logistica:          http://localhost:8083
echo Pedidos:            http://localhost:8084
echo Geolocalizacion:    http://localhost:8081
echo PostgreSQL:         localhost:5432
echo.
echo Para ver logs: logs.bat
echo Para parar:    stop.bat
echo.
pause
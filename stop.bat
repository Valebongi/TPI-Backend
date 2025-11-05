@echo off
REM Script para parar todos los servicios
echo ========================================
echo  Parando todos los servicios...
echo ========================================
echo.

docker-compose -f docker-compose-definitivo.yml down

if errorlevel 1 (
    echo ERROR: Fallo al parar los servicios
    pause
    exit /b 1
)

echo.
echo Todos los servicios han sido parados correctamente
echo.
pause
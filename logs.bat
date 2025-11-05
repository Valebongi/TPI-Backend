@echo off
REM Script para ver logs de todos los servicios
echo ========================================
echo  Mostrando logs en tiempo real...
echo ========================================
echo.
echo Presiona Ctrl+C para salir
echo.

docker-compose -f docker-compose-definitivo.yml logs -f
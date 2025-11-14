#!/bin/bash

# Script para build resiliente de microservicios
# Este script maneja reintentos automáticos en caso de fallos de conectividad

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Función para logging
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Función para retry de comandos
retry_command() {
    local max_attempts=$1
    local delay=$2
    local command="${@:3}"
    local attempt=1

    while [ $attempt -le $max_attempts ]; do
        log_info "Intento $attempt de $max_attempts: $command"
        
        if eval "$command"; then
            log_info "Comando ejecutado exitosamente en el intento $attempt"
            return 0
        else
            if [ $attempt -eq $max_attempts ]; then
                log_error "Comando falló después de $max_attempts intentos"
                return 1
            fi
            
            log_warn "Intento $attempt falló. Reintentando en $delay segundos..."
            sleep $delay
            ((attempt++))
        fi
    done
}

# Limpiar builds previos
log_info "Limpiando imágenes y contenedores previos..."
docker-compose -f docker-compose-definitivo.yml down --remove-orphans || true
docker system prune -f || true

# Intentar build con reintentos
log_info "Iniciando build de microservicios con reintentos..."

# Configuraciones de retry
MAX_ATTEMPTS=3
DELAY_SECONDS=30

# Build individual de cada servicio con reintentos
services=("api-gateway" "servicio-geolocalizacion" "servicio-administracion" "servicio-logistica" "servicio-pedidos")

for service in "${services[@]}"; do
    log_info "Building $service..."
    
    if retry_command $MAX_ATTEMPTS $DELAY_SECONDS "docker-compose -f docker-compose-definitivo.yml build $service"; then
        log_info "$service built successfully"
    else
        log_error "Failed to build $service after $MAX_ATTEMPTS attempts"
        
        # Intentar build sin cache como último recurso
        log_warn "Trying to build $service without cache as last resort..."
        if retry_command 2 60 "docker-compose -f docker-compose-definitivo.yml build --no-cache $service"; then
            log_info "$service built successfully without cache"
        else
            log_error "Failed to build $service even without cache"
            exit 1
        fi
    fi
done

log_info "Todos los servicios se construyeron exitosamente!"

# Opcional: Iniciar servicios después del build exitoso
read -p "¿Desea iniciar los servicios ahora? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    log_info "Iniciando servicios..."
    docker-compose -f docker-compose-definitivo.yml up -d
    log_info "Servicios iniciados. Verificando estado..."
    sleep 10
    docker-compose -f docker-compose-definitivo.yml ps
fi
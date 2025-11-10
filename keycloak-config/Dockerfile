FROM alpine:latest

# Instalar dependencias necesarias
RUN apk add --no-cache curl jq bash

# Copiar script de configuración actualizado según cátedra
COPY setup-keycloak-catedra.sh /setup-keycloak.sh
RUN chmod +x /setup-keycloak.sh

# Comando por defecto
CMD ["/setup-keycloak.sh"]
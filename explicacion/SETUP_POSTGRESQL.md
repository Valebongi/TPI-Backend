# Comandos para ejecutar después de instalar PostgreSQL

# 1. Abrir Command Prompt o PowerShell como administrador
# 2. Conectarse a PostgreSQL:
psql -U postgres

# 3. Crear la base de datos para el ServicioAdministracion:
CREATE DATABASE tpi_backend_admin_db;

# 4. Verificar que se creó:
\l

# 5. Salir:
\q

# Luego ya podrás ejecutar el proyecto Spring Boot
Paso a Paso - Keycloak Container.md 2025-07-04

🔐 Paso a Paso - Keycloak con Docker para el TPI![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.001.png)

Este instructivo guía la instalación de Docker en distintos sistemas operativos y la posterior creación y configuración de un contenedor de **Keycloak**, para utilizarlo como **Authentication e Identity Provider** del Trabajo Práctico Integrador.

Incluye la creación de roles y usuarios de ejemplo para comenzar a integrarlo con el backend.

🐳 1. Instalación de Docker

¿Por qué Docker?

Docker es una plataforma que permite empaquetar aplicaciones junto con todas sus dependencias en contenedores. Esto simplifica enormemente la instalación, despliegue y ejecución de software, asegurando que funcione igual en cualquier entorno (desarrollo, testing o producción).

Para este trabajo práctico integrador, Docker es clave porque nos permitirá levantar servicios como **Keycloak** de manera rápida y aislada, sin necesidad de instalarlo directamente en el sistema operativo. También facilitará la integración entre microservicios a futuro, y permite que los distintos equipos trabajen con entornos equivalentes.

Entre sus funcionalidades principales se destacan:

- **Ejecución de contenedores livianos y portables**: permite aislar procesos sin la sobrecarga de una máquina virtual completa, ocupando pocos recursos y arrancando rápidamente.
- **Aislamiento entre aplicaciones**: cada contenedor corre en su propio entorno, evitando conflictos de dependencias o configuraciones entre servicios.
- **Definición y configuración por archivos de texto**: Docker permite definir el comportamiento de servicios y entornos completos mediante archivos de texto como Dockerfile y docker-compose.yml, lo que facilita la reproducción exacta de configuraciones y mejora la trazabilidad en equipos de trabajo.
- **Reutilización de imágenes y componentes**: se pueden descargar imágenes oficiales o personalizadas desde Docker Hub y combinarlas, reduciendo el tiempo de configuración.
- **Despliegue reproducible con docker-compose**: al definir múltiples servicios y sus relaciones en un solo archivo, se pueden levantar entornos completos de forma automática y coherente en cualquier equipo.

A continuación, se detallan los pasos para instalar Docker en cada sistema operativo.

1. En Windows 10/11
1. Descargar Docker Desktop desde: <https://www.docker.com/products/docker-desktop/>
1. Ejecutar el instalador y seguir los pasos.
1. Al finalizar, reiniciar la PC si es necesario.
1. Verificar desde terminal (PowerShell o CMD):

docker --version docker compose version ![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.002.png)

- Si Docker está correctamente instalado, los comandos anteriores deberían devolver algo como:![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.003.png)

Docker version 28.3.x, build abc1234 Docker Compose version v2.38.x

💡 Requiere tener habilitado **WSL2** (Subsistema de Windows para Linux versión 2), que permite ejecutar un entorno Linux directamente sobre Windows. Se recomienda instalar **Ubuntu** como distro por su compatibilidad, soporte extendido y facilidad de uso. Docker Desktop guía automáticamente en la instalación y configuración inicial si aún no está configurado.![ref1]

2. En Linux (Ubuntu/Debian)

   sudo apt update ![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.005.png)

   sudo apt install docker.io docker-compose -y sudo systemctl enable docker 

   sudo systemctl start docker 

   sudo usermod -aG docker $USER

🔁 Reiniciar sesión para que se aplique el grupo docker ![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.006.png)Verificar:

docker --version docker compose version ![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.007.png)![ref1]

3. En macOS
1. Descargar Docker Desktop desde: <https://www.docker.com/products/docker-desktop/>
1. Abrir el archivo .dmg y arrastrar Docker a Aplicaciones.
1. Ejecutar Docker Desktop y completar configuración.

Verificar:

docker --version docker compose version ![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.008.png)

🚀 2. Lanzar un contenedor de Keycloak

¿Qué es Keycloak y por qué lo usamos?

Keycloak es una solución de código abierto para la gestión de identidades y accesos (IAM - Identity and Access Management). Permite centralizar el control de autenticación de usuarios, la administración de roles y la emisión de tokens compatibles con OAuth2 y OpenID Connect.

Para nuestro Trabajo Práctico Integrador, cumple la función de **Authentication Provider** (quién valida la identidad) y **Identity Provider** (quién emite la información sobre la identidad del usuario, como nombre, email, roles, etc.).

Esto permite desacoplar la seguridad del backend, delegando en Keycloak el ingreso de usuarios y la asignación de permisos, y brindando mayor flexibilidad, escalabilidad y estándares modernos de autenticación para nuestros microservicios.

Crear el contenedor Docker para soportar una instancia de Keycloak

Creamos una carpeta de trabajo y dentro de ella un archivo docker-compose.yml con el siguiente contenido:

version: '3.1' ![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.009.png)

services: 

`  `keycloak: 

`    `image: quay.io/keycloak/keycloak:24.0.3     container\_name: keycloak 

`    `command: start-dev 

`    `ports: 

- "8081:8080" 

`    `environment: 

- KEYCLOAK\_ADMIN=admin 
- KEYCLOAK\_ADMIN\_PASSWORD=admin123 

`    `volumes: 

- keycloak\_data:/opt/keycloak/data 

volumes: 

`  `keycloak\_data:

🧠 Este contenedor deja Keycloak expuesto en http://localhost:8081/![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.010.png)

Iniciar:

docker compose up -d ![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.011.png)🧩 Explicación del comando:![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.012.png)

docker compose: utiliza el archivo docker-compose.yml para levantar servicios definidos allí. up: inicia y crea los contenedores especificados si no existen.

-d: ejecuta los contenedores en modo "detached" (en segundo plano), permitiendo seguir usando la terminal.

- Si el contenedor se levanta correctamente, se puede comprobar con: docker ps 

Y debería aparecer un contenedor con el nombre keycloak, expuesto en el puerto 8081. También se puede acceder con el navegador a http://localhost:8081/ para verificar la interfaz de administración.

Parar:

docker compose down ![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.013.png)![ref2]

![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.015.png)

![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.016.png)

🔑 3. Acceder a Keycloak

1. Navegar a <http://localhost:8081/>

   ![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.017.jpeg)

2. Iniciar sesión con:
- Usuario: admin
- Contraseña: admin123

![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.018.jpeg)

🏗 4. Crear Realm, Roles y Usuarios

1. Crear un Realm nuevo

Un *Realm* en Keycloak es una partición lógica del servidor que permite gestionar de manera independiente usuarios, roles, clientes (aplicaciones) y configuraciones de seguridad. Cada realm funciona como un espacio aislado dentro del mismo servidor Keycloak.

Esto permite, por ejemplo, tener diferentes entornos (producción, desarrollo, test) o aplicaciones separadas sin interferencias entre sí.

1. Desde el menú desplegable superior izquierdo, seleccionar **Create Realm**. 

   ![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.019.png)

2. Nombre sugerido: tpi-backend ![ref2]

   ![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.020.jpeg)

2. Crear Roles
1. Ir a **Realm Roles > Create role** 

   ![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.021.jpeg)

2. Crear los siguientes roles:
- cliente
- admin![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.022.png)

![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.023.jpeg)

3. Crear Usuarios
1. Ir a **Users > Create new user**
1. Completar los datos mínimos (username, nombre, correo)
1. En el perfil del usuario, ir a **Credentials**:
   1. Definir contraseña (ej: clave123) y marcar **Set as temporary: OFF**
1. Ir a **Role Mappings**:
- Asignar uno de los roles creados

Deberíamos crear al menos un usuario cliente y un usuario admin para pruebas posteriores![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.024.png)

A modo de ejemplo, se puede utilizar la siguiente tabla de usuarios sugeridos para probar distintos roles y comportamientos en el sistema:

**Username Nombre Email Rol asignado Contraseña**



|cliente01|Carla Gómez|<carla@example.com>|cliente|Clave123|
| - | - | - | - | - |
|cliente02|Juan Pérez|<juan@example.com>|cliente|Clave123|
|cliente03|Lucía Fernández|<lucia@example.com>|cliente|Clave123|
|cliente04|Diego Luna|<diego@example.com>|cliente|Clave123|
|cliente05|Florencia Ramos|<flor@example.com>|cliente|Clave123|
|admin01|Marcos Salas|<marcos@example.com>|admin|Clave123|
|admin02|Natalia Quinteros|<natalia@example.com>|admin|Clave123|
|admin03|Tomás Acosta|<tomas@example.com>|admin|Clave123|
|tester01|Emiliano Testa|<emiliano@example.com>|cliente|Clave123|

tester02 Belén Rivas <belen@example.com> cliente Clave123![ref1]

![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.025.jpeg)

🧪 5. Verificar tokens

Una vez configurado el Realm y creados los usuarios, podemos probar el proceso de autenticación y obtener un token JWT válido. Esto se realiza contra los endpoints estándar de OpenID Connect que expone Keycloak.

1. Crear cliente público para pruebas

Antes de comenzar, es necesario crear en Keycloak un cliente público para permitir el flujo de autorización con redirección.

1. Ingresar al panel de administración de Keycloak (<http://localhost:8081/admin/>).
1. Seleccionar el realm tpi-backend y navegar a la sección **Clients**.
1. Hacer clic en **Create client**.
1. Configurar:
   1. Client ID: tpi-backend-client
   1. Client type: Public
   1. Name: TPI Backend Client
   1. Root URL: http://localhost:8080
   1. Click en Next
1. En la pantalla de configuración:
- Activar ✅ **Standard Flow Enabled**
- Desactivar ❌ Client Authentication
- En **Valid redirect URIs**, colocar:
  - http://localhost:8080/api/login/oauth2/code/keycloak
  - O un comodín como http://localhost:8080/\*
- Click en Save
2. Probar autenticación (obtener token)

Alternativamente al flujo directo password, también se puede utilizar el flujo estándar de autenticación con formulario de Keycloak y luego intercambiar el código de autorización por un token válido.

1. Navegar con el navegador a la URL del Authorization Endpoint:

http://localhost:8081/realms/tpi-backend/protocol/openid-connect/auth   ?client\_id=tpi-backend-client ![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.026.png)

`  `&response\_type=code 

`  `&redirect\_uri=http://localhost:8080/api/login/oauth2/code/keycloak 

2. Iniciar sesión con un usuario válido, por ejemplo:
- usuario: cliente01
- contraseña: clave123
3. Una vez autenticado, Keycloak intentará redirigir a la redirect\_uri especificada con un parámetro code en la URL.
3. Luego, se debe realizar una petición POST al token endpoint para intercambiar el code por un token:

\### Obtener token vía código de autorización ![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.027.png)

POST http://localhost:8081/realms/tpi-backend/protocol/openid-connect/token Content-Type: application/x-www-form-urlencoded 

grant\_type=authorization\_code code=<el\_code\_recibido> 

client\_id=tpi-backend-client redirect\_uri=http://localhost:8080/api/login/oauth2/code/keycloak 

💡 Este flujo es el más adecuado para aplicaciones web seguras y refleja mejor un escenario real de uso en producción.![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.028.png)

Una vez obtenido el access\_token, se puede inspeccionar su contenido en [https://jwt.io](https://jwt.io/) para verificar:

- Expiración (exp)
- Identidad del usuario (preferred\_username)
- Roles disponibles (realm\_access.roles)

Esto permite comprobar que el token es válido, confiable y contiene los claims esperados antes de integrarlo a una aplicación real. en [https://jwt.io](https://jwt.io/) y utilizarlo para consumir recursos protegidos.

🧪 Ejemplo de intercambio automático de código a token con RestClient

Para ilustrar cómo este proceso puede integrarse en un backend Spring moderno, aquí un ejemplo funcional utilizando RestClient de Spring Framework 6+ con codificación correcta del cuerpo application/x-www- form-urlencoded:

Creación de Servicio spring web básico para recibir la redirección del código de aplicación y obtener el token. Debe crear un microservicio básico con spring web y agregar el siguiente endpoint.

@GetMapping("/api/login/oauth2/code/keycloak") ![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.029.png)

public String intercambiarCode(@RequestParam String code) throws UnsupportedEncodingException { 

`    `RestClient restClient = RestClient.create(); 

`    `String formData = "grant\_type=authorization\_code" + 

`        `"&code=" + URLEncoder.encode(code, StandardCharsets.UTF\_8) + 

`        `"&client\_id=tpi-backend-client" + 

`        `"&redirect\_uri=" + URLEncoder.encode("http://localhost:8080/api/login/oauth2/code/keycloak", StandardCharsets.UTF\_8); 

`    `String token = restClient.post() 

.uri("http://localhost:8081/realms/tpi-backend/protocol/openid- connect/token") 

.contentType(MediaType.APPLICATION\_FORM\_URLENCODED) 

.body(formData) 

.retrieve() 

.body(String.class); 

`    `log.info("🔐 Token recibido desde Keycloak:{}", token);     return "✅ Token recibido y logueado en consola"; 

} 

🧠 Este código realiza el intercambio del code directamente desde el backend y permite capturar el ![](Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.030.png)access\_token para depuración o análisis posterior. Ideal para entornos de prueba o enseñanza. en [https://jwt.io](https://jwt.io/) y utilizarlo para consumir recursos protegidos. Luego volver a probar y se podrá encontrar el token en la consola del servidor.
11 / 11

[ref1]: Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.004.png
[ref2]: Aspose.Words.ae774b37-6570-4479-8ac6-97f7cbc1d218.014.png

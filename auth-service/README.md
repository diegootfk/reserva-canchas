# Auth Service

## Descripción

Microservicio encargado de la autenticación y autorización de usuarios dentro del sistema de reserva de canchas deportivas.

Permite registrar usuarios, registrar administradores, autenticar credenciales y generar tokens JWT para proteger el acceso a los distintos microservicios del sistema.

---

## Tecnologías Utilizadas

- Java 21
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- MySQL
- Maven
- Swagger OpenAPI
- SLF4J Logger
- JUnit 5
- Mockito

---

## Funcionalidades

- Registro de usuarios
- Registro de administradores
- Inicio de sesión
- Generación de JWT
- Validación de JWT
- Obtención de roles de usuario
- Encriptación de contraseñas mediante SHA-1
- Integración con API Gateway
- Documentación Swagger
- Registro de logs

---

## Estructura del Proyecto

src/main/java

├── controller

├── service

├── repository

├── model

├── security

├── config

├── dto

├── exception

└── util

---

## Endpoints Disponibles

### Iniciar Sesión

POST /auth/login

### Registrar Usuario

POST /auth/register

### Registrar Administrador

POST /auth/register-admin

---

## Ejemplo de JSON

### Login

```json
{
  "email": "admin@gmail.com",
  "password": "123456"
}
```

### Respuesta

```json
{
  "status": "ok",
  "token": "eyJhbGciOiJIUzM4NCJ9..."
}
```

---

### Registrar Usuario

```json
{
  "email": "usuario@gmail.com",
  "password": "123456"
}
```

### Respuesta

```json
{
  "message": "Usuario creado correctamente"
}
```

---

### Registrar Administrador

```json
{
  "email": "admin@gmail.com",
  "password": "123456"
}
```

### Respuesta

```json
{
  "message": "Administrador creado correctamente"
}
```

---

## Seguridad Implementada

Este microservicio utiliza JWT (JSON Web Token) para la autenticación.

El token contiene:

- Email del usuario.
- Rol del usuario.
- Fecha de emisión.
- Fecha de expiración.

Duración del token:

```text
1 hora
```

---

## Roles del Sistema

### USER

Permite acceder a funcionalidades de usuario estándar.

### ADMIN

Permite administrar recursos del sistema.

---

## Reglas de Negocio

- No se puede registrar un usuario con un correo ya existente.
- No se puede registrar un administrador con un correo ya existente.
- La contraseña se almacena encriptada mediante SHA-1.
- Solo usuarios con credenciales válidas pueden obtener un token JWT.
- El token expira automáticamente después de una hora.

---

## Componentes Principales

### UserService

Responsable de:

- Registro de usuarios.
- Registro de administradores.
- Inicio de sesión.
- Obtención de roles.

### JwtService

Responsable de:

- Generación de JWT.
- Validación de JWT.
- Extracción de información desde JWT.

### HashService

Responsable de:

- Encriptación de contraseñas mediante SHA-1.

---

## Validaciones Implementadas

- Verificación de existencia de correo electrónico.
- Validación de credenciales.
- Validación de JWT.
- Manejo centralizado de excepciones.

---

## Pruebas Unitarias

Se implementaron pruebas unitarias utilizando:

- JUnit 5
- Mockito

Cobertura de:

- Login exitoso.
- Login fallido.
- Registro de usuario.
- Registro de administrador.
- Usuario duplicado.
- Generación de JWT.
- Validación de JWT.
- Obtención de roles.

---

## Integración con API Gateway

El Auth Service trabaja en conjunto con el API Gateway para proteger los endpoints de los demás microservicios.

Flujo:

1. Usuario inicia sesión.
2. Auth Service genera JWT.
3. Cliente envía JWT al API Gateway.
4. API Gateway valida el token.
5. Se permite o deniega el acceso.

---

## Documentación Swagger

Swagger UI:

http://localhost:7101/swagger-ui.html

OpenAPI:

http://localhost:7101/v3/api-docs

---

## Autor

Proyecto desarrollado para la asignatura Fullstack I.

Duoc UC - Ingeniería en Informática.
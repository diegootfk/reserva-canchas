# API Gateway

## Descripción

Microservicio encargado de centralizar el acceso a todos los servicios del sistema de reserva de canchas deportivas.

Actúa como punto único de entrada para los clientes, gestionando el enrutamiento de solicitudes, validación de tokens JWT y control de acceso a los distintos microservicios.

---

## Tecnologías Utilizadas

- Java 21
- Spring Boot
- Spring Cloud Gateway
- Spring Security
- JWT (JSON Web Token)
- Maven
- Swagger OpenAPI
- SLF4J Logger
- JUnit 5
- Mockito

---

## Funcionalidades

- Enrutamiento centralizado de solicitudes
- Validación de tokens JWT
- Protección de endpoints
- Control de acceso basado en roles
- Integración con Auth Service
- Comunicación con todos los microservicios
- Registro de logs
- Documentación Swagger

---

## Arquitectura

```text
Cliente
   │
   ▼
API Gateway
   │
   ├── Auth Service
   ├── Usuario Service
   ├── Sede Service
   ├── Cancha Service
   ├── Reserva Service
   ├── Pago Service
   ├── Reseña Service
   ├── Horario Service
   ├── Disponibilidad Service
   ├── Mantenimiento Service
   └── Notificación Service
```

---

## Tecnologías de Seguridad

- Spring Security
- JWT Authentication
- Authorization por Roles
- Filtros personalizados
- Validación de Token

---

## Funciones Principales

### Validación de JWT

El API Gateway verifica:

- Existencia del token.
- Integridad del token.
- Fecha de expiración.
- Rol asociado al usuario.

---

### Control de Acceso

Permite restringir acceso a endpoints según:

- Usuario autenticado.
- Rol USER.
- Rol ADMIN.

---

### Enrutamiento

Redirecciona solicitudes hacia los microservicios correspondientes.

Ejemplos:

```text
/auth/**               → Auth Service
/usuarios/**           → Usuario Service
/sedes/**              → Sede Service
/canchas/**            → Cancha Service
/reservas/**           → Reserva Service
/pagos/**              → Pago Service
/resenas/**            → Reseña Service
/horarios/**           → Horario Service
/disponibilidades/**   → Disponibilidad Service
/mantenimientos/**     → Mantenimiento Service
/notificaciones/**     → Notificación Service
```

---

## Flujo de Autenticación

### Paso 1

El usuario inicia sesión.

```http
POST /auth/login
```

---

### Paso 2

Auth Service genera un JWT.

```json
{
  "status": "ok",
  "token": "eyJhbGciOiJIUzM4NCJ9..."
}
```

---

### Paso 3

El cliente envía el token.

```http
Authorization: Bearer eyJhbGciOiJIUzM4NCJ9...
```

---

### Paso 4

API Gateway valida el JWT.

---

### Paso 5

La solicitud es enviada al microservicio correspondiente.

---

## Configuración de Rutas

Ejemplo:

```yaml
spring:
  cloud:
    gateway:
      routes:

        - id: usuario-service
          uri: http://localhost:7091
          predicates:
            - Path=/usuarios/**

        - id: sede-service
          uri: http://localhost:7092
          predicates:
            - Path=/sedes/**
```

---

## Seguridad Implementada

- JWT obligatorio para endpoints protegidos.
- Validación automática de tokens.
- Roles USER y ADMIN.
- Protección mediante filtros de Spring Security.

---

## Reglas de Negocio

- No se permite acceder a recursos protegidos sin JWT válido.
- No se permite acceder con tokens expirados.
- Todas las solicitudes pasan primero por el API Gateway.
- El API Gateway es el único punto de entrada al sistema.

---

## Pruebas Unitarias

Se implementaron pruebas unitarias utilizando:

- JUnit 5
- Mockito

Cobertura de:

- Validación de JWT.
- Filtros de seguridad.
- Enrutamiento.
- Autorización por roles.
- Manejo de excepciones.

---

## Beneficios del API Gateway

- Centralización de acceso.
- Seguridad unificada.
- Escalabilidad.
- Menor acoplamiento entre cliente y microservicios.
- Mejor mantenibilidad del sistema.

---

## Documentación Swagger

Swagger UI:

http://localhost:7090/swagger-ui.html

OpenAPI:

http://localhost:7090/v3/api-docs

---

## Autor

Proyecto desarrollado para la asignatura Fullstack I.

Duoc UC - Ingeniería en Informática.
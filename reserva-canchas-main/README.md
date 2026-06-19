# Sistema de Reserva de Canchas

Proyecto desarrollado con arquitectura de microservicios utilizando Spring Boot, API Gateway, JWT y MySQL.

---

# Tecnologías utilizadas

* Java 21
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Cloud Gateway
* MySQL
* Liquibase
* Maven
* REST API
* Postman


# Arquitectura del proyecto

El sistema está compuesto por múltiples microservicios independientes conectados mediante REST.

## Microservicios

* usuario-service
* cancha-service
* reserva-service
* pago-service
* sede-service
* horario-service
* disponibilidad-service
* notificacion-service
* resena-service
* mantenimiento-service
* auth-service
* api-gateway


# API Gateway

El proyecto utiliza un API Gateway centralizado para:

* enrutar solicitudes
* validar JWT
* controlar accesos
* centralizar seguridad

Puerto del gateway:

```text
7090
```

---

# Seguridad JWT

El sistema implementa autenticación y autorización mediante JWT.

## Roles implementados

* ADMIN
* USER

## Funcionalidades

* Login
* Generación de token
* Protección de endpoints
* Validación de JWT en Gateway
* Validación de JWT en microservicios


# Manejo de excepciones

Se implementó manejo global de excepciones utilizando:

* GlobalExceptionHandler
* ResourceNotFoundException
* BadRequestException
* ApiErrorResponse

Respuestas JSON personalizadas:

```json
{
  "timestamp": "2026-05-10T10:00:00",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Recurso no encontrado"
}
```

---

# Comunicación entre microservicios

La comunicación se realiza mediante `RestTemplate`.

## Ejemplos

### reserva-service

Valida:

* existencia de usuario
* existencia de cancha

### pago-service

Valida:

* existencia de reserva


# Bases de datos

Cada microservicio posee su propia base de datos MySQL independiente.

## Bases implementadas

* db_usuario_service
* db_cancha_service
* db_reserva_service
* db_pago_service
* db_sede_service
* db_horario_service
* db_disponibilidad_service
* db_notificacion_service
* db_resena_service
* db_mantenimiento_service
* db_auth_service


# Puertos utilizados

| Microservicio          | Puerto |
| ---------------------- | ------ |
| api-gateway            | 7090   |
| usuario-service        | 7091   |
| cancha-service         | 7092   |
| reserva-service        | 7093   |
| pago-service           | 7094   |
| sede-service           | 7095   |
| horario-service        | 7096   |
| disponibilidad-service | 7097   |
| notificacion-service   | 7098   |
| resena-service         | 7099   |
| mantenimiento-service  | 7100   |
| auth-service           | 7101   |


# Funcionalidades principales

* CRUD de usuarios
* CRUD de canchas
* CRUD de reservas
* CRUD de pagos
* CRUD de sedes
* CRUD de horarios
* CRUD de disponibilidades
* autenticación JWT
* control de acceso por roles
* validaciones entre microservicios
* manejo global de excepciones


# Autor

Proyecto desarrollado por Diego Berrios para la asignatura Fullstack.

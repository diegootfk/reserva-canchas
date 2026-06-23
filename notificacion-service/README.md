# Notificacion Service

## Descripción

Microservicio encargado de la gestión y envío de notificaciones asociadas a los procesos del sistema de reserva de canchas deportivas.

Permite registrar, consultar, actualizar y eliminar notificaciones, validando la existencia de usuarios y reservas mediante comunicación entre microservicios.

---

## Tecnologías Utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- MySQL
- Maven
- Spring WebClient
- Swagger OpenAPI
- Spring HATEOAS
- SLF4J Logger
- JUnit 5
- Mockito

---

## Funcionalidades

- Crear notificaciones
- Listar notificaciones
- Buscar notificación por ID
- Actualizar notificaciones
- Eliminar notificaciones
- Verificar existencia de notificación
- Comunicación entre microservicios mediante WebClient
- Documentación Swagger
- Soporte HATEOAS
- Registro de logs

---

## Estructura del Proyecto

src/main/java

├── controller

├── service

├── repository

├── model

├── dto

├── exception

├── assembler

├── config

└── client

---

## Endpoints Disponibles

### Crear notificación

POST /notificaciones

### Listar notificaciones

GET /notificaciones

### Buscar notificación por ID

GET /notificaciones/{id}

### Actualizar notificación

PUT /notificaciones/{id}

### Eliminar notificación

DELETE /notificaciones/{id}

### Verificar existencia

GET /notificaciones/{id}/exists

---

## Ejemplo de JSON

### Crear Notificación

```json
{
  "idUsuario": 1,
  "idReserva": 1,
  "mensaje": "Su reserva fue confirmada exitosamente",
  "tipoNotificacion": "EMAIL",
  "fechaEnvio": "2026-06-22"
}
```

### Respuesta

```json
{
  "id": 1,
  "idUsuario": 1,
  "idReserva": 1,
  "mensaje": "Su reserva fue confirmada exitosamente",
  "tipoNotificacion": "EMAIL",
  "fechaEnvio": "2026-06-22",
  "estado": "ENVIADA"
}
```

---

## Comunicación Entre Microservicios

Antes de registrar una notificación, el sistema valida:

- Existencia del usuario mediante usuario-service.
- Existencia de la reserva mediante reserva-service.

Estas validaciones se realizan utilizando Spring WebClient.

---

## HATEOAS

Este microservicio implementa HATEOAS mediante un Assembler dedicado que incorpora enlaces relacionados a cada recurso.

Ejemplo:

```json
{
  "id": 1,
  "mensaje": "Su reserva fue confirmada exitosamente",
  "estado": "ENVIADA",
  "_links": {
    "self": {
      "href": "http://localhost:7090/notificaciones/1"
    },
    "notificaciones": {
      "href": "http://localhost:7090/notificaciones"
    },
    "usuario": {
      "href": "http://localhost:7090/usuarios/1"
    },
    "reserva": {
      "href": "http://localhost:7090/reservas/1"
    }
  }
}
```

---

## Reglas de Negocio

- No se puede registrar una notificación para un usuario inexistente.
- No se puede registrar una notificación para una reserva inexistente.
- Toda notificación creada queda automáticamente con estado:

```text
ENVIADA
```

- Toda notificación debe estar asociada a un usuario válido.
- Toda notificación debe estar asociada a una reserva válida.

---

## Validaciones Implementadas

- Validación mediante DTO.
- Verificación de usuario existente.
- Verificación de reserva existente.
- Manejo centralizado de excepciones.
- Verificación de existencia de registros.

---

## Pruebas Unitarias

Se implementaron pruebas unitarias utilizando:

- JUnit 5
- Mockito

Cobertura de:

- Buscar notificación
- Actualizar notificación
- Eliminar notificación
- Verificar existencia
- Reglas de negocio
- Manejo de excepciones

---

## Documentación Swagger

Swagger UI:

http://localhost:7098/swagger-ui.html

OpenAPI:

http://localhost:7098/v3/api-docs

---

## Autor

Proyecto desarrollado para la asignatura Fullstack I.

Duoc UC - Ingeniería en Informática.
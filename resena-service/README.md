# Reseña Service

## Descripción

Microservicio encargado de la gestión de reseñas realizadas por los usuarios sobre las canchas deportivas reservadas.

Permite registrar, consultar, actualizar y eliminar reseñas, validando la existencia de usuarios, canchas y reservas mediante comunicación entre microservicios.

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

- Crear reseñas
- Listar reseñas
- Buscar reseña por ID
- Actualizar reseñas
- Eliminar reseñas
- Verificar existencia de reseña
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

### Crear reseña

POST /resenas

### Listar reseñas

GET /resenas

### Buscar reseña por ID

GET /resenas/{id}

### Actualizar reseña

PUT /resenas/{id}

### Eliminar reseña

DELETE /resenas/{id}

### Verificar existencia

GET /resenas/{id}/exists

---

## Ejemplo de JSON

### Crear Reseña

```json
{
  "idUsuario": 1,
  "idCancha": 2,
  "idReserva": 3,
  "calificacion": 5,
  "comentario": "Excelente cancha y muy buen servicio",
  "fechaResena": "2026-06-22"
}
```

### Respuesta

```json
{
  "id": 1,
  "idUsuario": 1,
  "idCancha": 2,
  "idReserva": 3,
  "calificacion": 5,
  "comentario": "Excelente cancha y muy buen servicio",
  "fechaResena": "2026-06-22"
}
```

---

## Comunicación Entre Microservicios

Antes de registrar una reseña, el sistema valida:

- Existencia del usuario mediante usuario-service.
- Existencia de la cancha mediante cancha-service.
- Existencia de la reserva mediante reserva-service.

Estas validaciones se realizan utilizando Spring WebClient.

---

## HATEOAS

Este microservicio implementa HATEOAS mediante un Assembler dedicado que incorpora enlaces relacionados a cada recurso.

Ejemplo:

```json
{
  "id": 1,
  "calificacion": 5,
  "comentario": "Excelente cancha",
  "_links": {
    "self": {
      "href": "http://localhost:7090/resenas/1"
    },
    "resenas": {
      "href": "http://localhost:7090/resenas"
    },
    "usuario": {
      "href": "http://localhost:7090/usuarios/1"
    },
    "cancha": {
      "href": "http://localhost:7090/canchas/2"
    },
    "reserva": {
      "href": "http://localhost:7090/reservas/3"
    }
  }
}
```

---

## Reglas de Negocio

- No se puede registrar una reseña para un usuario inexistente.
- No se puede registrar una reseña para una cancha inexistente.
- No se puede registrar una reseña para una reserva inexistente.
- La calificación debe estar dentro del rango permitido por el sistema.
- Toda reseña debe estar asociada a una reserva válida.

---

## Validaciones Implementadas

- Validación mediante DTO.
- Verificación de usuario existente.
- Verificación de cancha existente.
- Verificación de reserva existente.
- Manejo centralizado de excepciones.
- Verificación de existencia de registros.

---

## Pruebas Unitarias

Se implementaron pruebas unitarias utilizando:

- JUnit 5
- Mockito

Cobertura de:

- Buscar reseña
- Actualizar reseña
- Eliminar reseña
- Verificar existencia
- Reglas de negocio
- Manejo de excepciones

---

## Documentación Swagger

Swagger UI:

http://localhost:7099/swagger-ui.html

OpenAPI:

http://localhost:7099/v3/api-docs

---

## Autor

Proyecto desarrollado para la asignatura Fullstack I.

Duoc UC - Ingeniería en Informática.
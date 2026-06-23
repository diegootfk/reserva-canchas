# Mantenimiento Service

## Descripción

Microservicio encargado de la gestión de mantenimientos programados para las canchas deportivas.

Permite registrar, consultar, actualizar y eliminar mantenimientos, validando la existencia de las canchas mediante comunicación entre microservicios.

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

- Crear mantenimientos
- Listar mantenimientos
- Buscar mantenimiento por ID
- Actualizar mantenimientos
- Eliminar mantenimientos
- Verificar existencia de mantenimiento
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

### Crear mantenimiento

POST /mantenimientos

### Listar mantenimientos

GET /mantenimientos

### Buscar mantenimiento por ID

GET /mantenimientos/{id}

### Actualizar mantenimiento

PUT /mantenimientos/{id}

### Eliminar mantenimiento

DELETE /mantenimientos/{id}

### Verificar existencia

GET /mantenimientos/{id}/exists

---

## Ejemplo de JSON

### Crear Mantenimiento

```json
{
  "idCancha": 1,
  "fechaInicio": "2026-06-25",
  "fechaFin": "2026-06-27",
  "descripcion": "Cambio de césped sintético",
  "estado": "PROGRAMADO"
}
```

### Respuesta

```json
{
  "id": 1,
  "idCancha": 1,
  "fechaInicio": "2026-06-25",
  "fechaFin": "2026-06-27",
  "descripcion": "Cambio de césped sintético",
  "estado": "PROGRAMADO"
}
```

---

## Comunicación Entre Microservicios

Antes de registrar un mantenimiento, el sistema valida:

- Existencia de la cancha mediante cancha-service.

Estas validaciones se realizan utilizando Spring WebClient.

---

## HATEOAS

Este microservicio implementa HATEOAS mediante un Assembler dedicado que incorpora enlaces relacionados a cada recurso.

Ejemplo:

```json
{
  "id": 1,
  "descripcion": "Cambio de césped sintético",
  "estado": "PROGRAMADO",
  "_links": {
    "self": {
      "href": "http://localhost:7090/mantenimientos/1"
    },
    "mantenimientos": {
      "href": "http://localhost:7090/mantenimientos"
    },
    "cancha": {
      "href": "http://localhost:7090/canchas/1"
    }
  }
}
```

---

## Reglas de Negocio

- No se puede registrar un mantenimiento para una cancha inexistente.
- Todo mantenimiento debe estar asociado a una cancha válida.
- La fecha de término debe ser posterior a la fecha de inicio.
- Durante un mantenimiento, la cancha puede quedar temporalmente fuera de servicio.

---

## Validaciones Implementadas

- Validación mediante DTO.
- Verificación de cancha existente.
- Manejo centralizado de excepciones.
- Verificación de existencia de registros.

---

## Pruebas Unitarias

Se implementaron pruebas unitarias utilizando:

- JUnit 5
- Mockito

Cobertura de:

- Buscar mantenimiento
- Actualizar mantenimiento
- Eliminar mantenimiento
- Verificar existencia
- Reglas de negocio
- Manejo de excepciones

---

## Documentación Swagger

Swagger UI:

http://localhost:7100/swagger-ui.html

OpenAPI:

http://localhost:7100/v3/api-docs

---

## Autor

Proyecto desarrollado para la asignatura Fullstack I.

Duoc UC - Ingeniería en Informática.
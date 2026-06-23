# Disponibilidad Service

## Descripción

Microservicio encargado de la gestión de disponibilidades de las canchas deportivas.

Permite registrar, consultar, actualizar y eliminar bloques de disponibilidad, validando la existencia de las canchas mediante comunicación entre microservicios.

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

- Crear disponibilidades
- Listar disponibilidades
- Buscar disponibilidad por ID
- Actualizar disponibilidades
- Eliminar disponibilidades
- Verificar existencia de disponibilidad
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

### Crear disponibilidad

POST /disponibilidades

### Listar disponibilidades

GET /disponibilidades

### Buscar disponibilidad por ID

GET /disponibilidades/{id}

### Actualizar disponibilidad

PUT /disponibilidades/{id}

### Eliminar disponibilidad

DELETE /disponibilidades/{id}

### Verificar existencia

GET /disponibilidades/{id}/exists

---

## Ejemplo de JSON

### Crear Disponibilidad

```json
{
  "idCancha": 1,
  "fecha": "2026-06-25",
  "horaInicio": "18:00",
  "horaFin": "20:00",
  "estado": "DISPONIBLE"
}
```

### Respuesta

```json
{
  "id": 1,
  "idCancha": 1,
  "fecha": "2026-06-25",
  "horaInicio": "18:00",
  "horaFin": "20:00",
  "estado": "DISPONIBLE"
}
```

---

## Comunicación Entre Microservicios

Antes de registrar una disponibilidad, el sistema valida:

- Existencia de la cancha mediante cancha-service.

Estas validaciones se realizan utilizando Spring WebClient.

---

## HATEOAS

Este microservicio implementa HATEOAS mediante un Assembler dedicado que incorpora enlaces relacionados a cada recurso.

Ejemplo:

```json
{
  "id": 1,
  "fecha": "2026-06-25",
  "estado": "DISPONIBLE",
  "_links": {
    "self": {
      "href": "http://localhost:7090/disponibilidades/1"
    },
    "disponibilidades": {
      "href": "http://localhost:7090/disponibilidades"
    },
    "cancha": {
      "href": "http://localhost:7090/canchas/1"
    }
  }
}
```

---

## Reglas de Negocio

- No se puede registrar una disponibilidad para una cancha inexistente.
- La fecha es obligatoria.
- La hora de término debe ser posterior a la hora de inicio.
- Toda disponibilidad debe estar asociada a una cancha válida.
- Una disponibilidad representa un bloque horario disponible para reservas futuras.

---

## Validaciones Implementadas

- Validación de fecha obligatoria.
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

- Crear disponibilidad
- Buscar disponibilidad
- Actualizar disponibilidad
- Eliminar disponibilidad
- Verificar existencia
- Reglas de negocio
- Manejo de excepciones

---

## Documentación Swagger

Swagger UI:

http://localhost:7097/swagger-ui.html

OpenAPI:

http://localhost:7097/v3/api-docs

---

## Autor

Proyecto desarrollado para la asignatura Fullstack I.

Duoc UC - Ingeniería en Informática.
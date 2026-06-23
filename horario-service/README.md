# Horario Service

## Descripción

Microservicio encargado de la gestión de horarios de funcionamiento de las canchas deportivas.

Permite registrar, consultar, actualizar y eliminar horarios, validando la existencia de las canchas mediante comunicación entre microservicios.

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

- Crear horarios
- Listar horarios
- Buscar horario por ID
- Actualizar horarios
- Eliminar horarios
- Verificar existencia de horario
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

### Crear horario

POST /horarios

### Listar horarios

GET /horarios

### Buscar horario por ID

GET /horarios/{id}

### Actualizar horario

PUT /horarios/{id}

### Eliminar horario

DELETE /horarios/{id}

### Verificar existencia

GET /horarios/{id}/exists

---

## Ejemplo de JSON

### Crear Horario

```json
{
  "idCancha": 1,
  "diaSemana": "LUNES",
  "horaInicio": "08:00",
  "horaFin": "22:00",
  "estado": "ACTIVO"
}
```

### Respuesta

```json
{
  "id": 1,
  "idCancha": 1,
  "diaSemana": "LUNES",
  "horaInicio": "08:00",
  "horaFin": "22:00",
  "estado": "ACTIVO"
}
```

---

## Comunicación Entre Microservicios

Antes de registrar un horario, el sistema valida:

- Existencia de la cancha mediante cancha-service.

Estas validaciones se realizan utilizando Spring WebClient.

---

## HATEOAS

Este microservicio implementa HATEOAS mediante un Assembler dedicado que incorpora enlaces relacionados a cada recurso.

Ejemplo:

```json
{
  "id": 1,
  "diaSemana": "LUNES",
  "estado": "ACTIVO",
  "_links": {
    "self": {
      "href": "http://localhost:7090/horarios/1"
    },
    "horarios": {
      "href": "http://localhost:7090/horarios"
    },
    "cancha": {
      "href": "http://localhost:7090/canchas/1"
    }
  }
}
```

---

## Reglas de Negocio

- No se puede registrar un horario para una cancha inexistente.
- El día de la semana es obligatorio.
- La hora de término debe ser posterior a la hora de inicio.
- Todo horario debe estar asociado a una cancha válida.

---

## Validaciones Implementadas

- Validación del día de la semana obligatorio.
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

- Crear horario
- Buscar horario
- Actualizar horario
- Eliminar horario
- Verificar existencia
- Reglas de negocio
- Manejo de excepciones

---

## Documentación Swagger

Swagger UI:

http://localhost:7096/swagger-ui.html

OpenAPI:

http://localhost:7096/v3/api-docs

---

## Autor

Proyecto desarrollado para la asignatura Fullstack I.

Duoc UC - Ingeniería en Informática.
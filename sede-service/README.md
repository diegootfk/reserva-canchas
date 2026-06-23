# Sede Service

## Descripción

Microservicio encargado de la gestión de sedes deportivas dentro del sistema de reserva de canchas.

Permite registrar, consultar, actualizar y eliminar sedes, además de verificar su existencia para futuras integraciones con otros microservicios.

---

## Tecnologías Utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- MySQL
- Maven
- Swagger OpenAPI
- Spring HATEOAS
- SLF4J Logger
- JUnit 5
- Mockito

---

## Funcionalidades

- Crear sedes
- Listar sedes
- Buscar sede por ID
- Actualizar sedes
- Eliminar sedes
- Verificar existencia de sede
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

└── config

---

## Endpoints Disponibles

### Crear sede

POST /sedes

### Listar sedes

GET /sedes

### Buscar sede por ID

GET /sedes/{id}

### Actualizar sede

PUT /sedes/{id}

### Eliminar sede

DELETE /sedes/{id}

### Verificar existencia

GET /sedes/{id}/exists

---

## Ejemplo de JSON

### Crear Sede

```json
{
  "nombre": "Sede Central",
  "direccion": "Av. Principal 123",
  "comuna": "Santiago",
  "telefono": "223344556",
  "estado": "ACTIVA"
}
```

### Respuesta

```json
{
  "id": 1,
  "nombre": "Sede Central",
  "direccion": "Av. Principal 123",
  "comuna": "Santiago",
  "telefono": "223344556",
  "estado": "ACTIVA"
}
```

---

## HATEOAS

Este microservicio implementa HATEOAS mediante un Assembler dedicado que incorpora enlaces relacionados a cada recurso.

Ejemplo:

```json
{
  "id": 1,
  "nombre": "Sede Central",
  "_links": {
    "self": {
      "href": "http://localhost:7090/sedes/1"
    },
    "sedes": {
      "href": "http://localhost:7090/sedes"
    },
    "existe": {
      "href": "http://localhost:7090/sedes/1/exists"
    }
  }
}
```

---

## Validaciones Implementadas

- Nombre obligatorio.
- Validación mediante DTO.
- Manejo centralizado de excepciones.
- Verificación de existencia de registros.

---

## Pruebas Unitarias

Se implementaron pruebas unitarias utilizando:

- JUnit 5
- Mockito

Cobertura de:

- Crear sede
- Buscar sede
- Actualizar sede
- Eliminar sede
- Verificar existencia
- Manejo de excepciones

---

## Documentación Swagger

Swagger UI:

http://localhost:7095/swagger-ui.html

OpenAPI:

http://localhost:7095/v3/api-docs

---

## Autor

Proyecto desarrollado para la asignatura Fullstack I.

Duoc UC - Ingeniería en Informática.
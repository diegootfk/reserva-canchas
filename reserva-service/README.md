# Reserva Service

## Descripción

Microservicio encargado de la gestión de reservas dentro del sistema de reserva de canchas deportivas.

Permite registrar, consultar, actualizar y eliminar reservas, validando la existencia de usuarios y canchas mediante comunicación entre microservicios.

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

- Crear reservas
- Listar reservas
- Buscar reserva por ID
- Actualizar reservas
- Eliminar reservas
- Verificar existencia de reserva
- Buscar reservas por estado
- Buscar reservas por usuario
- Buscar reservas por cancha
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

### Crear reserva

POST /reservas

### Listar reservas

GET /reservas

### Buscar reserva por ID

GET /reservas/{id}

### Actualizar reserva

PUT /reservas/{id}

### Eliminar reserva

DELETE /reservas/{id}

### Verificar existencia

GET /reservas/{id}/exists

### Buscar por estado

GET /reservas/estado/{estado}

### Buscar por usuario

GET /reservas/usuario/{idUsuario}

### Buscar por cancha

GET /reservas/cancha/{idCancha}

---

## Ejemplo de JSON

### Crear Reserva

```json
{
  "idUsuario": 1,
  "idCancha": 2,
  "total": 25000
}
```

### Respuesta

```json
{
  "id": 1,
  "idUsuario": 1,
  "idCancha": 2,
  "total": 25000,
  "estado": "CONFIRMADA"
}
```

---

## Comunicación Entre Microservicios

Antes de registrar una reserva, el sistema valida:

- Existencia del usuario mediante usuario-service.
- Existencia de la cancha mediante cancha-service.

Estas validaciones se realizan utilizando Spring WebClient.

---

## HATEOAS

Este microservicio implementa HATEOAS mediante un Assembler dedicado que incorpora enlaces relacionados a cada recurso.

Ejemplo:

```json
{
  "id": 1,
  "idUsuario": 1,
  "idCancha": 2,
  "total": 25000,
  "estado": "CONFIRMADA",
  "_links": {
    "self": {
      "href": "http://localhost:7090/reservas/1"
    },
    "reservas": {
      "href": "http://localhost:7090/reservas"
    },
    "usuario": {
      "href": "http://localhost:7090/usuarios/1"
    },
    "cancha": {
      "href": "http://localhost:7090/canchas/2"
    }
  }
}
```

---

## Reglas de Negocio

- No se puede registrar una reserva para un usuario inexistente.
- No se puede registrar una reserva para una cancha inexistente.
- Toda reserva creada queda automáticamente con estado:
  
  ```text
  CONFIRMADA
  ```

- El total de la reserva debe ser mayor a cero.

---

## Validaciones Implementadas

- Validación mediante DTO.
- Verificación de usuario existente.
- Verificación de cancha existente.
- Manejo centralizado de excepciones.
- Verificación de existencia de registros.

---

## Pruebas Unitarias

Se implementaron pruebas unitarias utilizando:

- JUnit 5
- Mockito

Cobertura de:

- Buscar reserva
- Actualizar reserva
- Eliminar reserva
- Verificar existencia
- Buscar por estado
- Buscar por usuario
- Buscar por cancha
- Reglas de negocio
- Manejo de excepciones

---

## Documentación Swagger

Swagger UI:

http://localhost:7093/swagger-ui.html

OpenAPI:

http://localhost:7093/v3/api-docs

---

## Autor

Proyecto desarrollado para la asignatura Fullstack I.

Duoc UC - Ingeniería en Informática.s
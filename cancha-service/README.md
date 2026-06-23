# Cancha Service

## Descripción

Microservicio encargado de la gestión de canchas deportivas dentro del sistema de reserva de canchas.

Permite registrar, consultar, actualizar y eliminar canchas, además de servir como entidad principal para reservas, horarios, disponibilidades, mantenimientos y reseñas.

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

- Crear canchas
- Listar canchas
- Buscar cancha por ID
- Actualizar canchas
- Eliminar canchas
- Verificar existencia de cancha
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

### Crear cancha

POST /canchas

### Listar canchas

GET /canchas

### Buscar cancha por ID

GET /canchas/{id}

### Actualizar cancha

PUT /canchas/{id}

### Eliminar cancha

DELETE /canchas/{id}

### Verificar existencia

GET /canchas/{id}/exists

---

## Ejemplo de JSON

### Crear Cancha

```json
{
  "nombre": "Cancha Futbolito 1",
  "tipoCancha": "FUTBOLITO",
  "precioHora": 25000,
  "capacidad": 14,
  "estado": "DISPONIBLE"
}
```

### Respuesta

```json
{
  "id": 1,
  "nombre": "Cancha Futbolito 1",
  "tipoCancha": "FUTBOLITO",
  "precioHora": 25000,
  "capacidad": 14,
  "estado": "DISPONIBLE"
}
```

---

## HATEOAS

Este microservicio implementa HATEOAS mediante un Assembler dedicado que incorpora enlaces relacionados a cada recurso.

Ejemplo:

```json
{
  "id": 1,
  "nombre": "Cancha Futbolito 1",
  "estado": "DISPONIBLE",
  "_links": {
    "self": {
      "href": "http://localhost:7090/canchas/1"
    },
    "canchas": {
      "href": "http://localhost:7090/canchas"
    },
    "reservas": {
      "href": "http://localhost:7090/reservas/cancha/1"
    },
    "disponibilidades": {
      "href": "http://localhost:7090/disponibilidades/cancha/1"
    },
    "horarios": {
      "href": "http://localhost:7090/horarios/cancha/1"
    },
    "resenas": {
      "href": "http://localhost:7090/resenas/cancha/1"
    },
    "mantenimientos": {
      "href": "http://localhost:7090/mantenimientos/cancha/1"
    }
  }
}
```

---

## Reglas de Negocio

- El nombre de la cancha es obligatorio.
- Toda cancha debe tener un tipo definido.
- El precio por hora debe ser mayor a cero.
- La capacidad debe ser mayor a cero.
- Toda cancha debe encontrarse en un estado válido.
- Una cancha puede asociarse a reservas, horarios, disponibilidades, mantenimientos y reseñas.

---

## Validaciones Implementadas

- Validación de nombre obligatorio.
- Validación mediante DTO.
- Manejo centralizado de excepciones.
- Verificación de existencia de registros.

---

## Pruebas Unitarias

Se implementaron pruebas unitarias utilizando:

- JUnit 5
- Mockito

Cobertura de:

- Crear cancha
- Buscar cancha
- Actualizar cancha
- Eliminar cancha
- Verificar existencia
- Reglas de negocio
- Manejo de excepciones

---

## Documentación Swagger

Swagger UI:

http://localhost:7092/swagger-ui.html

OpenAPI:

http://localhost:7092/v3/api-docs

---

## Autor

Proyecto desarrollado para la asignatura Fullstack I.

Duoc UC - Ingeniería en Informática.s
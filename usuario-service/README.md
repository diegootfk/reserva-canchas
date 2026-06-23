# Usuario Service

## Descripción

Microservicio encargado de la gestión de usuarios dentro del sistema de reserva de canchas deportivas.

Permite registrar, consultar, actualizar y eliminar usuarios, además de verificar su existencia para la comunicación entre microservicios.

---

## Tecnologías Utilizadas

* Java 21
* Spring Boot
* Spring Data JPA
* MySQL
* Maven
* Swagger OpenAPI
* Spring HATEOAS
* SLF4J Logger
* JUnit 5
* Mockito

---

## Funcionalidades

* Crear usuarios
* Listar usuarios
* Buscar usuario por ID
* Actualizar usuarios
* Eliminar usuarios
* Verificar existencia de usuario
* Documentación Swagger
* Soporte HATEOAS
* Registro de logs

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

### Crear usuario

POST /usuarios

### Listar usuarios

GET /usuarios

### Buscar usuario por ID

GET /usuarios/{id}

### Actualizar usuario

PUT /usuarios/{id}

### Eliminar usuario

DELETE /usuarios/{id}

### Verificar existencia

GET /usuarios/{id}/exists

---

## Ejemplo de JSON

### Crear Usuario

```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan@gmail.com",
  "password": "123456",
  "telefono": "987654321",
  "estado": "ACTIVO",
  "idRol": 1
}
```

### Respuesta

```json
{
  "id": 1,
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan@gmail.com",
  "telefono": "987654321",
  "estado": "ACTIVO",
  "idRol": 1
}
```

---

## HATEOAS

Este microservicio implementa HATEOAS mediante un Assembler dedicado que incorpora enlaces relacionados a cada recurso.

Ejemplo:

```json
{
  "id": 1,
  "nombre": "Juan",
  "_links": {
    "self": {
      "href": "http://localhost:7090/usuarios/1"
    },
    "usuarios": {
      "href": "http://localhost:7090/usuarios"
    },
    "existe": {
      "href": "http://localhost:7090/usuarios/1/exists"
    }
  }
}
```

---

## Validaciones Implementadas

* Email obligatorio.
* Validación mediante DTO.
* Manejo centralizado de excepciones.
* Verificación de existencia de registros.

---

## Pruebas Unitarias

Se implementaron pruebas unitarias utilizando:

* JUnit 5
* Mockito

Cobertura de:

* Crear usuario
* Buscar usuario
* Actualizar usuario
* Eliminar usuario
* Verificar existencia
* Manejo de excepciones

---

## Documentación Swagger

Swagger UI:

http://localhost:7091/swagger-ui.html

OpenAPI:

http://localhost:7091/v3/api-docs

---

## Autor

Proyecto desarrollado para la asignatura Fullstack I.

Duoc UC - Ingeniería en Informática.

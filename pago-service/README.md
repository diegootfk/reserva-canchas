# Pago Service

## Descripción

Microservicio encargado de la gestión de pagos asociados a las reservas de canchas deportivas.

Permite registrar, consultar, actualizar y eliminar pagos, validando la existencia de la reserva asociada mediante comunicación entre microservicios. Además, incorpora reglas de negocio para el cálculo de IVA, descuentos y total a pagar.

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

- Registrar pagos
- Listar pagos
- Buscar pago por ID
- Actualizar pagos
- Eliminar pagos
- Verificar existencia de pago
- Buscar pagos por método de pago
- Buscar pagos por estado
- Buscar pagos por reserva
- Cálculo de IVA
- Aplicación de descuentos
- Cálculo de total con IVA
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

### Registrar pago

POST /pagos

### Listar pagos

GET /pagos

### Buscar pago por ID

GET /pagos/{id}

### Actualizar pago

PUT /pagos/{id}

### Eliminar pago

DELETE /pagos/{id}

### Verificar existencia

GET /pagos/{id}/exists

### Buscar por método de pago

GET /pagos/metodo/{metodoPago}

### Buscar por estado

GET /pagos/estado/{estadoPago}

### Buscar por reserva

GET /pagos/reserva/{idReserva}

---

## Ejemplo de JSON

### Registrar Pago

```json
{
  "idReserva": 1,
  "monto": 25000,
  "metodoPago": "TARJETA"
}
```

### Respuesta

```json
{
  "id": 1,
  "idReserva": 1,
  "monto": 25000,
  "metodoPago": "TARJETA",
  "estadoPago": "PAGADO"
}
```

---

## Comunicación Entre Microservicios

Antes de registrar un pago, el sistema valida:

- Existencia de la reserva mediante reserva-service.

Estas validaciones se realizan utilizando Spring WebClient.

---

## HATEOAS

Este microservicio implementa HATEOAS mediante un Assembler dedicado que incorpora enlaces relacionados a cada recurso.

Ejemplo:

```json
{
  "id": 1,
  "monto": 25000,
  "estadoPago": "PAGADO",
  "_links": {
    "self": {
      "href": "http://localhost:7090/pagos/1"
    },
    "pagos": {
      "href": "http://localhost:7090/pagos"
    },
    "reserva": {
      "href": "http://localhost:7090/reservas/1"
    }
  }
}
```

---

## Reglas de Negocio

- No se puede registrar un pago para una reserva inexistente.
- Todo pago creado queda automáticamente con estado:

```text
PAGADO
```

- El IVA aplicado corresponde al 19%.
- Se pueden aplicar descuentos porcentuales al monto original.
- El total final puede calcularse automáticamente considerando IVA.

---

## Métodos de Negocio Implementados

### Calcular IVA

```java
calcularIva(double montoNeto)
```

Calcula el 19% de IVA sobre un monto neto.

### Aplicar Descuento

```java
aplicarDescuento(double montoOriginal, double porcentajeDescuento)
```

Calcula el monto final aplicando un porcentaje de descuento.

### Calcular Total con IVA

```java
calcularTotalConIva(double montoNeto)
```

Obtiene el total sumando el IVA correspondiente.

---

## Validaciones Implementadas

- Validación mediante DTO.
- Verificación de reserva existente.
- Manejo centralizado de excepciones.
- Verificación de existencia de registros.

---

## Pruebas Unitarias

Se implementaron pruebas unitarias utilizando:

- JUnit 5
- Mockito

Cobertura de:

- Buscar pago
- Actualizar pago
- Eliminar pago
- Verificar existencia
- Buscar por método de pago
- Buscar por estado
- Buscar por reserva
- Cálculo de IVA
- Aplicación de descuentos
- Cálculo de total con IVA
- Reglas de negocio
- Manejo de excepciones

---

## Documentación Swagger

Swagger UI:

http://localhost:7094/swagger-ui.html

OpenAPI:

http://localhost:7094/v3/api-docs

---

## Autor

Proyecto desarrollado para la asignatura Fullstack I.

Duoc UC - Ingeniería en Informática.
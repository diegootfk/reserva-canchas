# Sistema de Reserva de Canchas Deportivas

## Descripción General

Sistema desarrollado bajo una arquitectura de microservicios para la gestión de reservas de canchas deportivas.

La plataforma permite administrar usuarios, sedes, canchas, horarios, disponibilidades, reservas, pagos, reseñas, mantenimientos y notificaciones, incorporando autenticación mediante JWT y comunicación entre microservicios utilizando Spring WebClient.

---

## Objetivo del Proyecto

Desarrollar una solución escalable y desacoplada que permita gestionar reservas de canchas deportivas mediante una arquitectura moderna basada en microservicios.

---

## Arquitectura del Sistema

```text
                           ┌──────────────────┐
                           │     Cliente      │
                           └────────┬─────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │     API Gateway     │
                         └─────────┬───────────┘
                                   │
      ┌────────────────────────────┼────────────────────────────┐
      │                            │                            │
      ▼                            ▼                            ▼

┌─────────────┐          ┌────────────────┐          ┌─────────────┐
│ Auth Service│          │ Usuario Service│          │ Sede Service│
└─────────────┘          └────────────────┘          └─────────────┘

      │
      ▼

┌─────────────┐
│CanchaService│
└──────┬──────┘
       │
       ├──────────────┐
       │              │
       ▼              ▼

┌─────────────┐   ┌─────────────┐
│HorarioServ.│   │Disponib.Serv│
└─────────────┘   └─────────────┘

       │
       ▼

┌─────────────┐
│ReservaServ.│
└──────┬──────┘
       │
       ├───────────┬───────────┐
       │           │           │
       ▼           ▼           ▼

┌───────────┐ ┌───────────┐ ┌──────────────┐
│PagoServ.  │ │ReseñaServ.│ │Notificación  │
└───────────┘ └───────────┘ └──────────────┘

       │
       ▼

┌─────────────────┐
│MantenimientoServ│
└─────────────────┘
```

---

## Tecnologías Utilizadas

### Backend

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security
- Spring Cloud Gateway
- Spring WebClient
- Spring HATEOAS

### Base de Datos

- MySQL

### Documentación

- Swagger OpenAPI

### Testing

- JUnit 5
- Mockito

### Gestión del Proyecto

- Git
- GitHub
- Maven

---

## Microservicios Implementados

| Microservicio | Responsabilidad |
|--------------|----------------|
| Auth Service | Autenticación y generación de JWT |
| API Gateway | Punto único de acceso y validación de tokens |
| Usuario Service | Gestión de usuarios |
| Sede Service | Gestión de sedes deportivas |
| Cancha Service | Gestión de canchas |
| Horario Service | Gestión de horarios |
| Disponibilidad Service | Gestión de disponibilidades |
| Reserva Service | Gestión de reservas |
| Pago Service | Gestión de pagos |
| Reseña Service | Gestión de reseñas |
| Notificación Service | Gestión de notificaciones |
| Mantenimiento Service | Gestión de mantenimientos |

---

## Funcionalidades Principales

### Gestión de Usuarios

- Registro de usuarios.
- Administración de perfiles.
- Validación de existencia.

### Gestión de Canchas

- Registro de canchas.
- Configuración de horarios.
- Control de disponibilidad.

### Gestión de Reservas

- Creación de reservas.
- Validación de usuarios.
- Validación de canchas.

### Gestión de Pagos

- Registro de pagos.
- Cálculo de IVA.
- Aplicación de descuentos.

### Gestión de Reseñas

- Evaluación de canchas.
- Comentarios de usuarios.

### Gestión de Notificaciones

- Notificaciones asociadas a reservas.
- Control de estados.

### Gestión de Mantenimientos

- Programación de mantenimientos.
- Seguimiento de estado.

---

## Seguridad

La plataforma implementa autenticación y autorización mediante JWT.

### Características

- Login seguro.
- Tokens JWT.
- Roles USER y ADMIN.
- Protección de endpoints.
- Validación centralizada en API Gateway.

### Flujo de Seguridad

```text
Usuario
   │
   ▼
Login
   │
   ▼
Auth Service
   │
   ▼
Generación JWT
   │
   ▼
API Gateway
   │
   ▼
Microservicios
```

---

## Comunicación Entre Microservicios

La comunicación entre microservicios se implementa utilizando:

```text
Spring WebClient
```

### Validaciones entre servicios

Reserva Service valida:

- Usuario existente.
- Cancha existente.

Pago Service valida:

- Reserva existente.

Reseña Service valida:

- Usuario existente.
- Cancha existente.
- Reserva existente.

Notificación Service valida:

- Usuario existente.
- Reserva existente.

Horario Service valida:

- Cancha existente.

Disponibilidad Service valida:

- Cancha existente.

Mantenimiento Service valida:

- Cancha existente.

---

## HATEOAS

Todos los microservicios implementan HATEOAS mediante Assemblers dedicados.

Beneficios:

- Navegación dinámica entre recursos.
- APIs más descriptivas.
- Menor acoplamiento cliente-servidor.

---

## Reglas de Negocio Implementadas

### Usuario

- Email obligatorio.

### Sede

- Nombre obligatorio.

### Cancha

- Nombre obligatorio.
- Precio mayor a cero.

### Horario

- Día de la semana obligatorio.

### Disponibilidad

- Fecha obligatoria.

### Reserva

- Usuario debe existir.
- Cancha debe existir.
- Estado inicial: CONFIRMADA.

### Pago

- Reserva debe existir.
- Estado inicial: PAGADO.
- IVA del 19%.
- Aplicación de descuentos.

### Reseña

- Usuario debe existir.
- Cancha debe existir.
- Reserva debe existir.

### Notificación

- Usuario debe existir.
- Reserva debe existir.
- Estado inicial: ENVIADA.

### Mantenimiento

- Cancha debe existir.

---

## Testing

Se implementaron pruebas unitarias utilizando:

- JUnit 5
- Mockito

Cobertura:

- CRUD completo.
- Reglas de negocio.
- Manejo de excepciones.
- Validaciones.
- Servicios de dominio.

---

## Documentación API

Cada microservicio incorpora documentación Swagger.

Ejemplos:

```text
http://localhost:7090/swagger-ui.html
http://localhost:7091/swagger-ui.html
http://localhost:7092/swagger-ui.html
```

---

## Estructura General del Proyecto

```text
Proyecto

├── api-gateway
├── auth-service
├── usuario-service
├── sede-service
├── cancha-service
├── horario-service
├── disponibilidad-service
├── reserva-service
├── pago-service
├── resena-service
├── notificacion-service
├── mantenimiento-service
└── README.md
```

---

## Características Destacadas

- Arquitectura basada en microservicios.
- Seguridad con JWT.
- API Gateway centralizado.
- Comunicación mediante WebClient.
- Documentación Swagger.
- HATEOAS mediante Assemblers.
- Persistencia con MySQL.
- Testing con JUnit y Mockito.
- Manejo centralizado de excepciones.
- Registro de logs mediante SLF4J.

---

## Autores

Proyecto desarrollado para la asignatura Fullstack I.

Duoc UC – Ingeniería en Informática.

Integrantes:

- Diego
- Oscar
- Matías
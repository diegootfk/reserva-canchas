package com.reservacanchas.cl.notificacion_service.controller;

import com.reservacanchas.cl.notificacion_service.model.Notificacion;
import com.reservacanchas.cl.notificacion_service.service.NotificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(
        name = "Notificaciones",
        description = "Operaciones para la gestión de notificaciones del sistema"
)
@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionController.class);

    private static final String API_GATEWAY = "http://localhost:7090";

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @Operation(
            summary = "Crear notificación",
            description = "Registra una nueva notificación en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Notificación creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Notificacion> crear(@RequestBody Notificacion notificacion) {

        logger.info(
                "Solicitud POST recibida para crear notificación para usuario ID: {} y reserva ID: {}",
                notificacion.getIdUsuario(),
                notificacion.getIdReserva()
        );

        Notificacion notificacionGuardada = notificacionService.guardar(notificacion);

        logger.info("Notificación creada correctamente con ID: {}", notificacionGuardada.getId());

        return new ResponseEntity<>(
                notificacionGuardada,
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Listar notificaciones",
            description = "Obtiene todas las notificaciones registradas con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Notificacion>>> listar() {

        logger.info("Solicitud GET recibida para listar todas las notificaciones");

        List<Notificacion> notificacionesEncontradas = notificacionService.listar();

        logger.info("Se encontraron {} notificaciones registradas", notificacionesEncontradas.size());

        List<EntityModel<Notificacion>> notificaciones = notificacionesEncontradas
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Notificacion>> respuesta = CollectionModel.of(
                notificaciones,
                Link.of(API_GATEWAY + "/notificaciones").withSelfRel()
        );

        logger.info("Respuesta HATEOAS generada correctamente para listado de notificaciones");

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar notificación por ID",
            description = "Obtiene una notificación específica mediante su identificador con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Notificacion>> buscarPorId(@PathVariable Long id) {

        logger.info("Solicitud GET recibida para buscar notificación con ID: {}", id);

        Notificacion notificacion = notificacionService.buscarPorId(id);

        logger.info("Notificación encontrada correctamente con ID: {}", id);

        return ResponseEntity.ok(agregarLinks(notificacion));
    }

    @Operation(
            summary = "Actualizar notificación",
            description = "Actualiza los datos de una notificación existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Notificacion> actualizar(
            @PathVariable Long id,
            @RequestBody Notificacion notificacion) {

        logger.info("Solicitud PUT recibida para actualizar notificación con ID: {}", id);

        Notificacion notificacionActualizada = notificacionService.actualizar(id, notificacion);

        logger.info("Notificación actualizada correctamente con ID: {}", id);

        return ResponseEntity.ok(notificacionActualizada);
    }

    @Operation(
            summary = "Eliminar notificación",
            description = "Elimina una notificación según su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Notificación eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        logger.warn("Solicitud DELETE recibida para eliminar notificación con ID: {}", id);

        notificacionService.eliminar(id);

        logger.info("Notificación eliminada correctamente con ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de notificación",
            description = "Indica si una notificación existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        logger.info("Solicitud GET recibida para verificar existencia de notificación con ID: {}", id);

        boolean existe = notificacionService.existePorId(id);

        logger.info("Resultado de existencia para notificación ID {}: {}", id, existe);

        return existe;
    }

    private EntityModel<Notificacion> agregarLinks(Notificacion notificacion) {

        logger.debug("Agregando enlaces HATEOAS para notificación con ID: {}", notificacion.getId());

        return EntityModel.of(
                notificacion,
                Link.of(API_GATEWAY + "/notificaciones/" + notificacion.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/notificaciones").withRel("notificaciones"),
                Link.of(API_GATEWAY + "/notificaciones/" + notificacion.getId() + "/exists").withRel("existe"),
                Link.of(API_GATEWAY + "/usuarios/" + notificacion.getIdUsuario()).withRel("usuario"),
                Link.of(API_GATEWAY + "/reservas/" + notificacion.getIdReserva()).withRel("reserva")
        );
    }
}
package com.reservacanchas.cl.notificacion_service.controller;

import com.reservacanchas.cl.notificacion_service.model.Notificacion;
import com.reservacanchas.cl.notificacion_service.service.NotificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Notificaciones",
        description = "Operaciones para la gestión de notificaciones del sistema"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

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

        return new ResponseEntity<>(
                notificacionService.guardar(notificacion),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Listar notificaciones",
            description = "Obtiene todas las notificaciones registradas"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<Notificacion>> listar() {

        return ResponseEntity.ok(notificacionService.listar());
    }

    @Operation(
            summary = "Buscar notificación por ID",
            description = "Obtiene una notificación específica mediante su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(notificacionService.buscarPorId(id));
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

        return ResponseEntity.ok(
                notificacionService.actualizar(id, notificacion)
        );
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

        notificacionService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de notificación",
            description = "Indica si una notificación existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        return notificacionService.existePorId(id);
    }
}
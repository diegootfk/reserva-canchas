package com.reservacanchas.cl.disponibilidad_service.controller;

import com.reservacanchas.cl.disponibilidad_service.model.Disponibilidad;
import com.reservacanchas.cl.disponibilidad_service.service.DisponibilidadService;

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
        name = "Disponibilidades",
        description = "Operaciones para la gestión de disponibilidades de canchas"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/disponibilidades")
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    public DisponibilidadController(DisponibilidadService disponibilidadService) {
        this.disponibilidadService = disponibilidadService;
    }

    @Operation(
            summary = "Crear disponibilidad",
            description = "Registra una nueva disponibilidad en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Disponibilidad creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Disponibilidad> crear(@RequestBody Disponibilidad disponibilidad) {
        return new ResponseEntity<>(
                disponibilidadService.guardar(disponibilidad),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Listar disponibilidades",
            description = "Obtiene todas las disponibilidades registradas"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<Disponibilidad>> listar() {
        return ResponseEntity.ok(disponibilidadService.listar());
    }

    @Operation(
            summary = "Buscar disponibilidad por ID",
            description = "Obtiene una disponibilidad específica mediante su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disponibilidad encontrada"),
            @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Disponibilidad> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(disponibilidadService.buscarPorId(id));
    }

    @Operation(
            summary = "Actualizar disponibilidad",
            description = "Actualiza los datos de una disponibilidad existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disponibilidad actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Disponibilidad> actualizar(
            @PathVariable Long id,
            @RequestBody Disponibilidad disponibilidad) {

        return ResponseEntity.ok(
                disponibilidadService.actualizar(id, disponibilidad)
        );
    }

    @Operation(
            summary = "Eliminar disponibilidad",
            description = "Elimina una disponibilidad según su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Disponibilidad eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        disponibilidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de disponibilidad",
            description = "Indica si una disponibilidad existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {
        return disponibilidadService.existePorId(id);
    }
}
package com.reservacanchas.cl.mantenimiento_service.controller;

import com.reservacanchas.cl.mantenimiento_service.model.Mantenimiento;
import com.reservacanchas.cl.mantenimiento_service.service.MantenimientoService;

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
        name = "Mantenimientos",
        description = "Operaciones para la gestión de mantenimientos de canchas"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/mantenimientos")
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;

    public MantenimientoController(MantenimientoService mantenimientoService) {
        this.mantenimientoService = mantenimientoService;
    }

    @Operation(
            summary = "Crear mantenimiento",
            description = "Registra un nuevo mantenimiento en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Mantenimiento creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Mantenimiento> crear(@RequestBody Mantenimiento mantenimiento) {

        return new ResponseEntity<>(
                mantenimientoService.guardar(mantenimiento),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Listar mantenimientos",
            description = "Obtiene todos los mantenimientos registrados"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<Mantenimiento>> listar() {

        return ResponseEntity.ok(mantenimientoService.listar());
    }

    @Operation(
            summary = "Buscar mantenimiento por ID",
            description = "Obtiene un mantenimiento específico mediante su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mantenimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Mantenimiento> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(mantenimientoService.buscarPorId(id));
    }

    @Operation(
            summary = "Actualizar mantenimiento",
            description = "Actualiza los datos de un mantenimiento existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mantenimiento actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Mantenimiento> actualizar(
            @PathVariable Long id,
            @RequestBody Mantenimiento mantenimiento) {

        return ResponseEntity.ok(
                mantenimientoService.actualizar(id, mantenimiento)
        );
    }

    @Operation(
            summary = "Eliminar mantenimiento",
            description = "Elimina un mantenimiento según su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Mantenimiento eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        mantenimientoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de mantenimiento",
            description = "Indica si un mantenimiento existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        return mantenimientoService.existePorId(id);
    }
}
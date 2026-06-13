package com.reservacanchas.cl.sede_service.controller;

import com.reservacanchas.cl.sede_service.model.Sede;
import com.reservacanchas.cl.sede_service.service.SedeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Sedes",
        description = "Operaciones para la gestión de sedes deportivas registradas en el sistema"
)
@RestController
@RequestMapping("/sedes")
public class SedeController {

    private final SedeService sedeService;

    public SedeController(SedeService sedeService) {
        this.sedeService = sedeService;
    }

    @Operation(
            summary = "Crear sede",
            description = "Registra una nueva sede deportiva en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sede creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Sede> crear(@RequestBody Sede sede) {

        return new ResponseEntity<>(
                sedeService.guardar(sede),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Listar sedes",
            description = "Obtiene todas las sedes registradas en el sistema"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<Sede>> listar() {

        return ResponseEntity.ok(sedeService.listar());
    }

    @Operation(
            summary = "Buscar sede por ID",
            description = "Obtiene una sede específica mediante su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sede encontrada"),
            @ApiResponse(responseCode = "404", description = "Sede no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Sede> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(sedeService.buscarPorId(id));
    }

    @Operation(
            summary = "Actualizar sede",
            description = "Actualiza la información de una sede existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sede actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Sede no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Sede> actualizar(
            @PathVariable Long id,
            @RequestBody Sede sede) {

        return ResponseEntity.ok(
                sedeService.actualizar(id, sede)
        );
    }

    @Operation(
            summary = "Eliminar sede",
            description = "Elimina una sede según su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sede eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Sede no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        sedeService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de sede",
            description = "Indica si una sede existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        return sedeService.existePorId(id);
    }
}
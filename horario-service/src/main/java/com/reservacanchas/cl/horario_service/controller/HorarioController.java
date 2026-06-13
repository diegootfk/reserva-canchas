package com.reservacanchas.cl.horario_service.controller;

import com.reservacanchas.cl.horario_service.model.Horario;
import com.reservacanchas.cl.horario_service.service.HorarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Horarios",
        description = "Operaciones para la gestión de horarios de canchas"
)
@RestController
@RequestMapping("/horarios")
public class HorarioController {

    private final HorarioService horarioService;

    public HorarioController(HorarioService horarioService) {
        this.horarioService = horarioService;
    }

    @Operation(
            summary = "Crear horario",
            description = "Registra un nuevo horario en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Horario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Horario> crear(@RequestBody Horario horario) {

        return new ResponseEntity<>(
                horarioService.guardar(horario),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Listar horarios",
            description = "Obtiene todos los horarios registrados"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<Horario>> listar() {

        return ResponseEntity.ok(horarioService.listar());
    }

    @Operation(
            summary = "Buscar horario por ID",
            description = "Obtiene un horario específico mediante su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horario encontrado"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Horario> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(horarioService.buscarPorId(id));
    }

    @Operation(
            summary = "Actualizar horario",
            description = "Actualiza los datos de un horario existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horario actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Horario> actualizar(
            @PathVariable Long id,
            @RequestBody Horario horario) {

        return ResponseEntity.ok(
                horarioService.actualizar(id, horario)
        );
    }

    @Operation(
            summary = "Eliminar horario",
            description = "Elimina un horario según su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Horario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        horarioService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de horario",
            description = "Indica si un horario existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        return horarioService.existePorId(id);
    }
}
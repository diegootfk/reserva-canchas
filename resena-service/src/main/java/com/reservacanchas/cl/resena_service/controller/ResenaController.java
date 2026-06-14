package com.reservacanchas.cl.resena_service.controller;

import com.reservacanchas.cl.resena_service.model.Resena;
import com.reservacanchas.cl.resena_service.service.ResenaService;

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
        name = "Reseñas",
        description = "Operaciones para la gestión de reseñas de usuarios sobre las canchas"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/resenas")
public class ResenaController {

    private final ResenaService resenaService;

    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    @Operation(
            summary = "Crear reseña",
            description = "Registra una nueva reseña en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reseña creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Resena> crear(@RequestBody Resena resena) {

        return new ResponseEntity<>(
                resenaService.guardar(resena),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Listar reseñas",
            description = "Obtiene todas las reseñas registradas"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<Resena>> listar() {

        return ResponseEntity.ok(resenaService.listar());
    }

    @Operation(
            summary = "Buscar reseña por ID",
            description = "Obtiene una reseña específica mediante su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña encontrada"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Resena> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(resenaService.buscarPorId(id));
    }

    @Operation(
            summary = "Actualizar reseña",
            description = "Actualiza los datos de una reseña existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Resena> actualizar(
            @PathVariable Long id,
            @RequestBody Resena resena) {

        return ResponseEntity.ok(
                resenaService.actualizar(id, resena)
        );
    }

    @Operation(
            summary = "Eliminar reseña",
            description = "Elimina una reseña según su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reseña eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        resenaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de reseña",
            description = "Indica si una reseña existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        return resenaService.existePorId(id);
    }
}
package com.reservacanchas.cl.cancha_service.controller;

import com.reservacanchas.cl.cancha_service.dto.CanchaDTO;
import com.reservacanchas.cl.cancha_service.model.Cancha;
import com.reservacanchas.cl.cancha_service.service.CanchaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Canchas",
        description = "Operaciones para la gestión de canchas deportivas"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/canchas")
public class CanchaController {

    private final CanchaService canchaService;

    public CanchaController(CanchaService canchaService) {
        this.canchaService = canchaService;
    }

    @Operation(
            summary = "Crear cancha",
            description = "Registra una nueva cancha en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cancha creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Cancha> crear(@Valid @RequestBody CanchaDTO canchaDTO) {

        Cancha cancha = canchaService.guardar(canchaDTO);

        return new ResponseEntity<>(cancha, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar canchas",
            description = "Obtiene todas las canchas registradas"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<Cancha>> listar() {

        return ResponseEntity.ok(canchaService.listar());
    }

    @Operation(
            summary = "Buscar cancha por ID",
            description = "Obtiene una cancha específica mediante su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cancha encontrada"),
            @ApiResponse(responseCode = "404", description = "Cancha no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Cancha> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(canchaService.buscarPorId(id));
    }

    @Operation(
            summary = "Actualizar cancha",
            description = "Actualiza los datos de una cancha existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cancha actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Cancha no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Cancha> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CanchaDTO canchaDTO
    ) {

        return ResponseEntity.ok(canchaService.actualizar(id, canchaDTO));
    }

    @Operation(
            summary = "Eliminar cancha",
            description = "Elimina una cancha según su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cancha eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Cancha no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        canchaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de cancha",
            description = "Indica si una cancha existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        return canchaService.existePorId(id);
    }
}
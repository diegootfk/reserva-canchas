package com.reservacanchas.cl.disponibilidad_service.controller;

import com.reservacanchas.cl.disponibilidad_service.dto.DisponibilidadDTO;
import com.reservacanchas.cl.disponibilidad_service.model.Disponibilidad;
import com.reservacanchas.cl.disponibilidad_service.service.DisponibilidadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(
        name = "Disponibilidades",
        description = "Operaciones para la gestión de disponibilidades de canchas"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/disponibilidades")
public class DisponibilidadController {

    private static final String API_GATEWAY = "http://localhost:7090";

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
    public ResponseEntity<Disponibilidad> crear(
            @Valid @RequestBody DisponibilidadDTO disponibilidadDTO) {

        return new ResponseEntity<>(
                disponibilidadService.guardar(disponibilidadDTO),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Listar disponibilidades",
            description = "Obtiene todas las disponibilidades registradas con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Disponibilidad>>> listar() {

        List<EntityModel<Disponibilidad>> disponibilidades = disponibilidadService.listar()
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Disponibilidad>> respuesta = CollectionModel.of(
                disponibilidades,
                Link.of(API_GATEWAY + "/disponibilidades").withSelfRel()
        );

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar disponibilidad por ID",
            description = "Obtiene una disponibilidad específica mediante su identificador con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disponibilidad encontrada"),
            @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Disponibilidad>> buscarPorId(@PathVariable Long id) {

        Disponibilidad disponibilidad = disponibilidadService.buscarPorId(id);

        return ResponseEntity.ok(agregarLinks(disponibilidad));
    }

    @Operation(
            summary = "Actualizar disponibilidad",
            description = "Actualiza los datos de una disponibilidad existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disponibilidad actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Disponibilidad> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DisponibilidadDTO disponibilidadDTO) {

        return ResponseEntity.ok(
                disponibilidadService.actualizar(id, disponibilidadDTO)
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

    private EntityModel<Disponibilidad> agregarLinks(Disponibilidad disponibilidad) {

        return EntityModel.of(
                disponibilidad,
                Link.of(API_GATEWAY + "/disponibilidades/" + disponibilidad.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/disponibilidades").withRel("disponibilidades"),
                Link.of(API_GATEWAY + "/disponibilidades/" + disponibilidad.getId() + "/exists").withRel("existe"),
                Link.of(API_GATEWAY + "/canchas/" + disponibilidad.getIdCancha()).withRel("cancha")
        );
    }
}
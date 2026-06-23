package com.reservacanchas.cl.sede_service.controller;

import com.reservacanchas.cl.sede_service.dto.SedeDTO;
import com.reservacanchas.cl.sede_service.model.Sede;
import com.reservacanchas.cl.sede_service.service.SedeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

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
        name = "Sedes",
        description = "Operaciones para la gestión de sedes deportivas registradas en el sistema"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/sedes")
public class SedeController {

    private static final Logger logger =
            LoggerFactory.getLogger(SedeController.class);

    private static final String API_GATEWAY = "http://localhost:7090";

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
    public ResponseEntity<Sede> crear(@Valid @RequestBody SedeDTO sedeDTO) {

        logger.info("POST /sedes - Creando sede: {}", sedeDTO.getNombre());

        Sede sede = sedeService.guardar(sedeDTO);

        logger.info("Sede creada correctamente con ID: {}", sede.getId());

        return new ResponseEntity<>(sede, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar sedes",
            description = "Obtiene todas las sedes registradas en el sistema con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Sede>>> listar() {

        logger.info("GET /sedes - Listando sedes");

        List<EntityModel<Sede>> sedes = sedeService.listar()
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        logger.info("Se encontraron {} sedes", sedes.size());

        CollectionModel<EntityModel<Sede>> respuesta = CollectionModel.of(
                sedes,
                Link.of(API_GATEWAY + "/sedes").withSelfRel()
        );

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar sede por ID",
            description = "Obtiene una sede específica mediante su identificador con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sede encontrada"),
            @ApiResponse(responseCode = "404", description = "Sede no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Sede>> buscarPorId(@PathVariable Long id) {

        logger.info("GET /sedes/{} - Buscando sede", id);

        Sede sede = sedeService.buscarPorId(id);

        logger.info("Sede encontrada con ID: {}", id);

        return ResponseEntity.ok(agregarLinks(sede));
    }

    @Operation(
            summary = "Actualizar sede",
            description = "Actualiza la información de una sede existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sede actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Sede no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Sede> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SedeDTO sedeDTO) {

        logger.info("PUT /sedes/{} - Actualizando sede", id);

        Sede sede = sedeService.actualizar(id, sedeDTO);

        logger.info("Sede actualizada correctamente con ID: {}", id);

        return ResponseEntity.ok(sede);
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

        logger.info("DELETE /sedes/{} - Eliminando sede", id);

        sedeService.eliminar(id);

        logger.info("Sede eliminada correctamente con ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de sede",
            description = "Indica si una sede existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        logger.info("GET /sedes/{}/exists - Verificando existencia", id);

        boolean existe = sedeService.existePorId(id);

        logger.info("Resultado existencia sede {}: {}", id, existe);

        return existe;
    }

    private EntityModel<Sede> agregarLinks(Sede sede) {

        return EntityModel.of(
                sede,
                Link.of(API_GATEWAY + "/sedes/" + sede.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/sedes").withRel("sedes"),
                Link.of(API_GATEWAY + "/sedes/" + sede.getId() + "/exists").withRel("existe")
        );
    }
}
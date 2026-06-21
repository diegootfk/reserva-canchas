package com.reservacanchas.cl.sede_service.controller;

import com.reservacanchas.cl.sede_service.model.Sede;
import com.reservacanchas.cl.sede_service.service.SedeService;

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
        name = "Sedes",
        description = "Operaciones para la gestión de sedes deportivas registradas en el sistema"
)
@RestController
@RequestMapping("/sedes")
public class SedeController {

    private static final Logger logger = LoggerFactory.getLogger(SedeController.class);

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
    public ResponseEntity<Sede> crear(@RequestBody Sede sede) {

        logger.info("Solicitud POST recibida para crear una nueva sede");

        Sede sedeGuardada = sedeService.guardar(sede);

        logger.info("Sede creada correctamente con ID: {}", sedeGuardada.getId());

        return new ResponseEntity<>(
                sedeGuardada,
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Listar sedes",
            description = "Obtiene todas las sedes registradas en el sistema con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Sede>>> listar() {

        logger.info("Solicitud GET recibida para listar todas las sedes");

        List<Sede> sedesEncontradas = sedeService.listar();

        logger.info("Se encontraron {} sedes registradas", sedesEncontradas.size());

        List<EntityModel<Sede>> sedes = sedesEncontradas
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Sede>> respuesta = CollectionModel.of(
                sedes,
                Link.of(API_GATEWAY + "/sedes").withSelfRel()
        );

        logger.info("Respuesta HATEOAS generada correctamente para listado de sedes");

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

        logger.info("Solicitud GET recibida para buscar sede con ID: {}", id);

        Sede sede = sedeService.buscarPorId(id);

        logger.info("Sede encontrada correctamente con ID: {}", id);

        return ResponseEntity.ok(agregarLinks(sede));
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

        logger.info("Solicitud PUT recibida para actualizar sede con ID: {}", id);

        Sede sedeActualizada = sedeService.actualizar(id, sede);

        logger.info("Sede actualizada correctamente con ID: {}", id);

        return ResponseEntity.ok(sedeActualizada);
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

        logger.warn("Solicitud DELETE recibida para eliminar sede con ID: {}", id);

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

        logger.info("Solicitud GET recibida para verificar existencia de sede con ID: {}", id);

        boolean existe = sedeService.existePorId(id);

        logger.info("Resultado de existencia para sede ID {}: {}", id, existe);

        return existe;
    }

    private EntityModel<Sede> agregarLinks(Sede sede) {

        logger.debug("Agregando enlaces HATEOAS para sede con ID: {}", sede.getId());

        return EntityModel.of(
                sede,
                Link.of(API_GATEWAY + "/sedes/" + sede.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/sedes").withRel("sedes"),
                Link.of(API_GATEWAY + "/sedes/" + sede.getId() + "/exists").withRel("existe")
        );
    }
}
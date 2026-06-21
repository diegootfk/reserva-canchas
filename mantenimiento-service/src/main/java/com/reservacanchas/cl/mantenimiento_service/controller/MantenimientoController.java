package com.reservacanchas.cl.mantenimiento_service.controller;

import com.reservacanchas.cl.mantenimiento_service.model.Mantenimiento;
import com.reservacanchas.cl.mantenimiento_service.service.MantenimientoService;

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
        name = "Mantenimientos",
        description = "Operaciones para la gestión de mantenimientos de canchas"
)
@RestController
@RequestMapping("/mantenimientos")
public class MantenimientoController {

    private static final Logger logger = LoggerFactory.getLogger(MantenimientoController.class);

    private static final String API_GATEWAY = "http://localhost:7090";

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

        logger.info("Solicitud POST recibida para crear mantenimiento de cancha ID: {}",
                mantenimiento.getIdCancha());

        Mantenimiento mantenimientoGuardado = mantenimientoService.guardar(mantenimiento);

        logger.info("Mantenimiento creado correctamente con ID: {}", mantenimientoGuardado.getId());

        return new ResponseEntity<>(
                mantenimientoGuardado,
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Listar mantenimientos",
            description = "Obtiene todos los mantenimientos registrados con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Mantenimiento>>> listar() {

        logger.info("Solicitud GET recibida para listar todos los mantenimientos");

        List<Mantenimiento> mantenimientosEncontrados = mantenimientoService.listar();

        logger.info("Se encontraron {} mantenimientos registrados", mantenimientosEncontrados.size());

        List<EntityModel<Mantenimiento>> mantenimientos = mantenimientosEncontrados
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Mantenimiento>> respuesta = CollectionModel.of(
                mantenimientos,
                Link.of(API_GATEWAY + "/mantenimientos").withSelfRel()
        );

        logger.info("Respuesta HATEOAS generada correctamente para listado de mantenimientos");

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar mantenimiento por ID",
            description = "Obtiene un mantenimiento específico mediante su identificador con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mantenimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Mantenimiento>> buscarPorId(@PathVariable Long id) {

        logger.info("Solicitud GET recibida para buscar mantenimiento con ID: {}", id);

        Mantenimiento mantenimiento = mantenimientoService.buscarPorId(id);

        logger.info("Mantenimiento encontrado correctamente con ID: {}", id);

        return ResponseEntity.ok(agregarLinks(mantenimiento));
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

        logger.info("Solicitud PUT recibida para actualizar mantenimiento con ID: {}", id);

        Mantenimiento mantenimientoActualizado = mantenimientoService.actualizar(id, mantenimiento);

        logger.info("Mantenimiento actualizado correctamente con ID: {}", id);

        return ResponseEntity.ok(mantenimientoActualizado);
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

        logger.warn("Solicitud DELETE recibida para eliminar mantenimiento con ID: {}", id);

        mantenimientoService.eliminar(id);

        logger.info("Mantenimiento eliminado correctamente con ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de mantenimiento",
            description = "Indica si un mantenimiento existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        logger.info("Solicitud GET recibida para verificar existencia de mantenimiento con ID: {}", id);

        boolean existe = mantenimientoService.existePorId(id);

        logger.info("Resultado de existencia para mantenimiento ID {}: {}", id, existe);

        return existe;
    }

    private EntityModel<Mantenimiento> agregarLinks(Mantenimiento mantenimiento) {

        logger.debug("Agregando enlaces HATEOAS para mantenimiento con ID: {}", mantenimiento.getId());

        return EntityModel.of(
                mantenimiento,
                Link.of(API_GATEWAY + "/mantenimientos/" + mantenimiento.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/mantenimientos").withRel("mantenimientos"),
                Link.of(API_GATEWAY + "/mantenimientos/" + mantenimiento.getId() + "/exists").withRel("existe"),
                Link.of(API_GATEWAY + "/canchas/" + mantenimiento.getIdCancha()).withRel("cancha")
        );
    }
}
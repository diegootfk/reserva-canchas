package com.reservacanchas.cl.resena_service.controller;

import com.reservacanchas.cl.resena_service.assembler.ResenaAssembler;
import com.reservacanchas.cl.resena_service.dto.ResenaDTO;
import com.reservacanchas.cl.resena_service.model.Resena;
import com.reservacanchas.cl.resena_service.service.ResenaService;

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
        name = "Reseñas",
        description = "Operaciones para la gestión de reseñas de usuarios sobre las canchas"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/resenas")
public class ResenaController {

    private static final Logger logger =
            LoggerFactory.getLogger(ResenaController.class);

    private static final String API_GATEWAY = "http://localhost:7090";

    private final ResenaService resenaService;
    private final ResenaAssembler resenaAssembler;

    public ResenaController(ResenaService resenaService, ResenaAssembler resenaAssembler) {
        this.resenaService = resenaService;
        this.resenaAssembler = resenaAssembler;
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
    public ResponseEntity<Resena> crear(
            @Valid @RequestBody ResenaDTO resenaDTO) {

        logger.info("Solicitud para crear reseña");

        Resena resena = resenaService.guardar(resenaDTO);

        logger.info("Reseña creada correctamente con ID: {}",
                resena.getId());

        return new ResponseEntity<>(resena, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar reseñas",
            description = "Obtiene todas las reseñas registradas con enlaces HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Resena>>> listar() {

        logger.info("Solicitud para listar reseñas");

        List<EntityModel<Resena>> resenas = resenaService.listar()
                .stream()
                .map(resenaAssembler::toModel)
                .collect(Collectors.toList());

        logger.info("Reseñas listadas correctamente");

        CollectionModel<EntityModel<Resena>> respuesta = CollectionModel.of(
                resenas,
                Link.of(API_GATEWAY + "/resenas").withSelfRel()
        );

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar reseña por ID",
            description = "Obtiene una reseña específica mediante su identificador con enlaces HATEOAS"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña encontrada"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Resena>> buscarPorId(@PathVariable Long id) {

        logger.info("Solicitud para buscar reseña con ID: {}", id);

        Resena resena = resenaService.buscarPorId(id);

        logger.info("Reseña encontrada con ID: {}", id);

        return ResponseEntity.ok(resenaAssembler.toModel(resena));
    }

    @Operation(
            summary = "Actualizar reseña",
            description = "Actualiza los datos de una reseña existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Resena> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ResenaDTO resenaDTO) {

        logger.info("Solicitud para actualizar reseña con ID: {}", id);

        Resena resena = resenaService.actualizar(id, resenaDTO);

        logger.info("Reseña actualizada correctamente con ID: {}", id);

        return ResponseEntity.ok(resena);
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

        logger.info("Solicitud para eliminar reseña con ID: {}", id);

        resenaService.eliminar(id);

        logger.info("Reseña eliminada correctamente con ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de reseña",
            description = "Indica si una reseña existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        logger.info("Verificando existencia de reseña con ID: {}", id);

        return resenaService.existePorId(id);
    }
}
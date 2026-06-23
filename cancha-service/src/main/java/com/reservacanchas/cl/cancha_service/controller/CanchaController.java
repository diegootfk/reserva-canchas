package com.reservacanchas.cl.cancha_service.controller;

import com.reservacanchas.cl.cancha_service.assembler.CanchaAssembler;
import com.reservacanchas.cl.cancha_service.dto.CanchaDTO;
import com.reservacanchas.cl.cancha_service.model.Cancha;
import com.reservacanchas.cl.cancha_service.service.CanchaService;

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
        name = "Canchas",
        description = "Operaciones para la gestión de canchas deportivas"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/canchas")
public class CanchaController {

    private static final Logger logger =
            LoggerFactory.getLogger(CanchaController.class);

    private static final String API_GATEWAY = "http://localhost:7090";

    private final CanchaService canchaService;
    private final CanchaAssembler canchaAssembler;

    public CanchaController(CanchaService canchaService, CanchaAssembler canchaAssembler) {
        this.canchaService = canchaService;
        this.canchaAssembler = canchaAssembler;
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

        logger.info("Solicitud para crear cancha");

        Cancha cancha = canchaService.guardar(canchaDTO);

        logger.info("Cancha creada correctamente con ID {}",
                cancha.getId());

        return new ResponseEntity<>(cancha, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar canchas",
            description = "Obtiene todas las canchas registradas con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Cancha>>> listar() {

        logger.info("Solicitud para listar canchas");

        List<EntityModel<Cancha>> canchas = canchaService.listar()
                .stream()
                .map(canchaAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Cancha>> respuesta = CollectionModel.of(
                canchas,
                Link.of(API_GATEWAY + "/canchas").withSelfRel()
        );

        logger.info("Se encontraron {} canchas", canchas.size());

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar cancha por ID",
            description = "Obtiene una cancha específica mediante su identificador con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cancha encontrada"),
            @ApiResponse(responseCode = "404", description = "Cancha no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cancha>> buscarPorId(@PathVariable Long id) {

        logger.info("Solicitud para buscar cancha con ID {}", id);

        Cancha cancha = canchaService.buscarPorId(id);

        logger.info("Cancha encontrada con ID {}", id);

        return ResponseEntity.ok(canchaAssembler.toModel(cancha));
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

        logger.info("Solicitud para actualizar cancha con ID {}", id);

        Cancha cancha = canchaService.actualizar(id, canchaDTO);

        logger.info("Cancha actualizada correctamente con ID {}", id);

        return ResponseEntity.ok(cancha);
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

        logger.info("Solicitud para eliminar cancha con ID {}", id);

        canchaService.eliminar(id);

        logger.info("Cancha eliminada correctamente con ID {}", id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de cancha",
            description = "Indica si una cancha existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        logger.info("Verificando existencia de cancha con ID {}", id);

        return canchaService.existePorId(id);
    }
}
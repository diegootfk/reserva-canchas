package com.reservacanchas.cl.horario_service.controller;

import com.reservacanchas.cl.horario_service.assembler.HorarioAssembler;
import com.reservacanchas.cl.horario_service.dto.HorarioDTO;
import com.reservacanchas.cl.horario_service.model.Horario;
import com.reservacanchas.cl.horario_service.service.HorarioService;

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
        name = "Horarios",
        description = "Operaciones para la gestión de horarios de canchas"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/horarios")
public class HorarioController {

    private static final Logger logger =
            LoggerFactory.getLogger(HorarioController.class);

    private static final String API_GATEWAY = "http://localhost:7090";

    private final HorarioService horarioService;
    private final HorarioAssembler horarioAssembler;

    public HorarioController(HorarioService horarioService, HorarioAssembler horarioAssembler) {
        this.horarioService = horarioService;
        this.horarioAssembler = horarioAssembler;
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
    public ResponseEntity<Horario> crear(
            @Valid @RequestBody HorarioDTO horarioDTO) {

        logger.info(
                "Solicitud para crear horario de cancha {}",
                horarioDTO.getIdCancha()
        );

        Horario horario = horarioService.guardar(horarioDTO);

        logger.info(
                "Horario creado correctamente con ID {}",
                horario.getId()
        );

        return new ResponseEntity<>(
                horario,
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Listar horarios",
            description = "Obtiene todos los horarios registrados con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Horario>>> listar() {

        logger.info("Solicitud para listar todos los horarios");

        List<EntityModel<Horario>> horarios = horarioService.listar()
                .stream()
                .map(horarioAssembler::toModel)
                .collect(Collectors.toList());

        logger.info(
                "Se encontraron {} horarios",
                horarios.size()
        );

        CollectionModel<EntityModel<Horario>> respuesta = CollectionModel.of(
                horarios,
                Link.of(API_GATEWAY + "/horarios").withSelfRel()
        );

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar horario por ID",
            description = "Obtiene un horario específico mediante su identificador con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horario encontrado"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Horario>> buscarPorId(
            @PathVariable Long id) {

        logger.info(
                "Solicitud para buscar horario con ID {}",
                id
        );

        Horario horario = horarioService.buscarPorId(id);

        logger.info(
                "Horario encontrado con ID {}",
                id
        );

        return ResponseEntity.ok(
                horarioAssembler.toModel(horario)
        );
    }

    @Operation(
            summary = "Actualizar horario",
            description = "Actualiza los datos de un horario existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horario actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Horario> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HorarioDTO horarioDTO) {

        logger.info(
                "Solicitud para actualizar horario con ID {}",
                id
        );

        Horario horarioActualizado =
                horarioService.actualizar(id, horarioDTO);

        logger.info(
                "Horario actualizado correctamente con ID {}",
                id
        );

        return ResponseEntity.ok(
                horarioActualizado
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

        logger.info(
                "Solicitud para eliminar horario con ID {}",
                id
        );

        horarioService.eliminar(id);

        logger.info(
                "Horario eliminado correctamente con ID {}",
                id
        );

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de horario",
            description = "Indica si un horario existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        logger.info(
                "Verificando existencia de horario con ID {}",
                id
        );

        return horarioService.existePorId(id);
    }
}
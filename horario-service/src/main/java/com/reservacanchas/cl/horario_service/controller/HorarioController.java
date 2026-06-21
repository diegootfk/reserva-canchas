package com.reservacanchas.cl.horario_service.controller;

import com.reservacanchas.cl.horario_service.model.Horario;
import com.reservacanchas.cl.horario_service.service.HorarioService;

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
        name = "Horarios",
        description = "Operaciones para la gestión de horarios de canchas"
)
@RestController
@RequestMapping("/horarios")
public class HorarioController {

    private static final Logger logger = LoggerFactory.getLogger(HorarioController.class);

    private static final String API_GATEWAY = "http://localhost:7090";

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

        logger.info("Solicitud POST recibida para crear horario de cancha ID: {}",
                horario.getIdCancha());

        Horario horarioGuardado = horarioService.guardar(horario);

        logger.info("Horario creado correctamente con ID: {}", horarioGuardado.getId());

        return new ResponseEntity<>(
                horarioGuardado,
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

        logger.info("Solicitud GET recibida para listar todos los horarios");

        List<Horario> horariosEncontrados = horarioService.listar();

        logger.info("Se encontraron {} horarios registrados", horariosEncontrados.size());

        List<EntityModel<Horario>> horarios = horariosEncontrados
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Horario>> respuesta = CollectionModel.of(
                horarios,
                Link.of(API_GATEWAY + "/horarios").withSelfRel()
        );

        logger.info("Respuesta HATEOAS generada correctamente para listado de horarios");

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
    public ResponseEntity<EntityModel<Horario>> buscarPorId(@PathVariable Long id) {

        logger.info("Solicitud GET recibida para buscar horario con ID: {}", id);

        Horario horario = horarioService.buscarPorId(id);

        logger.info("Horario encontrado correctamente con ID: {}", id);

        return ResponseEntity.ok(agregarLinks(horario));
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

        logger.info("Solicitud PUT recibida para actualizar horario con ID: {}", id);

        Horario horarioActualizado = horarioService.actualizar(id, horario);

        logger.info("Horario actualizado correctamente con ID: {}", id);

        return ResponseEntity.ok(horarioActualizado);
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

        logger.warn("Solicitud DELETE recibida para eliminar horario con ID: {}", id);

        horarioService.eliminar(id);

        logger.info("Horario eliminado correctamente con ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de horario",
            description = "Indica si un horario existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        logger.info("Solicitud GET recibida para verificar existencia de horario con ID: {}", id);

        boolean existe = horarioService.existePorId(id);

        logger.info("Resultado de existencia para horario ID {}: {}", id, existe);

        return existe;
    }

    private EntityModel<Horario> agregarLinks(Horario horario) {

        logger.debug("Agregando enlaces HATEOAS para horario con ID: {}", horario.getId());

        return EntityModel.of(
                horario,
                Link.of(API_GATEWAY + "/horarios/" + horario.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/horarios").withRel("horarios"),
                Link.of(API_GATEWAY + "/horarios/" + horario.getId() + "/exists").withRel("existe"),
                Link.of(API_GATEWAY + "/canchas/" + horario.getIdCancha()).withRel("cancha")
        );
    }
}
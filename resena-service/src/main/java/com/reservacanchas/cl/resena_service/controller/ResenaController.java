package com.reservacanchas.cl.resena_service.controller;

import com.reservacanchas.cl.resena_service.model.Resena;
import com.reservacanchas.cl.resena_service.service.ResenaService;

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
        name = "Reseñas",
        description = "Operaciones para la gestión de reseñas de usuarios sobre las canchas"
)
@RestController
@RequestMapping("/resenas")
public class ResenaController {

    private static final Logger logger = LoggerFactory.getLogger(ResenaController.class);

    private static final String API_GATEWAY = "http://localhost:7090";

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

        logger.info("Solicitud POST recibida para crear una nueva reseña");

        Resena resenaGuardada = resenaService.guardar(resena);

        logger.info("Reseña creada correctamente con ID: {}", resenaGuardada.getId());

        return new ResponseEntity<>(
                resenaGuardada,
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Listar reseñas",
            description = "Obtiene todas las reseñas registradas con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Resena>>> listar() {

        logger.info("Solicitud GET recibida para listar todas las reseñas");

        List<Resena> resenasEncontradas = resenaService.listar();

        logger.info("Se encontraron {} reseñas registradas", resenasEncontradas.size());

        List<EntityModel<Resena>> resenas = resenasEncontradas
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Resena>> respuesta = CollectionModel.of(
                resenas,
                Link.of(API_GATEWAY + "/resenas").withSelfRel()
        );

        logger.info("Respuesta HATEOAS generada correctamente para listado de reseñas");

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar reseña por ID",
            description = "Obtiene una reseña específica mediante su identificador con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña encontrada"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Resena>> buscarPorId(@PathVariable Long id) {

        logger.info("Solicitud GET recibida para buscar reseña con ID: {}", id);

        Resena resena = resenaService.buscarPorId(id);

        logger.info("Reseña encontrada correctamente con ID: {}", id);

        return ResponseEntity.ok(agregarLinks(resena));
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

        logger.info("Solicitud PUT recibida para actualizar reseña con ID: {}", id);

        Resena resenaActualizada = resenaService.actualizar(id, resena);

        logger.info("Reseña actualizada correctamente con ID: {}", id);

        return ResponseEntity.ok(resenaActualizada);
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

        logger.warn("Solicitud DELETE recibida para eliminar reseña con ID: {}", id);

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

        logger.info("Solicitud GET recibida para verificar existencia de reseña con ID: {}", id);

        boolean existe = resenaService.existePorId(id);

        logger.info("Resultado de existencia para reseña ID {}: {}", id, existe);

        return existe;
    }

    private EntityModel<Resena> agregarLinks(Resena resena) {

        logger.debug("Agregando enlaces HATEOAS para reseña con ID: {}", resena.getId());

        return EntityModel.of(
                resena,
                Link.of(API_GATEWAY + "/resenas/" + resena.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/resenas").withRel("resenas"),
                Link.of(API_GATEWAY + "/resenas/" + resena.getId() + "/exists").withRel("existe"),
                Link.of(API_GATEWAY + "/usuarios/" + resena.getIdUsuario()).withRel("usuario"),
                Link.of(API_GATEWAY + "/canchas/" + resena.getIdCancha()).withRel("cancha"),
                Link.of(API_GATEWAY + "/reservas/" + resena.getIdReserva()).withRel("reserva")
        );
    }
}
package com.reservacanchas.cl.reserva_service.controller;

import com.reservacanchas.cl.reserva_service.dto.ReservaDTO;
import com.reservacanchas.cl.reserva_service.model.Reserva;
import com.reservacanchas.cl.reserva_service.service.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
        name = "Reservas",
        description = "Operaciones para la gestión de reservas de canchas y validación entre microservicios"
)
@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);

    private static final String API_GATEWAY = "http://localhost:7090";

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @Operation(
            summary = "Crear reserva",
            description = "Registra una reserva validando previamente la existencia del usuario y de la cancha mediante comunicación entre microservicios"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario o cancha no encontrados")
    })
    @PostMapping
    public ResponseEntity<Reserva> crear(@Valid @RequestBody ReservaDTO reservaDTO) {

        logger.info("Solicitud POST recibida para crear una nueva reserva");

        Reserva reserva = reservaService.guardar(reservaDTO);

        logger.info("Reserva creada correctamente con ID: {}", reserva.getId());

        return new ResponseEntity<>(reserva, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar reservas",
            description = "Obtiene todas las reservas registradas en el sistema con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Reserva>>> listar() {

        logger.info("Solicitud GET recibida para listar todas las reservas");

        List<Reserva> reservasEncontradas = reservaService.listar();

        logger.info("Se encontraron {} reservas registradas", reservasEncontradas.size());

        List<EntityModel<Reserva>> reservas = reservasEncontradas
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Reserva>> respuesta = CollectionModel.of(
                reservas,
                Link.of(API_GATEWAY + "/reservas").withSelfRel()
        );

        logger.info("Respuesta HATEOAS generada correctamente para listado de reservas");

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar reserva por ID",
            description = "Obtiene una reserva específica mediante su identificador con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Reserva>> buscarPorId(@PathVariable Long id) {

        logger.info("Solicitud GET recibida para buscar reserva con ID: {}", id);

        Reserva reserva = reservaService.buscarPorId(id);

        logger.info("Reserva encontrada correctamente con ID: {}", id);

        return ResponseEntity.ok(agregarLinks(reserva));
    }

    @Operation(
            summary = "Actualizar reserva",
            description = "Actualiza la información de una reserva existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Reserva> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReservaDTO reservaDTO
    ) {

        logger.info("Solicitud PUT recibida para actualizar reserva con ID: {}", id);

        Reserva reservaActualizada = reservaService.actualizar(id, reservaDTO);

        logger.info("Reserva actualizada correctamente con ID: {}", id);

        return ResponseEntity.ok(reservaActualizada);
    }

    @Operation(
            summary = "Eliminar reserva",
            description = "Elimina una reserva según su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reserva eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        logger.warn("Solicitud DELETE recibida para eliminar reserva con ID: {}", id);

        reservaService.eliminar(id);

        logger.info("Reserva eliminada correctamente con ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de reserva",
            description = "Indica si una reserva existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        logger.info("Solicitud GET recibida para verificar existencia de reserva con ID: {}", id);

        boolean existe = reservaService.existePorId(id);

        logger.info("Resultado de existencia para reserva ID {}: {}", id, existe);

        return existe;
    }

    @Operation(
            summary = "Buscar reservas por estado",
            description = "Obtiene todas las reservas según su estado con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Reservas encontradas correctamente")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<CollectionModel<EntityModel<Reserva>>> buscarPorEstado(@PathVariable String estado) {

        logger.info("Solicitud GET recibida para buscar reservas con estado: {}", estado);

        List<Reserva> reservasEncontradas = reservaService.buscarPorEstado(estado);

        logger.info("Se encontraron {} reservas con estado: {}", reservasEncontradas.size(), estado);

        List<EntityModel<Reserva>> reservas = reservasEncontradas
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Reserva>> respuesta = CollectionModel.of(
                reservas,
                Link.of(API_GATEWAY + "/reservas/estado/" + estado).withSelfRel(),
                Link.of(API_GATEWAY + "/reservas").withRel("reservas")
        );

        logger.info("Respuesta HATEOAS generada correctamente para reservas con estado: {}", estado);

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar reservas por usuario",
            description = "Obtiene todas las reservas asociadas a un usuario específico con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Reservas encontradas correctamente")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<CollectionModel<EntityModel<Reserva>>> buscarPorUsuario(@PathVariable Long idUsuario) {

        logger.info("Solicitud GET recibida para buscar reservas del usuario con ID: {}", idUsuario);

        List<Reserva> reservasEncontradas = reservaService.buscarPorUsuario(idUsuario);

        logger.info("Se encontraron {} reservas para el usuario con ID: {}", reservasEncontradas.size(), idUsuario);

        List<EntityModel<Reserva>> reservas = reservasEncontradas
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Reserva>> respuesta = CollectionModel.of(
                reservas,
                Link.of(API_GATEWAY + "/reservas/usuario/" + idUsuario).withSelfRel(),
                Link.of(API_GATEWAY + "/reservas").withRel("reservas"),
                Link.of(API_GATEWAY + "/usuarios/" + idUsuario).withRel("usuario")
        );

        logger.info("Respuesta HATEOAS generada correctamente para reservas del usuario con ID: {}", idUsuario);

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar reservas por cancha",
            description = "Obtiene todas las reservas asociadas a una cancha específica con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Reservas encontradas correctamente")
    @GetMapping("/cancha/{idCancha}")
    public ResponseEntity<CollectionModel<EntityModel<Reserva>>> buscarPorCancha(@PathVariable Long idCancha) {

        logger.info("Solicitud GET recibida para buscar reservas de la cancha con ID: {}", idCancha);

        List<Reserva> reservasEncontradas = reservaService.buscarPorCancha(idCancha);

        logger.info("Se encontraron {} reservas para la cancha con ID: {}", reservasEncontradas.size(), idCancha);

        List<EntityModel<Reserva>> reservas = reservasEncontradas
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Reserva>> respuesta = CollectionModel.of(
                reservas,
                Link.of(API_GATEWAY + "/reservas/cancha/" + idCancha).withSelfRel(),
                Link.of(API_GATEWAY + "/reservas").withRel("reservas"),
                Link.of(API_GATEWAY + "/canchas/" + idCancha).withRel("cancha")
        );

        logger.info("Respuesta HATEOAS generada correctamente para reservas de la cancha con ID: {}", idCancha);

        return ResponseEntity.ok(respuesta);
    }

    private EntityModel<Reserva> agregarLinks(Reserva reserva) {

        logger.debug("Agregando enlaces HATEOAS para reserva con ID: {}", reserva.getId());

        return EntityModel.of(
                reserva,
                Link.of(API_GATEWAY + "/reservas/" + reserva.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/reservas").withRel("reservas"),
                Link.of(API_GATEWAY + "/reservas/" + reserva.getId() + "/exists").withRel("existe"),
                Link.of(API_GATEWAY + "/reservas/estado/" + reserva.getEstado()).withRel("reservas-por-estado"),
                Link.of(API_GATEWAY + "/reservas/usuario/" + reserva.getIdUsuario()).withRel("reservas-por-usuario"),
                Link.of(API_GATEWAY + "/reservas/cancha/" + reserva.getIdCancha()).withRel("reservas-por-cancha"),
                Link.of(API_GATEWAY + "/usuarios/" + reserva.getIdUsuario()).withRel("usuario"),
                Link.of(API_GATEWAY + "/canchas/" + reserva.getIdCancha()).withRel("cancha"),
                Link.of(API_GATEWAY + "/pagos/reserva/" + reserva.getId()).withRel("pagos")
        );
    }
}
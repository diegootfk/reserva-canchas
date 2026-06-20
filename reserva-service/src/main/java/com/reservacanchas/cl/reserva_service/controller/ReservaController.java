package com.reservacanchas.cl.reserva_service.controller;

import com.reservacanchas.cl.reserva_service.dto.ReservaDTO;
import com.reservacanchas.cl.reserva_service.model.Reserva;
import com.reservacanchas.cl.reserva_service.service.ReservaService;

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
        name = "Reservas",
        description = "Operaciones para la gestión de reservas de canchas y validación entre microservicios"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/reservas")
public class ReservaController {

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

        Reserva reserva = reservaService.guardar(reservaDTO);

        return new ResponseEntity<>(reserva, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar reservas",
            description = "Obtiene todas las reservas registradas en el sistema con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Reserva>>> listar() {

        List<EntityModel<Reserva>> reservas = reservaService.listar()
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Reserva>> respuesta = CollectionModel.of(
                reservas,
                Link.of(API_GATEWAY + "/reservas").withSelfRel()
        );

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

        Reserva reserva = reservaService.buscarPorId(id);

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

        return ResponseEntity.ok(
                reservaService.actualizar(id, reservaDTO)
        );
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

        reservaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de reserva",
            description = "Indica si una reserva existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        return reservaService.existePorId(id);
    }

    @Operation(
            summary = "Buscar reservas por estado",
            description = "Obtiene todas las reservas según su estado con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Reservas encontradas correctamente")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<CollectionModel<EntityModel<Reserva>>> buscarPorEstado(@PathVariable String estado) {

        List<EntityModel<Reserva>> reservas = reservaService.buscarPorEstado(estado)
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Reserva>> respuesta = CollectionModel.of(
                reservas,
                Link.of(API_GATEWAY + "/reservas/estado/" + estado).withSelfRel(),
                Link.of(API_GATEWAY + "/reservas").withRel("reservas")
        );

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar reservas por usuario",
            description = "Obtiene todas las reservas asociadas a un usuario específico con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Reservas encontradas correctamente")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<CollectionModel<EntityModel<Reserva>>> buscarPorUsuario(@PathVariable Long idUsuario) {

        List<EntityModel<Reserva>> reservas = reservaService.buscarPorUsuario(idUsuario)
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Reserva>> respuesta = CollectionModel.of(
                reservas,
                Link.of(API_GATEWAY + "/reservas/usuario/" + idUsuario).withSelfRel(),
                Link.of(API_GATEWAY + "/reservas").withRel("reservas"),
                Link.of(API_GATEWAY + "/usuarios/" + idUsuario).withRel("usuario")
        );

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar reservas por cancha",
            description = "Obtiene todas las reservas asociadas a una cancha específica con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Reservas encontradas correctamente")
    @GetMapping("/cancha/{idCancha}")
    public ResponseEntity<CollectionModel<EntityModel<Reserva>>> buscarPorCancha(@PathVariable Long idCancha) {

        List<EntityModel<Reserva>> reservas = reservaService.buscarPorCancha(idCancha)
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Reserva>> respuesta = CollectionModel.of(
                reservas,
                Link.of(API_GATEWAY + "/reservas/cancha/" + idCancha).withSelfRel(),
                Link.of(API_GATEWAY + "/reservas").withRel("reservas"),
                Link.of(API_GATEWAY + "/canchas/" + idCancha).withRel("cancha")
        );

        return ResponseEntity.ok(respuesta);
    }

    private EntityModel<Reserva> agregarLinks(Reserva reserva) {

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
package com.reservacanchas.cl.pago_service.controller;

import com.reservacanchas.cl.pago_service.dto.PagoDTO;
import com.reservacanchas.cl.pago_service.model.Pago;
import com.reservacanchas.cl.pago_service.service.PagoService;

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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Tag(
        name = "Pagos",
        description = "Operaciones para la gestión de pagos asociados a reservas"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @Operation(
            summary = "Registrar pago",
            description = "Crea un nuevo pago validando previamente la existencia de la reserva asociada"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pago registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @PostMapping
    public ResponseEntity<Pago> crear(@Valid @RequestBody PagoDTO pagoDTO) {

        Pago pago = pagoService.guardar(pagoDTO);

        return new ResponseEntity<>(pago, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar pagos",
            description = "Obtiene todos los pagos registrados en el sistema con enlaces HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> listar() {

        List<EntityModel<Pago>> pagos = pagoService.listar()
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pago>> respuesta = CollectionModel.of(
                pagos,
                linkTo(PagoController.class).withSelfRel()
        );

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar pago por ID",
            description = "Obtiene un pago específico mediante su identificador con enlaces HATEOAS"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pago>> buscarPorId(@PathVariable Long id) {

        Pago pago = pagoService.buscarPorId(id);

        return ResponseEntity.ok(agregarLinks(pago));
    }

    @Operation(
            summary = "Actualizar pago",
            description = "Actualiza la información de un pago existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PagoDTO pagoDTO) {

        return ResponseEntity.ok(
                pagoService.actualizar(id, pagoDTO)
        );
    }

    @Operation(
            summary = "Eliminar pago",
            description = "Elimina un pago según su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pago eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        pagoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de pago",
            description = "Indica si un pago existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        return pagoService.existePorId(id);
    }

    @Operation(
            summary = "Buscar pagos por método",
            description = "Obtiene todos los pagos realizados con un método de pago específico con enlaces HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Pagos encontrados correctamente")
    @GetMapping("/metodo/{metodoPago}")
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> buscarPorMetodoPago(
            @PathVariable String metodoPago) {

        List<EntityModel<Pago>> pagos = pagoService.buscarPorMetodoPago(metodoPago)
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pago>> respuesta = CollectionModel.of(
                pagos,
                linkTo(PagoController.class).slash("metodo").slash(metodoPago).withSelfRel(),
                linkTo(PagoController.class).withRel("pagos")
        );

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar pagos por estado",
            description = "Obtiene todos los pagos según su estado con enlaces HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Pagos encontrados correctamente")
    @GetMapping("/estado/{estadoPago}")
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> buscarPorEstadoPago(
            @PathVariable String estadoPago) {

        List<EntityModel<Pago>> pagos = pagoService.buscarPorEstadoPago(estadoPago)
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pago>> respuesta = CollectionModel.of(
                pagos,
                linkTo(PagoController.class).slash("estado").slash(estadoPago).withSelfRel(),
                linkTo(PagoController.class).withRel("pagos")
        );

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar pagos por reserva",
            description = "Obtiene todos los pagos asociados a una reserva específica con enlaces HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Pagos encontrados correctamente")
    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> buscarPorReserva(
            @PathVariable Long idReserva) {

        List<EntityModel<Pago>> pagos = pagoService.buscarPorReserva(idReserva)
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pago>> respuesta = CollectionModel.of(
                pagos,
                linkTo(PagoController.class).slash("reserva").slash(idReserva).withSelfRel(),
                linkTo(PagoController.class).withRel("pagos"),
                Link.of("http://localhost:7093/reservas/" + idReserva).withRel("reserva")
        );

        return ResponseEntity.ok(respuesta);
    }

    private EntityModel<Pago> agregarLinks(Pago pago) {

        return EntityModel.of(
                pago,
                linkTo(PagoController.class).slash(pago.getId()).withSelfRel(),
                linkTo(PagoController.class).withRel("pagos"),
                linkTo(PagoController.class).slash(pago.getId()).slash("exists").withRel("existe"),
                linkTo(PagoController.class).slash("metodo").slash(pago.getMetodoPago()).withRel("pagos-por-metodo"),
                linkTo(PagoController.class).slash("estado").slash(pago.getEstadoPago()).withRel("pagos-por-estado"),
                linkTo(PagoController.class).slash("reserva").slash(pago.getIdReserva()).withRel("pagos-por-reserva"),
                Link.of("http://localhost:7093/reservas/" + pago.getIdReserva()).withRel("reserva")
        );
    }
}
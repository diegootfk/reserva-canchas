package com.reservacanchas.cl.pago_service.controller;

import com.reservacanchas.cl.pago_service.assembler.PagoAssembler;
import com.reservacanchas.cl.pago_service.dto.PagoDTO;
import com.reservacanchas.cl.pago_service.model.Pago;
import com.reservacanchas.cl.pago_service.service.PagoService;

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
        name = "Pagos",
        description = "Operaciones para la gestión de pagos asociados a reservas"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/pagos")
public class PagoController {

    private static final Logger logger =
            LoggerFactory.getLogger(PagoController.class);

    private static final String API_GATEWAY = "http://localhost:7090";

    private final PagoService pagoService;
    private final PagoAssembler pagoAssembler;

    public PagoController(PagoService pagoService, PagoAssembler pagoAssembler) {
        this.pagoService = pagoService;
        this.pagoAssembler = pagoAssembler;
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

        logger.info("Solicitud para crear pago de reserva {}",
                pagoDTO.getIdReserva());

        Pago pago = pagoService.guardar(pagoDTO);

        logger.info("Pago creado correctamente con ID {}",
                pago.getId());

        return new ResponseEntity<>(pago, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar pagos",
            description = "Obtiene todos los pagos registrados en el sistema con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> listar() {

        logger.info("Solicitud para listar todos los pagos");

        List<EntityModel<Pago>> pagos = pagoService.listar()
                .stream()
                .map(pagoAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pago>> respuesta = CollectionModel.of(
                pagos,
                Link.of(API_GATEWAY + "/pagos").withSelfRel()
        );

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar pago por ID",
            description = "Obtiene un pago específico mediante su identificador con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pago>> buscarPorId(@PathVariable Long id) {

        logger.info("Solicitud para buscar pago con ID {}", id);

        Pago pago = pagoService.buscarPorId(id);

        return ResponseEntity.ok(pagoAssembler.toModel(pago));
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

        logger.info("Solicitud para actualizar pago con ID {}", id);

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

        logger.info("Solicitud para eliminar pago con ID {}", id);

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

        logger.info("Verificando existencia del pago con ID {}", id);

        return pagoService.existePorId(id);
    }

    @Operation(
            summary = "Buscar pagos por método",
            description = "Obtiene todos los pagos realizados con un método de pago específico con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Pagos encontrados correctamente")
    @GetMapping("/metodo/{metodoPago}")
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> buscarPorMetodoPago(
            @PathVariable String metodoPago) {

        logger.info("Buscando pagos por método {}", metodoPago);

        List<EntityModel<Pago>> pagos = pagoService.buscarPorMetodoPago(metodoPago)
                .stream()
                .map(pagoAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pago>> respuesta = CollectionModel.of(
                pagos,
                Link.of(API_GATEWAY + "/pagos/metodo/" + metodoPago).withSelfRel(),
                Link.of(API_GATEWAY + "/pagos").withRel("pagos")
        );

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar pagos por estado",
            description = "Obtiene todos los pagos según su estado con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Pagos encontrados correctamente")
    @GetMapping("/estado/{estadoPago}")
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> buscarPorEstadoPago(
            @PathVariable String estadoPago) {

        logger.info("Buscando pagos por estado {}", estadoPago);

        List<EntityModel<Pago>> pagos = pagoService.buscarPorEstadoPago(estadoPago)
                .stream()
                .map(pagoAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pago>> respuesta = CollectionModel.of(
                pagos,
                Link.of(API_GATEWAY + "/pagos/estado/" + estadoPago).withSelfRel(),
                Link.of(API_GATEWAY + "/pagos").withRel("pagos")
        );

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar pagos por reserva",
            description = "Obtiene todos los pagos asociados a una reserva específica con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Pagos encontrados correctamente")
    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> buscarPorReserva(
            @PathVariable Long idReserva) {

        logger.info("Buscando pagos de la reserva {}", idReserva);

        List<EntityModel<Pago>> pagos = pagoService.buscarPorReserva(idReserva)
                .stream()
                .map(pagoAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pago>> respuesta = CollectionModel.of(
                pagos,
                Link.of(API_GATEWAY + "/pagos/reserva/" + idReserva).withSelfRel(),
                Link.of(API_GATEWAY + "/pagos").withRel("pagos"),
                Link.of(API_GATEWAY + "/reservas/" + idReserva).withRel("reserva")
        );

        return ResponseEntity.ok(respuesta);
    }
}
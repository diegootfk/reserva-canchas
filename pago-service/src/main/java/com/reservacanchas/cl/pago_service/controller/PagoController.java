package com.reservacanchas.cl.pago_service.controller;

import com.reservacanchas.cl.pago_service.dto.PagoDTO;
import com.reservacanchas.cl.pago_service.model.Pago;
import com.reservacanchas.cl.pago_service.service.PagoService;

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
        name = "Pagos",
        description = "Operaciones para la gestión de pagos asociados a reservas"
)
@RestController
@RequestMapping("/pagos")
public class PagoController {

    private static final Logger logger = LoggerFactory.getLogger(PagoController.class);

    private static final String API_GATEWAY = "http://localhost:7090";

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

        logger.info("Solicitud POST recibida para registrar un nuevo pago asociado a reserva ID: {}",
                pagoDTO.getIdReserva());

        Pago pago = pagoService.guardar(pagoDTO);

        logger.info("Pago registrado correctamente con ID: {}", pago.getId());

        return new ResponseEntity<>(pago, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar pagos",
            description = "Obtiene todos los pagos registrados en el sistema con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> listar() {

        logger.info("Solicitud GET recibida para listar todos los pagos");

        List<Pago> pagosEncontrados = pagoService.listar();

        logger.info("Se encontraron {} pagos registrados", pagosEncontrados.size());

        List<EntityModel<Pago>> pagos = pagosEncontrados
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pago>> respuesta = CollectionModel.of(
                pagos,
                Link.of(API_GATEWAY + "/pagos").withSelfRel()
        );

        logger.info("Respuesta HATEOAS generada correctamente para listado de pagos");

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

        logger.info("Solicitud GET recibida para buscar pago con ID: {}", id);

        Pago pago = pagoService.buscarPorId(id);

        logger.info("Pago encontrado correctamente con ID: {}", id);

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

        logger.info("Solicitud PUT recibida para actualizar pago con ID: {}", id);

        Pago pagoActualizado = pagoService.actualizar(id, pagoDTO);

        logger.info("Pago actualizado correctamente con ID: {}", id);

        return ResponseEntity.ok(pagoActualizado);
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

        logger.warn("Solicitud DELETE recibida para eliminar pago con ID: {}", id);

        pagoService.eliminar(id);

        logger.info("Pago eliminado correctamente con ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de pago",
            description = "Indica si un pago existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        logger.info("Solicitud GET recibida para verificar existencia de pago con ID: {}", id);

        boolean existe = pagoService.existePorId(id);

        logger.info("Resultado de existencia para pago ID {}: {}", id, existe);

        return existe;
    }

    @Operation(
            summary = "Buscar pagos por método",
            description = "Obtiene todos los pagos realizados con un método de pago específico con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Pagos encontrados correctamente")
    @GetMapping("/metodo/{metodoPago}")
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> buscarPorMetodoPago(
            @PathVariable String metodoPago) {

        logger.info("Solicitud GET recibida para buscar pagos con método de pago: {}", metodoPago);

        List<Pago> pagosEncontrados = pagoService.buscarPorMetodoPago(metodoPago);

        logger.info("Se encontraron {} pagos con método de pago: {}",
                pagosEncontrados.size(),
                metodoPago);

        List<EntityModel<Pago>> pagos = pagosEncontrados
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pago>> respuesta = CollectionModel.of(
                pagos,
                Link.of(API_GATEWAY + "/pagos/metodo/" + metodoPago).withSelfRel(),
                Link.of(API_GATEWAY + "/pagos").withRel("pagos")
        );

        logger.info("Respuesta HATEOAS generada correctamente para pagos con método: {}", metodoPago);

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

        logger.info("Solicitud GET recibida para buscar pagos con estado: {}", estadoPago);

        List<Pago> pagosEncontrados = pagoService.buscarPorEstadoPago(estadoPago);

        logger.info("Se encontraron {} pagos con estado: {}",
                pagosEncontrados.size(),
                estadoPago);

        List<EntityModel<Pago>> pagos = pagosEncontrados
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pago>> respuesta = CollectionModel.of(
                pagos,
                Link.of(API_GATEWAY + "/pagos/estado/" + estadoPago).withSelfRel(),
                Link.of(API_GATEWAY + "/pagos").withRel("pagos")
        );

        logger.info("Respuesta HATEOAS generada correctamente para pagos con estado: {}", estadoPago);

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

        logger.info("Solicitud GET recibida para buscar pagos asociados a reserva ID: {}", idReserva);

        List<Pago> pagosEncontrados = pagoService.buscarPorReserva(idReserva);

        logger.info("Se encontraron {} pagos asociados a reserva ID: {}",
                pagosEncontrados.size(),
                idReserva);

        List<EntityModel<Pago>> pagos = pagosEncontrados
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pago>> respuesta = CollectionModel.of(
                pagos,
                Link.of(API_GATEWAY + "/pagos/reserva/" + idReserva).withSelfRel(),
                Link.of(API_GATEWAY + "/pagos").withRel("pagos"),
                Link.of(API_GATEWAY + "/reservas/" + idReserva).withRel("reserva")
        );

        logger.info("Respuesta HATEOAS generada correctamente para pagos de reserva ID: {}", idReserva);

        return ResponseEntity.ok(respuesta);
    }

    private EntityModel<Pago> agregarLinks(Pago pago) {

        logger.debug("Agregando enlaces HATEOAS para pago con ID: {}", pago.getId());

        return EntityModel.of(
                pago,
                Link.of(API_GATEWAY + "/pagos/" + pago.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/pagos").withRel("pagos"),
                Link.of(API_GATEWAY + "/pagos/" + pago.getId() + "/exists").withRel("existe"),
                Link.of(API_GATEWAY + "/pagos/metodo/" + pago.getMetodoPago()).withRel("pagos-por-metodo"),
                Link.of(API_GATEWAY + "/pagos/estado/" + pago.getEstadoPago()).withRel("pagos-por-estado"),
                Link.of(API_GATEWAY + "/pagos/reserva/" + pago.getIdReserva()).withRel("pagos-por-reserva"),
                Link.of(API_GATEWAY + "/reservas/" + pago.getIdReserva()).withRel("reserva")
        );
    }
}
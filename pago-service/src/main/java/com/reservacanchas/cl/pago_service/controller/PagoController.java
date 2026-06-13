package com.reservacanchas.cl.pago_service.controller;

import com.reservacanchas.cl.pago_service.dto.PagoDTO;
import com.reservacanchas.cl.pago_service.model.Pago;
import com.reservacanchas.cl.pago_service.service.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Pagos",
        description = "Operaciones para la gestión de pagos asociados a reservas"
)
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
            description = "Obtiene todos los pagos registrados en el sistema"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<Pago>> listar() {

        return ResponseEntity.ok(pagoService.listar());
    }

    @Operation(
            summary = "Buscar pago por ID",
            description = "Obtiene un pago específico mediante su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(pagoService.buscarPorId(id));
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
            description = "Obtiene todos los pagos realizados con un método de pago específico"
    )
    @ApiResponse(responseCode = "200", description = "Pagos encontrados correctamente")
    @GetMapping("/metodo/{metodoPago}")
    public ResponseEntity<List<Pago>> buscarPorMetodoPago(
            @PathVariable String metodoPago) {

        return ResponseEntity.ok(
                pagoService.buscarPorMetodoPago(metodoPago)
        );
    }

    @Operation(
            summary = "Buscar pagos por estado",
            description = "Obtiene todos los pagos según su estado"
    )
    @ApiResponse(responseCode = "200", description = "Pagos encontrados correctamente")
    @GetMapping("/estado/{estadoPago}")
    public ResponseEntity<List<Pago>> buscarPorEstadoPago(
            @PathVariable String estadoPago) {

        return ResponseEntity.ok(
                pagoService.buscarPorEstadoPago(estadoPago)
        );
    }

    @Operation(
            summary = "Buscar pagos por reserva",
            description = "Obtiene todos los pagos asociados a una reserva específica"
    )
    @ApiResponse(responseCode = "200", description = "Pagos encontrados correctamente")
    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<Pago>> buscarPorReserva(
            @PathVariable Long idReserva) {

        return ResponseEntity.ok(
                pagoService.buscarPorReserva(idReserva)
        );
    }
}
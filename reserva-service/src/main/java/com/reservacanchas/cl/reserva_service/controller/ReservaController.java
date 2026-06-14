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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Reservas",
        description = "Operaciones para la gestión de reservas de canchas y validación entre microservicios"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/reservas")
public class ReservaController {

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
            description = "Obtiene todas las reservas registradas en el sistema"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<Reserva>> listar() {

        return ResponseEntity.ok(reservaService.listar());
    }

    @Operation(
            summary = "Buscar reserva por ID",
            description = "Obtiene una reserva específica mediante su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(reservaService.buscarPorId(id));
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
            description = "Obtiene todas las reservas según su estado"
    )
    @ApiResponse(responseCode = "200", description = "Reservas encontradas correctamente")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Reserva>> buscarPorEstado(@PathVariable String estado) {

        return ResponseEntity.ok(
                reservaService.buscarPorEstado(estado)
        );
    }

    @Operation(
            summary = "Buscar reservas por usuario",
            description = "Obtiene todas las reservas asociadas a un usuario específico"
    )
    @ApiResponse(responseCode = "200", description = "Reservas encontradas correctamente")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Reserva>> buscarPorUsuario(@PathVariable Long idUsuario) {

        return ResponseEntity.ok(
                reservaService.buscarPorUsuario(idUsuario)
        );
    }

    @Operation(
            summary = "Buscar reservas por cancha",
            description = "Obtiene todas las reservas asociadas a una cancha específica"
    )
    @ApiResponse(responseCode = "200", description = "Reservas encontradas correctamente")
    @GetMapping("/cancha/{idCancha}")
    public ResponseEntity<List<Reserva>> buscarPorCancha(@PathVariable Long idCancha) {

        return ResponseEntity.ok(
                reservaService.buscarPorCancha(idCancha)
        );
    }
}
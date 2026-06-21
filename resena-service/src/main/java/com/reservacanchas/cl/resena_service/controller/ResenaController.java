package com.reservacanchas.cl.resena_service.controller;

import com.reservacanchas.cl.resena_service.dto.ResenaDTO;
import com.reservacanchas.cl.resena_service.model.Resena;
import com.reservacanchas.cl.resena_service.service.ResenaService;

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
        name = "Reseñas",
        description = "Operaciones para la gestión de reseñas de usuarios sobre las canchas"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/resenas")
public class ResenaController {

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
    public ResponseEntity<Resena> crear(
            @Valid @RequestBody ResenaDTO resenaDTO) {

        return new ResponseEntity<>(
                resenaService.guardar(resenaDTO),
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

        List<EntityModel<Resena>> resenas = resenaService.listar()
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Resena>> respuesta = CollectionModel.of(
                resenas,
                Link.of(API_GATEWAY + "/resenas").withSelfRel()
        );

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

        Resena resena = resenaService.buscarPorId(id);

        return ResponseEntity.ok(agregarLinks(resena));
    }

    @Operation(
            summary = "Actualizar reseña",
            description = "Actualiza los datos de una reseña existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Resena> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ResenaDTO resenaDTO) {

        return ResponseEntity.ok(
                resenaService.actualizar(id, resenaDTO)
        );
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

        resenaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de reseña",
            description = "Indica si una reseña existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        return resenaService.existePorId(id);
    }

    private EntityModel<Resena> agregarLinks(Resena resena) {

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
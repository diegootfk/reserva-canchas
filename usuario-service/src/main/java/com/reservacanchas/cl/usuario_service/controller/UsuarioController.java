package com.reservacanchas.cl.usuario_service.controller;

import com.reservacanchas.cl.usuario_service.dto.UsuarioDTO;
import com.reservacanchas.cl.usuario_service.model.Usuario;
import com.reservacanchas.cl.usuario_service.service.UsuarioService;

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
        name = "Usuarios",
        description = "Operaciones para la gestión de usuarios registrados en el sistema"
)
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private static final String API_GATEWAY = "http://localhost:7090";

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Crear usuario",
            description = "Registra un nuevo usuario en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Usuario> crear(@Valid @RequestBody UsuarioDTO usuarioDTO) {

        logger.info("Solicitud POST recibida para crear un nuevo usuario");

        Usuario usuario = usuarioService.guardar(usuarioDTO);

        logger.info("Usuario creado correctamente con ID: {}", usuario.getId());

        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar usuarios",
            description = "Obtiene todos los usuarios registrados con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> listar() {

        logger.info("Solicitud GET recibida para listar todos los usuarios");

        List<Usuario> usuariosEncontrados = usuarioService.listar();

        logger.info("Se encontraron {} usuarios registrados", usuariosEncontrados.size());

        List<EntityModel<Usuario>> usuarios = usuariosEncontrados
                .stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Usuario>> respuesta = CollectionModel.of(
                usuarios,
                Link.of(API_GATEWAY + "/usuarios").withSelfRel()
        );

        logger.info("Respuesta HATEOAS generada correctamente para listado de usuarios");

        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Buscar usuario por ID",
            description = "Obtiene un usuario específico mediante su identificador con enlaces HATEOAS apuntando al API Gateway"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> buscarPorId(@PathVariable Long id) {

        logger.info("Solicitud GET recibida para buscar usuario con ID: {}", id);

        Usuario usuario = usuarioService.buscarPorId(id);

        logger.info("Usuario encontrado correctamente con ID: {}", id);

        return ResponseEntity.ok(agregarLinks(usuario));
    }

    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza la información de un usuario existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO usuarioDTO) {

        logger.info("Solicitud PUT recibida para actualizar usuario con ID: {}", id);

        Usuario usuarioActualizado = usuarioService.actualizar(id, usuarioDTO);

        logger.info("Usuario actualizado correctamente con ID: {}", id);

        return ResponseEntity.ok(usuarioActualizado);
    }

    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario según su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        logger.warn("Solicitud DELETE recibida para eliminar usuario con ID: {}", id);

        usuarioService.eliminar(id);

        logger.info("Usuario eliminado correctamente con ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar existencia de usuario",
            description = "Indica si un usuario existe en la base de datos"
    )
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente")
    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        logger.info("Solicitud GET recibida para verificar existencia de usuario con ID: {}", id);

        boolean existe = usuarioService.existePorId(id);

        logger.info("Resultado de existencia para usuario ID {}: {}", id, existe);

        return existe;
    }

    private EntityModel<Usuario> agregarLinks(Usuario usuario) {

        logger.debug("Agregando enlaces HATEOAS para usuario con ID: {}", usuario.getId());

        return EntityModel.of(
                usuario,
                Link.of(API_GATEWAY + "/usuarios/" + usuario.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/usuarios").withRel("usuarios"),
                Link.of(API_GATEWAY + "/usuarios/" + usuario.getId() + "/exists").withRel("existe"),
                Link.of(API_GATEWAY + "/reservas/usuario/" + usuario.getId()).withRel("reservas")
        );
    }
}
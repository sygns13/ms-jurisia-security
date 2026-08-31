package pj.gob.pe.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pj.gob.pe.security.exception.ModeloNotFoundException;
import pj.gob.pe.security.model.beans.UserLogin;
import pj.gob.pe.security.model.entities.Aplicacion;
import pj.gob.pe.security.model.entities.Modulo;
import pj.gob.pe.security.model.entities.User;
import pj.gob.pe.security.service.UserService;
import pj.gob.pe.security.service.externals.AuthService;
import pj.gob.pe.security.utils.InputConsultaIA;
import pj.gob.pe.security.utils.UserIdsRequest;
import pj.gob.pe.security.utils.beans.IdsRequest;
import pj.gob.pe.security.utils.beans.InputCambioPassword;
import pj.gob.pe.security.utils.beans.InputPasswordReset;
import pj.gob.pe.security.utils.beans.ResponseRolUsuario;

import java.net.URI;
import java.util.List;

@Tag(name = "Users", description = "API para gestionar Usuarios")
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @Operation(summary = "Consulta Lista de todos los usuarios", description = "Retorna una Lista de todos los usuarios")
    @GetMapping("/get-all")
    public ResponseEntity<List<User>> listarAll(
            @RequestHeader("SessionId") String SessionId) throws Exception{

        authService.validarSesion(SessionId);

        List<User> resultado = userService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(summary = "Consulta Lista de usuarios Paginadas", description = "Retorna una Lista de usuarios paginadas")
    @GetMapping
    public ResponseEntity<Page<User>> listar(
            @RequestHeader("SessionId") String SessionId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "buscar", defaultValue = "") String buscar,
            @RequestParam(name = "dependenciaId", defaultValue = "0") long dependenciaId) throws Exception{

        authService.validarSesion(SessionId);

        Pageable pageable = PageRequest.of(page,size);
        Page<User> resultado = userService.listar(pageable, buscar, dependenciaId);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(summary = "Consulta un Usuario por ID", description = "Retorna una User filtrada por ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> listarPorId(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id) throws Exception{

        authService.validarSesion(SessionId);

        User obj = userService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<User>(obj, HttpStatus.OK);
    }

    @Operation(summary = "Creación de un Usuario", description = "Registro de un nuevo Usuario")
    @PostMapping
    public ResponseEntity<User> registrar(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody User a) throws Exception{

        UserLogin userLogin = authService.validarSesion(SessionId);

        a.setId(null);
        User obj = userService.registrar(a, userLogin.getIdUser());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @Operation(summary = "Modificación de un Usuario", description = "Modificación de un Usuario")
    @PutMapping
    public ResponseEntity<Integer> modificar(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody User a) throws Exception{

        UserLogin userLogin = authService.validarSesion(SessionId);

        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        User objBD = userService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ a.getId());
        }

        int obj = userService.modificar(a, userLogin.getIdUser());

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @Operation(summary = "Elimina un Usuario por ID", description = "Elimina un Usuario por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id) throws Exception{

        UserLogin userLogin = authService.validarSesion(SessionId);

        User obj = userService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        userService.eliminar(id, userLogin.getIdUser());

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Activa o Desactiva un Usuario por ID", description = "Activa o Desactiva un Usuario por ID")
    @PatchMapping("/activation/{id}/{valor}")
    public ResponseEntity<Void> altabaja(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id, @PathVariable("valor") Integer valor) throws Exception{

        UserLogin userLogin = authService.validarSesion(SessionId);

        User obj = userService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        userService.altabaja(id, valor, userLogin.getIdUser());

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    // Endpoint de consumo interservicios (ms-jurisia-metricas): no exige SessionId
    @Operation(summary = "Busqueda de Usuarios por Criterios", description = "Busqueda de Usuarios por Criterios")
    @PostMapping("/listar-interservices")
    public ResponseEntity<List<User>> reportCabConsultaIA(@Valid @RequestBody InputConsultaIA inputData) throws Exception{

        List<User> dataResponse = userService.buscarUsuarios(inputData);

        if(dataResponse == null) {
            throw new ModeloNotFoundException("Error de procesamiento de Datos. Comunicarse con un administrador ");
        }

        return new ResponseEntity<List<User>>(dataResponse, HttpStatus.OK);
    }

    // Endpoint de consumo interservicios (ms-jurisia-metricas): no exige SessionId
    @Operation(summary = "Busqueda de Usuarios por Criterios", description = "Busqueda de Usuarios por Criterios")
    @PostMapping("/by-ids")
    public ResponseEntity<List<User>> getUsersByIds(@RequestBody UserIdsRequest request) throws Exception {
        List<User> users = userService.getUsersByIds(request);
        return ResponseEntity.ok(users);
    }

    // =====================================================================
    // Aplicaciones del Usuario (AplicacionsHasUsers)
    // =====================================================================

    @Operation(summary = "Consulta las Aplicaciones de un Usuario",
               description = "Retorna las Aplicaciones asignadas al Usuario en AplicacionsHasUsers")
    @GetMapping("/{id}/aplicaciones")
    public ResponseEntity<List<Aplicacion>> listarAplicaciones(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id) throws Exception{

        authService.validarSesion(SessionId);

        this.validarUsuario(id);

        return new ResponseEntity<>(userService.listarAplicaciones(id), HttpStatus.OK);
    }

    @Operation(summary = "Asigna una Aplicacion a un Usuario",
               description = "Registra la relacion Aplicacion - Usuario en AplicacionsHasUsers")
    @PostMapping("/{id}/aplicaciones/{aplicacionId}")
    public ResponseEntity<List<Aplicacion>> asignarAplicacion(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id,
            @PathVariable("aplicacionId") Long aplicacionId) throws Exception{

        authService.validarSesion(SessionId);

        this.validarUsuario(id);

        userService.asignarAplicacion(id, aplicacionId);

        return new ResponseEntity<>(userService.listarAplicaciones(id), HttpStatus.CREATED);
    }

    @Operation(summary = "Quita una Aplicacion a un Usuario",
               description = "Elimina la relacion Aplicacion - Usuario de AplicacionsHasUsers")
    @DeleteMapping("/{id}/aplicaciones/{aplicacionId}")
    public ResponseEntity<Void> quitarAplicacion(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id,
            @PathVariable("aplicacionId") Long aplicacionId) throws Exception{

        authService.validarSesion(SessionId);

        this.validarUsuario(id);

        userService.quitarAplicacion(id, aplicacionId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Reemplaza las Aplicaciones de un Usuario",
               description = "El Usuario queda asignado exactamente a las Aplicaciones indicadas. Una lista vacia lo deja sin Aplicaciones")
    @PutMapping("/{id}/aplicaciones")
    public ResponseEntity<List<Aplicacion>> reemplazarAplicaciones(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id,
            @RequestBody IdsRequest request) throws Exception{

        authService.validarSesion(SessionId);

        this.validarUsuario(id);

        List<Aplicacion> resultado = userService.reemplazarAplicaciones(id, request != null ? request.getIds() : null);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    // =====================================================================
    // Roles del Usuario (UsersHasRoles)
    // =====================================================================

    @Operation(summary = "Consulta los Roles de un Usuario",
               description = "Retorna los Roles asignados en UsersHasRoles, indicando en efectivoEnKeycloak si cada uno se propaga realmente a Keycloak")
    @GetMapping("/{id}/roles")
    public ResponseEntity<List<ResponseRolUsuario>> listarRoles(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id) throws Exception{

        authService.validarSesion(SessionId);

        this.validarUsuario(id);

        return new ResponseEntity<>(userService.listarRoles(id), HttpStatus.OK);
    }

    @Operation(summary = "Asigna un Rol a un Usuario",
               description = "Registra la relacion Rol - Usuario en UsersHasRoles con su auditoria. La respuesta advierte si el Rol no llega a Keycloak")
    @PostMapping("/{id}/roles/{roleId}")
    public ResponseEntity<List<ResponseRolUsuario>> asignarRol(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id,
            @PathVariable("roleId") Long roleId) throws Exception{

        UserLogin userLogin = authService.validarSesion(SessionId);

        this.validarUsuario(id);

        List<ResponseRolUsuario> resultado = userService.asignarRol(id, roleId, userLogin.getIdUser());

        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    @Operation(summary = "Quita un Rol a un Usuario",
               description = "Elimina la relacion Rol - Usuario de UsersHasRoles")
    @DeleteMapping("/{id}/roles/{roleId}")
    public ResponseEntity<List<ResponseRolUsuario>> quitarRol(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id,
            @PathVariable("roleId") Long roleId) throws Exception{

        authService.validarSesion(SessionId);

        this.validarUsuario(id);

        List<ResponseRolUsuario> resultado = userService.quitarRol(id, roleId);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(summary = "Reemplaza los Roles de un Usuario",
               description = "El Usuario queda con exactamente los Roles indicados. Una lista vacia lo deja sin Roles")
    @PutMapping("/{id}/roles")
    public ResponseEntity<List<ResponseRolUsuario>> reemplazarRoles(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id,
            @RequestBody IdsRequest request) throws Exception{

        UserLogin userLogin = authService.validarSesion(SessionId);

        this.validarUsuario(id);

        List<ResponseRolUsuario> resultado = userService.reemplazarRoles(
                id, request != null ? request.getIds() : null, userLogin.getIdUser());

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    // =====================================================================
    // Contraseña
    // =====================================================================

    @Operation(summary = "Restablece la contrasena de un Usuario",
               description = "Cambio administrativo: asigna una nueva contrasena al Usuario indicado sin exigir la anterior. " +
                             "La contrasena se guarda encriptada y queda registrada la traza de edicion con el usuario de la sesion")
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> cambiarPassword(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id,
            @Valid @RequestBody InputPasswordReset input) throws Exception{

        UserLogin userLogin = authService.validarSesion(SessionId);

        this.validarUsuario(id);

        userService.cambiarPassword(id, input.getPasswordNuevo(), userLogin.getIdUser());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Cambia la contrasena del Usuario de la sesion",
               description = "Autoservicio: el Usuario dueno de la sesion cambia su propia contrasena. " +
                             "Exige la contrasena actual y no permite modificar la de otro Usuario, " +
                             "porque el Usuario se toma del SessionId y nunca de la URL")
    @PatchMapping("/cambiar-password")
    public ResponseEntity<Void> cambiarPasswordPropio(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody InputCambioPassword input) throws Exception{

        UserLogin userLogin = authService.validarSesion(SessionId);

        userService.cambiarPasswordPropio(userLogin.getIdUser(), input.getPasswordActual(), input.getPasswordNuevo());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // =====================================================================
    // Modulos efectivos del Usuario
    // =====================================================================

    @Operation(summary = "Consulta los Modulos a los que llega un Usuario",
               description = "Modulos de las Aplicaciones del Usuario cuyos Roles coinciden con los Roles que tiene asignados. " +
                             "Es la misma resolucion que consume Keycloak a traves del proveedor de usuarios remoto")
    @GetMapping("/{id}/modulos")
    public ResponseEntity<List<Modulo>> listarModulosEfectivos(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id) throws Exception{

        authService.validarSesion(SessionId);

        this.validarUsuario(id);

        return new ResponseEntity<>(userService.listarModulosEfectivos(id), HttpStatus.OK);
    }

    /** Verifica que el Usuario exista y no este borrado. */
    private void validarUsuario(Long id) throws Exception {
        User obj = userService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
    }
}

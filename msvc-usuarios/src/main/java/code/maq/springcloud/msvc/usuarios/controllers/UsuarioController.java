package code.maq.springcloud.msvc.usuarios.controllers;

import code.maq.springcloud.msvc.usuarios.entities.Usuario;
import code.maq.springcloud.msvc.usuarios.services.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UsuarioController {
    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listarUsuarios(){
        return this.usuarioService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalleUsuario(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = usuarioService.porId(id);
        if (usuarioOptional.isPresent()) {
            return ResponseEntity.ok(usuarioOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()){
            return validarCampos(result);
        }
        if (!usuario.getEmail().isEmpty() && usuarioService.porEmail(usuario.getEmail())){
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje","Ya existe un usuario con el correo electronico ingresado!"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarUsuario(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id) {
        Optional<Usuario> usuarioOptional = usuarioService.porId(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuarioDb = usuarioOptional.get();
            if (result.hasErrors()){
                return validarCampos(result);
            }
            if (!usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail()) && usuarioService.porEmail(usuario.getEmail())){
                return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje","Ya existe un usuario con el correo electronico ingresado!"));
            }
            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuarioDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = usuarioService.porId(id);
        if (usuarioOptional.isPresent()) {
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Map<String, String>> validarCampos(BindingResult result) {
        Map<String,String> errores = new HashMap<>();
        result.getFieldErrors().forEach(fieldError ->{
            errores.put(fieldError.getField(),"El campo "+fieldError.getField()+" "+fieldError.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerUsuariosPorCurso(@RequestParam List<Long> ids){
        return ResponseEntity.ok(usuarioService.listarPorIds(ids));
    }
}

package code.maq.springcloud.msvc.cursos.controllers;

import code.maq.springcloud.msvc.cursos.models.Usuario;
import code.maq.springcloud.msvc.cursos.models.entities.Curso;
import code.maq.springcloud.msvc.cursos.services.ICursoService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class CursoController {
    @Autowired
    private ICursoService cursoService;

    @GetMapping
    public List<Curso> listarCursos(){
        return cursoService.listar();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> detalleCurso(@PathVariable Long id){
        Optional<Curso> cursoOptional = cursoService.porIdConUsuarios(id);
        if (cursoOptional.isPresent()){
            return ResponseEntity.ok(cursoOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crearCurso(@Valid @RequestBody Curso curso, BindingResult result){
        if (result.hasErrors()){
            return validarCampos(result);
        }
        Curso cursodb= cursoService.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursodb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarCurso(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id){
        Optional<Curso> cursoOptional=cursoService.porId(id);
        if (cursoOptional.isPresent()){
            Curso cursodb = cursoOptional.get();
            if (result.hasErrors()){
                return validarCampos(result);
            }
            cursodb.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(cursodb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCurso(@PathVariable Long id){
        Optional<Curso> cursoOptional=cursoService.porId(id);
        if(cursoOptional.isPresent()){
            cursoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarCurso(@PathVariable Long cursoId, @RequestBody Usuario usuario){
        Optional<Usuario> o = null;
        try {
            o = cursoService.asignarUsuario(usuario, cursoId);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensagge","Error en la comunicación: "+e.getMessage()));
        }
        if (o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearCurso(@PathVariable Long cursoId, @RequestBody Usuario usuario){
        Optional<Usuario> o = null;
        try {
            o = cursoService.crearUsuario(usuario, cursoId);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensagge","Error en la comunicación: "+e.getMessage()));
        }
        if (o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/desasignar-usuario/{cursoId}")
    public ResponseEntity<?> desasignarCurso(@PathVariable Long cursoId, @RequestBody Usuario usuario){
        Optional<Usuario> o = null;
        try {
            o = cursoService.desasignarUsuario(usuario, cursoId);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensagge","Error en la comunicación: "+e.getMessage()));
        }
        if (o.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Map<String, String>> validarCampos(BindingResult result) {
        Map<String,String> errores= new HashMap<>();
        result.getFieldErrors().forEach(fieldError -> {
            errores.put(fieldError.getField(),"El campo "+fieldError.getField()+" "+fieldError.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }


}

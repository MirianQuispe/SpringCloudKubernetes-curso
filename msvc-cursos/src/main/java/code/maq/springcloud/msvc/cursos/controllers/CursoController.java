package code.maq.springcloud.msvc.cursos.controllers;

import code.maq.springcloud.msvc.cursos.models.entities.Curso;
import code.maq.springcloud.msvc.cursos.services.ICursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Optional<Curso> cursoOptional = cursoService.porId(id);
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

    private static ResponseEntity<Map<String, String>> validarCampos(BindingResult result) {
        Map<String,String> errores= new HashMap<>();
        result.getFieldErrors().forEach(fieldError -> {
            errores.put(fieldError.getField(),"El campo "+fieldError.getField()+" "+fieldError.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }


}

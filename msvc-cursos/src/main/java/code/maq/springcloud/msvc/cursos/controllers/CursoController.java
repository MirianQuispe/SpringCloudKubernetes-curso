package code.maq.springcloud.msvc.cursos.controllers;

import code.maq.springcloud.msvc.cursos.entities.Curso;
import code.maq.springcloud.msvc.cursos.services.ICursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<?> crearCurso(@RequestBody Curso curso){
        Curso cursodb= cursoService.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarCurso(@RequestBody Curso curso, @PathVariable Long id){
        Optional<Curso> cursoOptional=cursoService.porId(id);
        if (cursoOptional.isPresent()){
            Curso cursodb = cursoOptional.get();
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


}

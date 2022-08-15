package code.maq.springcloud.msvc.cursos.services;

import code.maq.springcloud.msvc.cursos.entities.Curso;

import java.util.List;
import java.util.Optional;

public interface ICursoService {
    List<Curso> listar();
    Optional<Curso> porId(Long id);
    Curso guardar(Curso curso);
    void eliminar(Long id);

}
package code.maq.springcloud.msvc.cursos.repositories;

import code.maq.springcloud.msvc.cursos.entities.Curso;
import org.springframework.data.repository.CrudRepository;

public interface CursoRepository extends CrudRepository<Curso,Long> {
}

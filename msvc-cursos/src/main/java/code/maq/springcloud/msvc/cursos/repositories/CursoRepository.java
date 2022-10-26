package code.maq.springcloud.msvc.cursos.repositories;

import code.maq.springcloud.msvc.cursos.models.entities.Curso;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CursoRepository extends CrudRepository<Curso,Long> {
    @Modifying
    @Query("delete from CursoUsuario cu where cu.usuarioId = ?1")
    void eliminarUsuarioPorCurso(Long idUsuario);
}

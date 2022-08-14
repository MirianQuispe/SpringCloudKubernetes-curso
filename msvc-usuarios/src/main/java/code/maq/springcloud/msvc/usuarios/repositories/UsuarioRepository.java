package code.maq.springcloud.msvc.usuarios.repositories;

import code.maq.springcloud.msvc.usuarios.entities.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
}
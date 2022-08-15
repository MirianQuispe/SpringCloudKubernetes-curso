package code.maq.springcloud.msvc.usuarios.repositories;

import code.maq.springcloud.msvc.usuarios.entities.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);
}
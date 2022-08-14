package code.maq.springcloud.msvc.usuarios.services;

import code.maq.springcloud.msvc.usuarios.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    public List<Usuario> listar();
    Optional <Usuario> porId(Long id);
    Usuario guardar( Usuario usuario);
    void eliminar(Long id);
}

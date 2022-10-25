package code.maq.springcloud.msvc.cursos.clients;

import code.maq.springcloud.msvc.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-usuarios",url = "localhost:8001")
public interface IUsuarioClientRest {
    @GetMapping("/{id}")
    Usuario detalle(@PathVariable Long id);
    @PostMapping("/")
    Usuario crear(@RequestBody Usuario usuario);

    @GetMapping("/usuarios-por-curso")
    List<Usuario> listarUsuariosPorCurso(@RequestParam Iterable<Long> ids);

}

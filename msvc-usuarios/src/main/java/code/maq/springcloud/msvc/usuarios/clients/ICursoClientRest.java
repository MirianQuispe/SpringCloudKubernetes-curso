package code.maq.springcloud.msvc.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-cursos", url = "localhost:8002")
public interface ICursoClientRest {
    @DeleteMapping("/eliminar-usuario/{usuarioId}")
    void eliminarCursoUsuarioPorUsuarioId(@PathVariable Long usuarioId);
}

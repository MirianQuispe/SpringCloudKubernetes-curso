package code.maq.springcloud.msvc.cursos.services;

import code.maq.springcloud.msvc.cursos.clients.IUsuarioClientRest;
import code.maq.springcloud.msvc.cursos.models.Usuario;
import code.maq.springcloud.msvc.cursos.models.entities.Curso;
import code.maq.springcloud.msvc.cursos.models.entities.CursoUsuario;
import code.maq.springcloud.msvc.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements ICursoService{
    @Autowired
    private CursoRepository repository;

    @Autowired
    private IUsuarioClientRest UsuarioClient;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return repository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = repository.findById(cursoId);
        if (o.isPresent()){
            Usuario usuarioMsvc = UsuarioClient.detalle(usuario.getId());

            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
            Curso curso = o.get();
            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);

            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = repository.findById(cursoId);
        if (o.isPresent()){
            Usuario usuarioMsvc = UsuarioClient.crear(usuario);

            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
            Curso curso = o.get();
            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);

            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> desasignarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = repository.findById(cursoId);
        if (o.isPresent()){
            Usuario usuarioMsvc = UsuarioClient.detalle(usuario.getId());

            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
            Curso curso = o.get();
            curso.removeCursoUsuario(cursoUsuario);
            repository.save(curso);

            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long id) {
        Optional<Curso> o=repository.findById(id);
        if (o.isPresent()){
            Curso curso = o.get();
            if (!curso.getCursoUsuarios().isEmpty()){
                List<Long> ids = curso.getCursoUsuarios()
                        .stream().map( cursoUsuario -> cursoUsuario.getUsuarioId())
                        .collect(Collectors.toList());
                List<Usuario> usuarios = UsuarioClient.listarUsuariosPorCurso(ids);
                curso.setUsuarios(usuarios);
                return Optional.of(curso);
            }
        }
        return Optional.empty();
    }
}

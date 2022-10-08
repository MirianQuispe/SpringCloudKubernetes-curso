package code.maq.springcloud.msvc.cursos.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="cursos")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    private String nombre;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "curso_id")
    private List<CursoUsuario> cursoUsuarios;

    public Curso() {
        cursoUsuarios = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<CursoUsuario> getCursoUsuarios() { return cursoUsuarios; }

    public void setCursoUsuarios(List<CursoUsuario> cursoUsuarios) { this.cursoUsuarios = cursoUsuarios; }

    public void addCursoUsuario(CursoUsuario cursoUsuario){
        cursoUsuarios.add(cursoUsuario);
    }

    public void removeCursoUsuario(CursoUsuario cursoUsuario){
        cursoUsuarios.remove(cursoUsuario);
    }
}

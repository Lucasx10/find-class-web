package findclass.web_application.domain.aula;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import findclass.web_application.domain.professor.Professor;
import findclass.web_application.domain.aluno.Aluno;

@Entity
@Table(name = "aulas")
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;
    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    private LocalDateTime data;
    @Deprecated
    public Aula(){}

    public Aula(Professor professor, Aluno aluno, DadosAgendamentoAula dados) {
        modificarDados(professor, aluno, dados);
    }

    public void modificarDados(Professor professor, Aluno aluno, DadosAgendamentoAula dados) {
        this.professor = professor;
        this.aluno = aluno;
        this.data = dados.data();
    }
    public Long getId() {
        return id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Professor getProfessor() {
        return professor;
    }

    public LocalDateTime getData() {
        return data;
    }

}

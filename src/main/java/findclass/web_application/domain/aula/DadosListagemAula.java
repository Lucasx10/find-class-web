package findclass.web_application.domain.aula;

import java.time.LocalDateTime;
import findclass.web_application.domain.professor.Especialidade;

public record DadosListagemAula(Long id, String professor, String aluno, LocalDateTime data, Especialidade especialidade) {
    public DadosListagemAula(Aula aula) {
        this(aula.getId(), aula.getProfessor().getNome(), aula.getAluno().getNome(), aula.getData(), aula.getProfessor().getEspecialidade());
    }
}

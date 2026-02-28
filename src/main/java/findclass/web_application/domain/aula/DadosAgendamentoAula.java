package findclass.web_application.domain.aula;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import findclass.web_application.domain.professor.Especialidade;

public record DadosAgendamentoAula(

        Long id,
        Long idProfessor,

        @NotNull
        String aluno,

        @NotNull
        @Future
        LocalDateTime data,

        Especialidade especialidade) {
}

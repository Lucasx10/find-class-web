package findclass.web_application.domain.professor;

public record DadosListagemProfessor(Long id, String nome, String email, String cpf, Especialidade especialidade) {
    public DadosListagemProfessor(Professor professor) {
        this(professor.getId(), professor.getNome(), professor.getEmail(), professor.getCpf(), professor.getEspecialidade());
    }
}

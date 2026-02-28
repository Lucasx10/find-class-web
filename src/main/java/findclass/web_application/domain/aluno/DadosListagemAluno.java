package findclass.web_application.domain.aluno;

public record DadosListagemAluno(Long id, String nome, String email, String cpf, String telefone) {
    public DadosListagemAluno(Aluno aluno) {
        this(aluno.getId(), aluno.getNome(), aluno.getEmail(), aluno.getCpf(), aluno.getTelefone());
    }
}

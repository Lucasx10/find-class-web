package findclass.web_application.domain.aluno;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "alunos")
public class Aluno {

    @Id
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;

    @Deprecated
    public Aluno(){}

    public Aluno(Long id, DadosCadastroAluno dados) {
        this.id = id;
        modificarDados(dados);
    }

    public void modificarDados(DadosCadastroAluno dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.cpf = dados.cpf();
    }
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCpf() {
        return cpf;
    }
}

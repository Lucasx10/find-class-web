package findclass.web_application.domain.professor;

import jakarta.persistence.*;

@Entity
@Table(name = "professores")
public class Professor {

    @Id
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    @Deprecated
    public Professor(){}

    public Professor(Long id, DadosCadastroProfessor dados) {
        this.id = id;
        atualizarDados(dados);
    }

    public void atualizarDados(DadosCadastroProfessor dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.cpf = dados.cpf();
        this.especialidade = dados.especialidade();
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

    public Especialidade getEspecialidade() {
        return especialidade;
    }

}

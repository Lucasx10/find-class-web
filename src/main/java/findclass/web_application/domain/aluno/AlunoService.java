package findclass.web_application.domain.aluno;

import jakarta.transaction.Transactional;
import findclass.web_application.domain.RegraDeNegocioException;
import findclass.web_application.domain.usuario.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlunoService {

    private final AlunoRepository repository;
    private final UsuarioService usuarioService;

    public AlunoService(AlunoRepository repository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    public Page<DadosListagemAluno> listar(Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosListagemAluno::new);
    }

    @Transactional
    public void cadastrar(DadosCadastroAluno dados) {
        if (repository.isJaCadastrado(dados.email(), dados.cpf(), dados.id())) {
            throw new RegraDeNegocioException("E-mail ou CPF já cadastrado para outro aluno!");
        }

        if (dados.id() == null) {
            Long usuarioId = usuarioService.salvarUsuario(dados.nome(), dados.email(), dados.cpf());
            repository.save(new Aluno(usuarioId, dados));
        } else {
            var aluno = repository.findById(dados.id()).orElseThrow();
            aluno.modificarDados(dados);
        }
    }

    public DadosCadastroAluno carregarPorId(Long id) {
        var aluno = repository.findById(id).orElseThrow();
        return new DadosCadastroAluno(aluno.getId(), aluno.getNome(), aluno.getEmail(), aluno.getTelefone(), aluno.getCpf());
    }

    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
        usuarioService.excluir(id);
    }

}

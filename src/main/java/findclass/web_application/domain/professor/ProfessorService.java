package findclass.web_application.domain.professor;

import jakarta.transaction.Transactional;
import findclass.web_application.domain.RegraDeNegocioException;
import findclass.web_application.domain.usuario.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    private final ProfessorRepository repository;
    private final UsuarioService usuarioService;

    public ProfessorService(ProfessorRepository repository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    public Page<DadosListagemProfessor> listar(Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosListagemProfessor::new);
    }

    @Transactional
    public void cadastrar(DadosCadastroProfessor dados) {
        if (repository.isJaCadastrado(dados.email(), dados.cpf(), dados.id())) {
            throw new RegraDeNegocioException("E-mail ou CRM já cadastrado para outro médico!");
        }

        if (dados.id() == null) {
            Long id = usuarioService.salvarUsuario(dados.nome(), dados.email(), dados.cpf());
            repository.save(new Professor(id, dados));
        } else {
            var professor = repository.findById(dados.id()).orElseThrow();
            professor.atualizarDados(dados);
        }
    }

    public DadosCadastroProfessor carregarPorId(Long id) {
        var professor = repository.findById(id).orElseThrow();
        return new DadosCadastroProfessor(professor.getId(), professor.getNome(), professor.getEmail(), professor.getTelefone(), professor.getCpf(), professor.getEspecialidade());
    }

    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
        usuarioService.excluir(id);
    }

    public List<DadosListagemProfessor> listarPorEspecialidade(Especialidade especialidade) {
        return repository.findByEspecialidade(especialidade).stream().map(DadosListagemProfessor::new).toList();
    }

}

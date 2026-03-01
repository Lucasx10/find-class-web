package findclass.web_application.domain.aula;

import jakarta.transaction.Transactional;
import findclass.web_application.domain.professor.ProfessorRepository;
import findclass.web_application.domain.usuario.Perfil;
import findclass.web_application.domain.usuario.Usuario;
import findclass.web_application.domain.RegraDeNegocioException;
import findclass.web_application.domain.aluno.AlunoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AulaService {

    private final AulaRepository repository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;

    public AulaService(AulaRepository repository, ProfessorRepository professorRepository, AlunoRepository alunoRepository) {
        this.repository = repository;
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
    }

    public Page<DadosListagemAula> listar(Pageable paginacao, Usuario logado) {
        if (logado.getPerfil() == Perfil.ATENDENTE) {
            return repository.findAllByOrderByData(paginacao).map(DadosListagemAula::new);
        }
        return repository.buscaPersonalizadaAulas(logado.getId(), paginacao).map(DadosListagemAula::new);
    }

    @Transactional
    public void cadastrar(DadosAgendamentoAula dados, Usuario logado) {
        var professorAula = professorRepository.findById(dados.idProfessor()).orElseThrow();
        var alunoAula = alunoRepository.findByCpf(dados.aluno()).orElseThrow();
        if(logado.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ALUNO"))
            && !alunoAula.getId().equals(logado.getId()))
        throw new RegraDeNegocioException("Cpf inválido!");

        if (dados.id() == null) {
            repository.save(new Aula(professorAula, alunoAula, dados));
        } else {
            var aula = repository.findById(dados.id()).orElseThrow();
            aula.modificarDados(professorAula, alunoAula, dados);
        }
    }

    @PreAuthorize("hasRole('ATENDENTE') or" +
        "(hasRole('ALUNO') and @aulaRepository.findById(#id).get().aluno.id == principal.id)")
    public DadosAgendamentoAula carregarPorId(Long id) {
        var aula = repository.findById(id).orElseThrow();
        return new DadosAgendamentoAula(aula.getId(), aula.getProfessor().getId(), aula.getAluno().getNome(), aula.getData(), aula.getProfessor().getEspecialidade());
    }

    @PreAuthorize("hasRole('ATENDENTE') or" +
        "(hasRole('ALUNO') and @aulaRepository.findById(#id).get().aluno.id == principal.id) or " +
        "(hasRole('PROFESSOR') and @aulaRepository.findById(#id).get().professor.id == principal.id)")
    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }

}

package findclass.web_application.domain.aula;

import jakarta.transaction.Transactional;
import findclass.web_application.domain.professor.ProfessorRepository;
import findclass.web_application.domain.aluno.AlunoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<DadosListagemAula> listar(Pageable paginacao) {
        return repository.findAllByOrderByData(paginacao).map(DadosListagemAula::new);
    }

    @Transactional
    public void cadastrar(DadosAgendamentoAula dados) {
        var professorAula = professorRepository.findById(dados.idProfessor()).orElseThrow();
        var alunoAula = alunoRepository.findByCpf(dados.aluno()).orElseThrow();
        if (dados.id() == null) {
            repository.save(new Aula(professorAula, alunoAula, dados));
        } else {
            var aula = repository.findById(dados.id()).orElseThrow();
            aula.modificarDados(professorAula, alunoAula, dados);
        }
    }

    public DadosAgendamentoAula carregarPorId(Long id) {
        var aula = repository.findById(id).orElseThrow();
        return new DadosAgendamentoAula(aula.getId(), aula.getProfessor().getId(), aula.getAluno().getNome(), aula.getData(), aula.getProfessor().getEspecialidade());
    }

    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }

}

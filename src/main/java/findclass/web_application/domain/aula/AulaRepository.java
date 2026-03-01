package findclass.web_application.domain.aula;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AulaRepository extends JpaRepository<Aula, Long> {
    Page<Aula> findAllByOrderByData(Pageable paginacao);

    @Query("SELECT c FROM Aula c " +
            "WHERE (c.professor.id = :id OR c.aluno.id = :id)" +
            " ORDER BY c.data")
    Page<Aula> buscaPersonalizadaAulas(Long id, Pageable paginacao);
}

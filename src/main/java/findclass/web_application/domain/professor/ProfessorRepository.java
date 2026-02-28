package findclass.web_application.domain.professor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    @Query("""
            SELECT
                CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END
            FROM
                Professor p
            WHERE (p.email = :email OR p.cpf = :cpf) AND (:id IS NULL OR p.id <> :id)
            """)
    boolean isJaCadastrado(String email, String cpf, Long id);

    List<Professor> findByEspecialidade(Especialidade especialidade);
}

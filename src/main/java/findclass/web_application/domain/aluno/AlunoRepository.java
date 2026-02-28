package findclass.web_application.domain.aluno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    @Query("""
            SELECT
                CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END
            FROM
                Aluno p
            WHERE (p.email = :email OR p.cpf = :cpf) AND (:id IS NULL OR p.id <> :id)
            """)
    boolean isJaCadastrado(String email, String cpf, Long id);

    Optional<Aluno> findByCpf(String cpf);
}

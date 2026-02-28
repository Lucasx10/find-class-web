package findclass.web_application.domain.aula;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AulaRepository extends JpaRepository<Aula, Long> {
    Page<Aula> findAllByOrderByData(Pageable paginacao);
}

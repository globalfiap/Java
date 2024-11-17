package repository;

import model.EstacaoSustentavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstacaoSustentavelRepository extends JpaRepository<EstacaoSustentavel, Long> {
}

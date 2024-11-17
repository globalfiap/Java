package repository;

import model.EstacaoRecarga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstacaoRecargaRepository extends JpaRepository<EstacaoRecarga, Long> {
}

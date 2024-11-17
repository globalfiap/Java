package repository;

import model.HistoricoCarregamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoCarregamentoRepository extends JpaRepository<HistoricoCarregamento, Long> {
}

package repository;

import model.GastoCarregamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GastoCarregamentoRepository extends JpaRepository<GastoCarregamento, Long> {
}

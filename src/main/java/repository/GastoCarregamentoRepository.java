package repository;

import model.GastoCarregamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GastoCarregamentoRepository extends JpaRepository<GastoCarregamento, Long> {
    List<GastoCarregamento> findByHistoricoCarregamentoHistoricoId(Long historicoId);
    List<GastoCarregamento> findByDataGastoBetween(LocalDateTime inicio, LocalDateTime fim);
}

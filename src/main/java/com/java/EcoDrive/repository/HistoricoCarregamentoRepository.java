package com.java.EcoDrive.repository;

import com.java.EcoDrive.model.HistoricoCarregamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoCarregamentoRepository extends JpaRepository<HistoricoCarregamento, Long> {
    List<HistoricoCarregamento> findByUsuarioUsuarioId(Long usuarioId);
    List<HistoricoCarregamento> findByVeiculoVeiculoId(Long veiculoId);
}


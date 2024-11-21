package com.java.EcoDrive.repository;

import com.java.EcoDrive.model.EstacaoRecarga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstacaoRecargaRepository extends JpaRepository<EstacaoRecarga, Long> {
    List<EstacaoRecarga> findByBairroBairroId(Long bairroId);
    List<EstacaoRecarga> findByTipoCarregadorContainingIgnoreCase(String tipoCarregador);
}

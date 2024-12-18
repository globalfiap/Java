package com.java.EcoDrive.repository;

import com.java.EcoDrive.model.EstacaoSustentavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstacaoSustentavelRepository extends JpaRepository<EstacaoSustentavel, Long> {
    List<EstacaoSustentavel> findByFonteEnergiaTipoEnergiaContainingIgnoreCase(String tipoEnergia);
}

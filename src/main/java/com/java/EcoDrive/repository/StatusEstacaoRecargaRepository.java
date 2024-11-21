package com.java.EcoDrive.repository;

import com.java.EcoDrive.model.EstacaoRecarga;
import com.java.EcoDrive.model.StatusEstacaoRecarga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusEstacaoRecargaRepository extends JpaRepository<StatusEstacaoRecarga, Long> {

    /**
     * Busca todos os status relacionados a uma estação de recarga específica.
     *
     * @param estacaoRecarga a estação de recarga associada.
     * @return lista de status relacionados à estação.
     */
    List<StatusEstacaoRecarga> findByEstacaoRecarga(EstacaoRecarga estacaoRecarga);
}

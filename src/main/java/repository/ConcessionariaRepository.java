package repository;

import model.Concessionaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcessionariaRepository extends JpaRepository<Concessionaria, Long> {
    List<Concessionaria> findByBairroBairroId(Long bairroId);
    List<Concessionaria> findByMarcaContainingIgnoreCase(String marca);
}

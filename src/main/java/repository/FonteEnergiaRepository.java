package repository;

import model.FonteEnergia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FonteEnergiaRepository extends JpaRepository<FonteEnergia, Long> {
    List<FonteEnergia> findByTipoEnergiaContainingIgnoreCase(String tipoEnergia);
}

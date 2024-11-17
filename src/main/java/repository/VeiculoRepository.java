package repository;

import model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    boolean existsByMarca(String marca);

    List<Veiculo> findByUsuarioNome(String nome);

    @Query("SELECT v FROM Veiculo v WHERE v.marca = :marca")
    List<Veiculo> buscarVeiculosPorMarca(@Param("marca") String marca);
}


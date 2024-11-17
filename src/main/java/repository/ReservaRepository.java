package repository;

import model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByStatus(Integer status);
    List<Reserva> findByUsuarioUsuarioId(Long usuarioId);
    List<Reserva> findByDataReservaBetween(LocalDateTime inicio, LocalDateTime fim);
}

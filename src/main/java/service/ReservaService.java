package service;

import dto.Reserva.ReservaCreateDTO;
import dto.Reserva.ReservaDTO;
import exception.ResourceNotFoundException;
import exception.InvalidRequestException;
import model.Reserva;
import model.Usuario;
import model.EstacaoRecarga;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ReservaRepository;
import repository.UsuarioRepository;
import repository.EstacaoRecargaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstacaoRecargaRepository estacaoRecargaRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository, UsuarioRepository usuarioRepository,
                          EstacaoRecargaRepository estacaoRecargaRepository, ModelMapper modelMapper) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.estacaoRecargaRepository = estacaoRecargaRepository;
        this.modelMapper = modelMapper;
    }

    public List<ReservaDTO> listarTodas() {
        List<Reserva> reservas = reservaRepository.findAll();
        return reservas.stream()
                .map(reserva -> modelMapper.map(reserva, ReservaDTO.class))
                .collect(Collectors.toList());
    }

    public ReservaDTO obterPorId(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada com ID: " + id));
        return modelMapper.map(reserva, ReservaDTO.class);
    }

    public List<ReservaDTO> listarPorStatus(Integer status) {
        List<Reserva> reservas = reservaRepository.findByStatus(status);
        if (reservas.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma reserva encontrada com o status: " + status);
        }
        return reservas.stream()
                .map(reserva -> modelMapper.map(reserva, ReservaDTO.class))
                .collect(Collectors.toList());
    }

    public List<ReservaDTO> listarPorUsuario(Long usuarioId) {
        List<Reserva> reservas = reservaRepository.findByUsuarioUsuarioId(usuarioId);
        if (reservas.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma reserva encontrada para o usuário com ID: " + usuarioId);
        }
        return reservas.stream()
                .map(reserva -> modelMapper.map(reserva, ReservaDTO.class))
                .collect(Collectors.toList());
    }

    public List<ReservaDTO> listarPorIntervaloDeDatas(LocalDateTime inicio, LocalDateTime fim) {
        List<Reserva> reservas = reservaRepository.findByDataReservaBetween(inicio, fim);
        if (reservas.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma reserva encontrada no intervalo de datas especificado.");
        }
        return reservas.stream()
                .map(reserva -> modelMapper.map(reserva, ReservaDTO.class))
                .collect(Collectors.toList());
    }

    public ReservaDTO criarReserva(ReservaCreateDTO reservaCreateDTO) {
        if (reservaCreateDTO.getUsuarioId() == null) {
            throw new InvalidRequestException("O ID do usuário é obrigatório.");
        }
        if (reservaCreateDTO.getEstacaoId() == null) {
            throw new InvalidRequestException("O ID da estação de recarga é obrigatório.");
        }
        if (reservaCreateDTO.getDataReserva() == null) {
            throw new InvalidRequestException("A data da reserva é obrigatória.");
        }

        Usuario usuario = usuarioRepository.findById(reservaCreateDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + reservaCreateDTO.getUsuarioId()));
        EstacaoRecarga estacaoRecarga = estacaoRecargaRepository.findById(reservaCreateDTO.getEstacaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estação de recarga não encontrada com ID: " + reservaCreateDTO.getEstacaoId()));

        Reserva reserva = modelMapper.map(reservaCreateDTO, Reserva.class);
        reserva.setUsuario(usuario);
        reserva.setEstacaoRecarga(estacaoRecarga);

        Reserva reservaSalva = reservaRepository.save(reserva);
        return modelMapper.map(reservaSalva, ReservaDTO.class);
    }

    public ReservaDTO atualizarReserva(Long id, ReservaCreateDTO reservaCreateDTO) {
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada com ID: " + id));

        if (reservaCreateDTO.getDataReserva() != null) {
            reservaExistente.setDataReserva(reservaCreateDTO.getDataReserva());
        }
        if (reservaCreateDTO.getStatus() != null) {
            reservaExistente.setStatus(reservaCreateDTO.getStatus());
        }

        Reserva reservaAtualizada = reservaRepository.save(reservaExistente);
        return modelMapper.map(reservaAtualizada, ReservaDTO.class);
    }

    public void deletarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada com ID: " + id));
        reservaRepository.delete(reserva);
    }
}

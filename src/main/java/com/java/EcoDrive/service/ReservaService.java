package com.java.EcoDrive.service;

import com.java.EcoDrive.dto.Reserva.ReservaCreateDTO;
import com.java.EcoDrive.dto.Reserva.ReservaDTO;
import com.java.EcoDrive.exception.ResourceNotFoundException;
import com.java.EcoDrive.exception.InvalidRequestException;
import com.java.EcoDrive.model.Reserva;
import com.java.EcoDrive.model.Usuario;
import com.java.EcoDrive.model.EstacaoRecarga;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.java.EcoDrive.repository.ReservaRepository;
import com.java.EcoDrive.repository.UsuarioRepository;
import com.java.EcoDrive.repository.EstacaoRecargaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {

    private static final String RESERVA_NAO_ENCONTRADA = "Reserva não encontrada com ID: ";

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

    public Page<ReservaDTO> listarTodasPaginado(Pageable pageable) {
        Page<Reserva> reservas = reservaRepository.findAll(pageable);
        return reservas.map(reserva -> modelMapper.map(reserva, ReservaDTO.class));
    }

    public List<ReservaDTO> listarTodas() {
        List<Reserva> reservas = reservaRepository.findAll();
        return reservas.stream()
                .map(reserva -> modelMapper.map(reserva, ReservaDTO.class))
                .toList();
    }

    public ReservaDTO obterPorId(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESERVA_NAO_ENCONTRADA + id));
        return modelMapper.map(reserva, ReservaDTO.class);
    }

    public List<ReservaDTO> listarPorStatus(Integer status) {
        List<Reserva> reservas = reservaRepository.findByStatus(status);
        if (reservas.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma reserva encontrada com o status: " + status);
        }
        return reservas.stream()
                .map(reserva -> modelMapper.map(reserva, ReservaDTO.class))
                .toList();
    }

    public List<ReservaDTO> listarPorUsuario(Long usuarioId) {
        List<Reserva> reservas = reservaRepository.findByUsuarioUsuarioId(usuarioId);
        if (reservas.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma reserva encontrada para o usuário com ID: " + usuarioId);
        }
        return reservas.stream()
                .map(reserva -> modelMapper.map(reserva, ReservaDTO.class))
                .toList();
    }

    public List<ReservaDTO> listarPorIntervaloDeDatas(LocalDateTime inicio, LocalDateTime fim) {
        List<Reserva> reservas = reservaRepository.findByDataReservaBetween(inicio, fim);
        if (reservas.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma reserva encontrada no intervalo de datas especificado.");
        }
        return reservas.stream()
                .map(reserva -> modelMapper.map(reserva, ReservaDTO.class))
                .toList();
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
                .orElseThrow(() -> new ResourceNotFoundException(RESERVA_NAO_ENCONTRADA + id));

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
                .orElseThrow(() -> new ResourceNotFoundException(RESERVA_NAO_ENCONTRADA + id));
        reservaRepository.delete(reserva);
    }
}
package service;

import dto.Reserva.ReservaCreateDTO;
import dto.Reserva.ReservaDTO;
import exception.ResourceNotFoundException;
import model.Reserva;
import model.Usuario;
import model.EstacaoRecarga;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ReservaRepository;
import repository.UsuarioRepository;
import repository.EstacaoRecargaRepository;

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

    public ReservaDTO criarReserva(ReservaCreateDTO reservaCreateDTO) {
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

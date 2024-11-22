package com.java.EcoDrive.repository;

import com.java.EcoDrive.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndUsuarioIdNot(String email, Long usuarioId); // Novo método

    List<Usuario> findByNomeContainingIgnoreCase(String nome);
}

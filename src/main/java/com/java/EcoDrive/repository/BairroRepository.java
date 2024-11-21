package com.java.EcoDrive.repository;

import com.java.EcoDrive.model.Bairro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BairroRepository extends JpaRepository<Bairro, Long> {
    List<Bairro> findByNomeContainingIgnoreCase(String nome);
}

package com.example.aula_data_jpa.repository;

import java.util.Optional;

import com.example.aula_data_jpa.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String username);

}
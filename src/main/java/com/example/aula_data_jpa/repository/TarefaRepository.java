package com.example.aula_data_jpa.repository;

import com.example.aula_data_jpa.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

}

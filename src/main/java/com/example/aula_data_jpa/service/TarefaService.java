package com.example.aula_data_jpa.service;

import java.util.List;
import java.util.Optional;

import com.example.aula_data_jpa.entity.Tarefa;
import com.example.aula_data_jpa.entity.Usuario;
import com.example.aula_data_jpa.entity.dtos.CriarTarefaDTO;
import com.example.aula_data_jpa.repository.TarefaRepository;
import org.springframework.stereotype.Service;

@Service
public class TarefaService {

    private TarefaRepository tarefaRepository;
    private UsuarioService usuarioService;

    public TarefaService(TarefaRepository tarefaRepository, UsuarioService usuarioService) {
        this.tarefaRepository = tarefaRepository;
        this.usuarioService = usuarioService;
    }

    public Tarefa criarTarefa(CriarTarefaDTO tarefaDTO) throws Exception {

        Tarefa tarefa = new Tarefa();
        tarefa.setDescricao(tarefaDTO.getDescricao());
        tarefa.setStatus(tarefaDTO.getStatus());

        Optional<Usuario> usuario = usuarioService.listarUsuarioPorCodigo(tarefaDTO.getUsuario());

        if (Optional.ofNullable(usuario).isPresent()) {
            tarefa.setUsuario(usuario.get());
            tarefaRepository.save(tarefa);
        } else {
            throw new Exception("Usuário não encontrado");
        }
        return tarefa;
    }

    public Tarefa atualizarTarefa(Tarefa tarefa) {
        tarefaRepository.save(tarefa);
        return tarefa;
    }

    public List<Tarefa> listarTarefas() {
        return tarefaRepository.findAll();
    }

    public Optional<Tarefa> listarTarefaPorCodigo(Long codigo) {
        Optional<Tarefa> tarefa = tarefaRepository.findById(codigo);
        return tarefa;
    }

    public void excluirTarefa(Long codigo) {
        tarefaRepository.deleteById(codigo);
    }

    public void atualizarStatusTarefa(Long codigo, String status) {
        Optional<Tarefa> tarefa = listarTarefaPorCodigo(codigo);
        if (Optional.ofNullable(tarefa).isPresent()) {
            tarefa.get().setStatus(status);
            tarefaRepository.save(tarefa.get());
        }

    }
}

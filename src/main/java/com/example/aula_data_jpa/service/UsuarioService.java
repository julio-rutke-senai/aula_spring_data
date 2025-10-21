package com.example.aula_data_jpa.service;

import java.util.*;

import com.example.aula_data_jpa.entity.Usuario;
import com.example.aula_data_jpa.entity.dtos.AlterarUsuarioDTO;
import com.example.aula_data_jpa.entity.dtos.AtualizarSenhaDTO;
import com.example.aula_data_jpa.entity.dtos.LoginDTO;
import com.example.aula_data_jpa.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    Map<String, Usuario> listaDeTokens;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.listaDeTokens = new HashMap<>();
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario criarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
        return usuario;
    }

    public AlterarUsuarioDTO atualizarUsuario(AlterarUsuarioDTO alterarUsuarioDTO) throws Exception {
        Optional<Usuario> usuario = usuarioRepository.findById(alterarUsuarioDTO.getCodigo());

        if (Optional.ofNullable(usuario).isPresent()) {
            usuario.get().setEmail(alterarUsuarioDTO.getEmail());
            usuario.get().setNome(alterarUsuarioDTO.getNome());
            usuario.get().setPermissao(alterarUsuarioDTO.getPermissao());
            usuario.get().setTelefone(alterarUsuarioDTO.getTelefone());
            usuarioRepository.save(usuario.get());

            return alterarUsuarioDTO;
        }

        throw new Exception("Usuário não existe!");
    }

    public Usuario atualizarSenhaUsuario(AtualizarSenhaDTO senhaUsuario) {
        Optional<Usuario> usuario = usuarioRepository.findById(senhaUsuario.getCodigo());
        usuario.get().setSenha(senhaUsuario.getSenha());
        usuarioRepository.save(usuario.get());
        return usuario.get();
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> listarUsuarioPorCodigo(Long codigo) {
        Optional<Usuario> usuario = usuarioRepository.findById(codigo);
        return usuario;
    }

    public void excluirUsuario(Long codigo) {
        usuarioRepository.deleteById(codigo);
    }

    public Optional<Usuario> getUsuarioAutenticacao(String username) {
        return usuarioRepository.findByEmail(username);
    }

    public Optional<Usuario> autenticar(LoginDTO loginDTO) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(loginDTO.getEmail());
        if(usuario.isPresent()){
            if(usuario.get().getSenha().equals(loginDTO.getSenha())){
                return usuario;
            }
        }
        return Optional.empty();
    }

    public String generateUuidToken(Usuario usuario) {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        listaDeTokens.put(uuidString, usuario);

        return uuidString;
    }

    public boolean validarToken(String cookieAutenticacao) {
        if(listaDeTokens.containsKey(cookieAutenticacao))
            return true;
        return false;
    }
}

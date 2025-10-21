package com.example.aula_data_jpa.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.aula_data_jpa.entity.Usuario;
import com.example.aula_data_jpa.entity.dtos.AlterarUsuarioDTO;
import com.example.aula_data_jpa.entity.dtos.AtualizarSenhaDTO;
import com.example.aula_data_jpa.entity.dtos.LoginDTO;
import com.example.aula_data_jpa.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
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

    public boolean autenticar(LoginDTO loginDTO) {
        Optional<Usuario> email = usuarioRepository.findByEmail(loginDTO.getEmail());
        if(email.isPresent()){
            if(email.get().getSenha().equals(loginDTO.getSenha())){
                return true;
            }
        }
        return false;
    }

    public String generateUuidToken() {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        return uuidString;
    }

}

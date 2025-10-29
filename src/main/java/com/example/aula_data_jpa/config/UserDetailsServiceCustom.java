package com.example.aula_data_jpa.config;

import com.example.aula_data_jpa.entity.Usuario;
import com.example.aula_data_jpa.service.UsuarioService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceCustom implements UserDetailsService {

    private final UsuarioService usuarioService;

    public UserDetailsServiceCustom(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioAutenticacao = usuarioService.getUsuarioAutenticacao(username);

        if(!usuarioAutenticacao.isPresent())
            throw new UsernameNotFoundException("Usuário não encontrado");

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(usuarioAutenticacao.get().getPermissao());
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(simpleGrantedAuthority);

        User userDetails = new User(usuarioAutenticacao.get().getEmail(), usuarioAutenticacao.get().getSenha(), authorities);

        return userDetails;
    }
}

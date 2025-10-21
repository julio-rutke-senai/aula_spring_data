package com.example.aula_data_jpa.controller;

import java.util.List;
import java.util.Optional;

import com.example.aula_data_jpa.entity.Usuario;
import com.example.aula_data_jpa.entity.dtos.AlterarUsuarioDTO;
import com.example.aula_data_jpa.entity.dtos.AtualizarSenhaDTO;
import com.example.aula_data_jpa.entity.dtos.LoginDTO;
import com.example.aula_data_jpa.service.UsuarioService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarUsuarios(@CookieValue(name = "SESSION_ID") String cookieAutenticacao){
        try {
            if(!Optional.ofNullable(cookieAutenticacao).isPresent())
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            if(!usuarioService.validarToken(cookieAutenticacao))
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            List<Usuario> usuarios = usuarioService.listarUsuarios();
            return ResponseEntity.ok(usuarios);
        } catch(Exception ex) {
            return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario){
        try {
            Usuario usuarioCriado = usuarioService.criarUsuario(usuario);
            return ResponseEntity.ok(usuarioCriado);
        }catch(Exception ex) {
            return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
        }
    }

    @PutMapping("/alterar")
    public ResponseEntity<?> atualizarUsuario(@RequestBody AlterarUsuarioDTO usuario){
        try {
            usuario = usuarioService.atualizarUsuario(usuario);
            return ResponseEntity.ok(usuario);
        }catch(Exception ex) {
            return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
        }
    }


    @PatchMapping("/alterar/senha")
    public ResponseEntity<?> atualizarSenhaUsuario(@RequestBody AtualizarSenhaDTO atualizarSenhaDTO){
        try {
            Usuario usuario = usuarioService.atualizarSenhaUsuario(atualizarSenhaDTO);
            return ResponseEntity.ok(usuario);
        }catch(Exception ex) {
            return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
        }
    }

    @DeleteMapping("/excluir/{codigo}")
    public ResponseEntity<?> excluirUsuario(@PathVariable Long codigo){
        try {
            usuarioService.excluirUsuario(codigo);
            return ResponseEntity.ok("Excluído com Sucesso");
        }catch(Exception ex) {
            return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
        }
    }

    @GetMapping("/buscar/{codigo}")
    public ResponseEntity<?> buscarUsuarioPorCodigo(@PathVariable Long codigo){
        try {
            Optional<Usuario> usuario = usuarioService.listarUsuarioPorCodigo(codigo);
            if(Optional.ofNullable(usuario).isPresent())
                return ResponseEntity.ok(usuario.get());
            else
                return ResponseEntity.notFound().build();

        }catch(Exception ex) {
            return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@RequestBody LoginDTO loginDTO, HttpServletResponse response){
        Optional<Usuario> autenticado = usuarioService.autenticar(loginDTO);
        if(autenticado.isPresent()){
            String token = usuarioService.generateUuidToken(autenticado.get());
            Cookie cookieAutenticacao = new Cookie("SESSION_ID", token);
            response.addCookie(cookieAutenticacao);
            return ResponseEntity.ok("Usuário Autenticado com sucesso!");
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}
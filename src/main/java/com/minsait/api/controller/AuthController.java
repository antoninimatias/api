package com.minsait.api.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minsait.api.controller.dto.GetTokenRequest;
import com.minsait.api.controller.dto.GetTokenResponse;
import com.minsait.api.repository.UsuarioEntity;
import com.minsait.api.repository.UsuarioRepository;
import com.minsait.api.sicurity.util.JWTUtil;

@RestController
@RequestMapping(value = "/auth")
public class AuthController implements AuthSwagger {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	JWTUtil jwtUtil;

	@PostMapping("/get-token")
	public ResponseEntity<GetTokenResponse> getToken(@RequestBody GetTokenRequest request) {

		// 1 - Buscar o usuário pelo login do request
		UsuarioEntity usuario = usuarioRepository.findByLogin(request.getUserName());

		// 2 - Comparar a senha do request com a senha criptografada no banco
		BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
		if (usuario != null && encode.matches(request.getPassword(), usuario.getSenha())) {
			final List<String> permissions = Arrays.asList(usuario.getPermissoes().split(","));

			final var token = jwtUtil.generateToken(usuario.getLogin(), permissions, 5);
			return new ResponseEntity<>(GetTokenResponse.builder().accessToken(token).build(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(GetTokenResponse.builder().build(), HttpStatus.UNAUTHORIZED);
		}

	}
}

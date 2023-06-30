package com.minsait.api.controller;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.minsait.api.controller.dto.MessageResponse;
import com.minsait.api.controller.dto.UsuarioRequest;
import com.minsait.api.controller.dto.UsuarioResponse;
import com.minsait.api.repository.UsuarioEntity;
import com.minsait.api.repository.UsuarioRepository;
import com.minsait.api.util.ObjectMapperUtil;

@RestController
@RequestMapping(value = "/api")
public class UsuarioController implements UsuarioSwagger {

	/**
	 * Criar endpoints para manter usuários
	 * 
	 * [GET] /api/usuario Retorna todos os usuários com paginação | permissão: LEITURA_USUARIO 
	 * [POST] /api/usuario Cria um usuário novo | permissão: ESCRITA_USUARIO
	 * [PUT] /api/usuario Altera um usuário | permissão: ESCRITA_USUARIO
	 * [DELETE] /api/usuario/{id} Exclui um usuário | permissão: ESCRITA_USUARIO
	 * [GET] /api/usuario/{id} Retorna um usuário | permissão: LEITURA_USUARIO
	 */
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	

	@PreAuthorize("hasAuthority('LEITURA_USUARIO')")
	@GetMapping("/usuario")
	public ResponseEntity<Page<UsuarioResponse>> findAll(@RequestParam(required = false) String nome,
																@RequestParam(required = false) String login,
																@RequestParam(required = false) String email,
																@RequestParam(required = false, defaultValue = "0") int page,
																@RequestParam(required = false, defaultValue = "10") int pageSize) {
		final var usuarioEntity = new UsuarioEntity();
		usuarioEntity.setLogin(login);
		usuarioEntity.setEmail(email);
		Pageable pageable = PageRequest.of(page, pageSize);

		final Page<UsuarioEntity> usuarioEntityListPage = usuarioRepository.findAll(usuarioEntity.usuarioEntitySpecification(), pageable);
		final Page<UsuarioResponse> usuarioResponseList = ObjectMapperUtil.mapAll(usuarioEntityListPage, UsuarioResponse.class);
		return ResponseEntity.ok(usuarioResponseList);
	}

	@PreAuthorize("hasAuthority('ESCRITA_USUARIO')")
	@PostMapping("/usuario")
	public ResponseEntity<UsuarioResponse> insert(@RequestBody UsuarioRequest request){

		final var usuarioEntity = ObjectMapperUtil.map(request, UsuarioEntity.class);
		usuarioEntity.encodePassword(request.getSenha());

		final var usuarioInserted = usuarioRepository.save(usuarioEntity);
		final var usuarioResponse = ObjectMapperUtil.map(usuarioInserted, UsuarioResponse.class);

		return new ResponseEntity<>(usuarioResponse, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAuthority('ESCRITA_USUARIO')")
	@PutMapping("/usuario")
	public ResponseEntity<UsuarioResponse> update(@RequestBody UsuarioRequest request){
		
		final var usuarioRequest = ObjectMapperUtil.map(request, UsuarioEntity.class);
		final var usuarioEntityFound = usuarioRepository.findById(usuarioRequest.getId());

	    if (usuarioEntityFound.isEmpty()) {
	    	return new ResponseEntity<>(new UsuarioResponse(), HttpStatus.NOT_FOUND);
	    }

	    final var usuarioEntity = usuarioEntityFound.get();

	    updateIfNotNull(usuarioEntity::setNome, usuarioRequest.getNome());
	    updateIfNotNull(usuarioEntity::setLogin, usuarioRequest.getLogin());
	    updateIfNotNull(usuarioEntity::encodePassword, usuarioRequest.getSenha());
	    updateIfNotNull(usuarioEntity::setEmail, usuarioRequest.getEmail());
	    updateIfNotNull(usuarioEntity::setPermissoes, usuarioRequest.getPermissoes());

	    final var updatedUsuarioEntity = usuarioRepository.save(usuarioEntity);
	    final var usuarioResponse = ObjectMapperUtil.map(updatedUsuarioEntity, UsuarioResponse.class);

		return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('ESCRITA_USUARIO')")
	@DeleteMapping("/usuario/{id}")
	public ResponseEntity<MessageResponse> delete(@PathVariable Long id){
		final var usuarioEntityFound = usuarioRepository.findById(id);
		if(usuarioEntityFound.isPresent()){
			usuarioRepository.delete(usuarioEntityFound.get());
		}else{
			return new ResponseEntity<>(MessageResponse.builder()
					.message("Usuário não encontrado!")
					.date(LocalDateTime.now())
					.error(false)
					.build(), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(MessageResponse.builder()
				.message("OK")
				.date(LocalDateTime.now())
				.error(false)
				.build(), HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('LEITURA_USUARIO')")
	@GetMapping("/usuario/{id}")
	public ResponseEntity<UsuarioResponse> findById(@PathVariable Long id){
		final var usuarioEntity = usuarioRepository.findById(id);
		UsuarioResponse usuarioResponse = new UsuarioResponse();

		if (usuarioEntity.isPresent()){
			usuarioResponse = ObjectMapperUtil.map(usuarioEntity.get(), UsuarioResponse.class);
		}else{
			return new ResponseEntity<>(usuarioResponse, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
	}
	
	private <T> void updateIfNotNull(Consumer<T> setter, T value) {
	    if (value != null) {
	        setter.accept(value);
	    }
	}
}

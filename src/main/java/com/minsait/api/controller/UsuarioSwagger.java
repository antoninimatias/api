package com.minsait.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.minsait.api.controller.dto.MessageResponse;
import com.minsait.api.controller.dto.UsuarioRequest;
import com.minsait.api.controller.dto.UsuarioResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name ="USUÁRIO")
public interface UsuarioSwagger {
	
	@Operation(summary = "Busca todos os usuarios", responses = {
			@ApiResponse(responseCode = "200", description = "Dados do usuario retornados com sucesso"),
			@ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
			@ApiResponse(responseCode = "500", description = "Erro interno"),
			@ApiResponse(responseCode = "403", description = "Acesso negado"), })
	public ResponseEntity<Page<UsuarioResponse>> findAll(String login, String nome, String email, int page, int pagesize);

	@Operation(summary = "Insere um novo usuario", responses = {
			@ApiResponse(responseCode = "200", description = "Usuario inserido com sucesso"),
			@ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
			@ApiResponse(responseCode = "500", description = "Erro interno"),
			@ApiResponse(responseCode = "403", description = "Acesso negado"), })
	public ResponseEntity<UsuarioResponse> insert(UsuarioRequest request);

	@Operation(summary = "Atualiza um cliente", responses = {
			@ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
			@ApiResponse(responseCode = "500", description = "Erro interno"),
			@ApiResponse(responseCode = "403", description = "Acesso negado"), })
	public ResponseEntity<UsuarioResponse> update(UsuarioRequest request);

	@Operation(summary = "Exclui um usuario", responses = {
			@ApiResponse(responseCode = "200", description = "Usuario excluído com sucesso"),
			@ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
			@ApiResponse(responseCode = "404", description = "Usuario não encontrado"),
			@ApiResponse(responseCode = "500", description = "Erro interno"),
			@ApiResponse(responseCode = "403", description = "Acesso negado"), })
	public ResponseEntity<MessageResponse> delete(Long id);

	@Operation(summary = "Busca um usuario pelo id", responses = {
			@ApiResponse(responseCode = "200", description = "Dados do usuario retornados com sucesso"),
			@ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
			@ApiResponse(responseCode = "500", description = "Erro interno"),
			@ApiResponse(responseCode = "403", description = "Acesso negado"), })
	public ResponseEntity<UsuarioResponse> findById(Long id);
}

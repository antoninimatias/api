package com.minsait.api.controller;

import com.minsait.api.controller.dto.ClienteRequest;
import com.minsait.api.controller.dto.ClienteResponse;
import com.minsait.api.controller.dto.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Tag(name ="CLIENTE")
public interface ApiSwagger {

    @Operation(summary = "Busca todos os clientes",
                responses = {
                    @ApiResponse(responseCode = "200", description = "Dados do cliente retornados com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "500", description = "Erro interno"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado"),
                }
    )
    public ResponseEntity<Page<ClienteResponse>> clienteFindAll(String nome, String endereco, int page, int pagesize);

    @Operation(summary = "Insere um novo cliente",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cliente inserido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
        }
    )
    public ResponseEntity<ClienteResponse> insert(ClienteRequest request);

    @Operation(summary = "Atualiza um cliente",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
        }
    )
    public ResponseEntity<ClienteResponse> update(ClienteRequest request);

    @Operation(summary = "Exclui um cliente",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cliente excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
        }
    )
    public ResponseEntity<MessageResponse> delete(Long id);

    @Operation(summary = "Busca um cliente pelo id",
        responses = {
            @ApiResponse(responseCode = "200", description = "Dados do cliente retornados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
        }
    )
    public ResponseEntity<ClienteResponse> findById(Long id);
}

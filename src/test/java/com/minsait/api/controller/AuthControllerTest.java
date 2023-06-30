package com.minsait.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.minsait.api.controller.dto.GetTokenRequest;

@DisplayName("Teste endpoints de autenticação")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    private ObjectWriter ow;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @BeforeEach
    public void init(){
        ow = MAPPER.writer().withDefaultPrettyPrinter();
    }

    @Test
    @DisplayName("Deve gerar um token")
    public void getTokenTest() throws Exception {
        final var request = new GetTokenRequest();
        request.setUserName("root");
        request.setPassword("12345");
        mvc.perform(MockMvcRequestBuilders.post("/auth/get-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(request))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Deve retornar não autorizado quando usuário e/ou senha inválido")
    public void getTokenNaoAutorizadoTest() throws Exception {
        final var request = new GetTokenRequest();
        request.setUserName("teste");
        request.setPassword("senha inválida");
        mvc.perform(MockMvcRequestBuilders.post("/auth/get-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(request))
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }

}
package io.ideale.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ideale.auth.dto.CartaoDTO;
import io.ideale.auth.exception.CartaoExceptionHandler;
import io.ideale.auth.exception.CartaoExistenteException;
import io.ideale.auth.exception.CartaoInexistenteException;
import io.ideale.auth.model.Cartao;
import io.ideale.auth.service.CartaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
class CartaoControllerTest {

    static String CARTOES_API = "/cartoes";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartaoService service;

    @Test
    @DisplayName("Criar cartao apresentrando StatusCode 201")
    void criarCartao() throws Exception{
        CartaoDTO cartaoDTO = CartaoDTO.builder().numeroCartao("12345678").senha("12345").build();

        String json = new ObjectMapper().writeValueAsString(cartaoDTO);

        MockHttpServletRequestBuilder req = post(CARTOES_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                ;

        mockMvc
                .perform(req)
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Criar cartao ja existente StatusCode 422")
    void criarCartaoJaExistente() throws Exception{
        CartaoDTO cartaoDTO = CartaoDTO.builder().numeroCartao("12345678").senha("12345").build();
        Cartao cartao = Cartao.builder().numero("12345678").senha("12345").build();

        //BDDMockito.given(service.criarCartao(any(Cartao.class))).
        //willThrow(CartaoExistenteException.class);

        willThrow(CartaoExistenteException
                .builder()
                .cartao(CartaoExceptionHandler
                        .builder()
                        .numeroCartao(cartao.getNumero())
                        .senha(cartao.getSenha()).build())
                .build())
                .given(service).criarCartao(cartao);

        String json = new ObjectMapper().writeValueAsString(cartaoDTO);

        MockHttpServletRequestBuilder req = post(CARTOES_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                ;

        mockMvc
                .perform(req)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Obter saldo cartao")
    void obterSaldo() throws Exception {
        BigDecimal saldo = new BigDecimal(100.00);
        String numeroCartao = "12345678";
        BDDMockito.given(service.obterSaldo(any(String.class))).
                willReturn(saldo);

        String json = new ObjectMapper().writeValueAsString(saldo);

        MockHttpServletRequestBuilder req = get(CARTOES_API + "/" + numeroCartao)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                ;
        req.param("numeroCartao", numeroCartao);

        mockMvc
                .perform(req)
                .andExpect(status().isOk())
                .andExpect(content().string("100"))
        ;
    }

    @Test
    @DisplayName("Obter saldo de cartao inexistente")
    void obterSaldoDeCartaoInexistente() throws Exception {
        BigDecimal saldo = new BigDecimal(100.00);
        String numeroCartao = "12345678";
        BDDMockito.given(service.obterSaldo(any(String.class))).
                willThrow(CartaoInexistenteException.class);

        String json = new ObjectMapper().writeValueAsString(saldo);

        MockHttpServletRequestBuilder req = get(CARTOES_API + "/" + numeroCartao)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                ;
        req.param("numeroCartao", numeroCartao);

        mockMvc
                .perform(req)
                .andExpect(status().isNotFound())
        ;
    }
}
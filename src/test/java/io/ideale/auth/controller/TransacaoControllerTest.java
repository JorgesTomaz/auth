package io.ideale.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ideale.auth.dto.TransacaoDTO;
import io.ideale.auth.exception.CartaoInexistenteException;
import io.ideale.auth.exception.SaldoInsuficienteException;
import io.ideale.auth.exception.SenhaIvalidaException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
class TransacaoControllerTest {

    static String TRANSACOES_API = "/transacoes";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartaoService service;

    @Test
    @DisplayName("Realizar transacao de debito")
    void realizarDebito() throws Exception{
        TransacaoDTO transacaoDTO = TransacaoDTO.builder().numeroCartao("12345678").senhaCartao("12345").valor(BigDecimal.valueOf(500L)).build();
        Cartao cartao = Cartao.builder().numero("12345678").senha("12345").valor(BigDecimal.valueOf(500L)).build();

        BDDMockito.given(service.debito(any(Cartao.class))).willReturn(cartao);

        String json = new ObjectMapper().writeValueAsString(transacaoDTO);

        MockHttpServletRequestBuilder req = post(TRANSACOES_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                ;

        mockMvc
                .perform(req)
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Realizar transacao de debito com saldo insuficiente")
    void realizarDebitoComSaldoInsuficiente() throws Exception{
        TransacaoDTO transacaoDTO = TransacaoDTO.builder().numeroCartao("12345678").senhaCartao("12345").valor(BigDecimal.valueOf(500L)).build();
        Cartao cartao = Cartao.builder().numero("12345678").senha("12345").valor(BigDecimal.valueOf(500L)).build();

        BDDMockito.given(service.debito(any(Cartao.class))).willThrow(SaldoInsuficienteException.class);

        String json = new ObjectMapper().writeValueAsString(transacaoDTO);

        MockHttpServletRequestBuilder req = post(TRANSACOES_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                ;

        mockMvc
                .perform(req)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("SALDO_INSUFICIENTE"))
        ;
    }

    @Test
    @DisplayName("Realizar transacao de debito com senha invalida")
    void realizarDebitoComSenhaInvalida() throws Exception{
        TransacaoDTO transacaoDTO = TransacaoDTO.builder().numeroCartao("12345678").senhaCartao("12345").valor(BigDecimal.valueOf(500L)).build();
        Cartao cartao = Cartao.builder().numero("12345678").senha("12345").valor(BigDecimal.valueOf(500L)).build();

        BDDMockito.given(service.debito(any(Cartao.class))).willThrow(SenhaIvalidaException.class);

        String json = new ObjectMapper().writeValueAsString(transacaoDTO);

        MockHttpServletRequestBuilder req = post(TRANSACOES_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                ;

        mockMvc
                .perform(req)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("SENHA_INVALIDA"))
        ;
    }


    @Test
    @DisplayName("Realizar transacao de debito com cartao inexistente")
    void realizarDebitoComCartaoInexistente() throws Exception{
        TransacaoDTO transacaoDTO = TransacaoDTO.builder().numeroCartao("12345678").senhaCartao("12345").valor(BigDecimal.valueOf(500L)).build();
        Cartao cartao = Cartao.builder().numero("12345678").senha("12345").valor(BigDecimal.valueOf(500L)).build();

        BDDMockito.given(service.debito(any(Cartao.class))).willThrow(CartaoInexistenteException.class);

        String json = new ObjectMapper().writeValueAsString(transacaoDTO);

        MockHttpServletRequestBuilder req = post(TRANSACOES_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                ;

        mockMvc
                .perform(req)
                .andExpect(status().isNotFound())
                .andExpect(content().string("CARTAO_INEXISTENTE"))

        ;
    }
}

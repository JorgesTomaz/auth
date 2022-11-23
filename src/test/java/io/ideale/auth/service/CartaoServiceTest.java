package io.ideale.auth.service;

import io.ideale.auth.exception.CartaoInexistenteException;
import io.ideale.auth.exception.SaldoInsuficienteException;
import io.ideale.auth.exception.SenhaIvalidaException;
import io.ideale.auth.model.Cartao;
import io.ideale.auth.repository.CartaoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class CartaoServiceTest {

    CartaoService service;

    CartaoRepository repository;

    @BeforeEach
    public void init() {
        this.repository = mock(CartaoRepository.class);
    }

    @Test
    @DisplayName("criar cartao")
    void criarCartao(){
        Cartao cartao = Cartao.builder().numero("123456789").senha("123456").build();

        when(repository.criarNovo(cartao)).thenReturn(Cartao.builder().id(1L).numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build());

        service = CartaoServiceImpl.builder().repository(repository).build();

        Cartao cartaoCriado = service.criarCartao(cartao);

        assertThat(cartaoCriado.getValor()).isPositive();

    }

    @Test
    @DisplayName("obter saldo cartao")
    void obterSaldoCartao(){

        String numeroCartao = "123456789";

        when(repository.obterSaldo(numeroCartao)).thenReturn(BigDecimal.valueOf(1000L));

        service = CartaoServiceImpl.builder().repository(repository).build();

        assertThat(service.obterSaldo(numeroCartao)).isPositive();
    }

    @Test
    @DisplayName("obter saldo cartao inexistente")
    void obterSaldoCartaoInexistente(){

        String numeroCartao = "123456789";

        when(repository.obterSaldo(numeroCartao)).thenThrow(CartaoInexistenteException.class);

        service = CartaoServiceImpl.builder().repository(repository).build();

        Throwable throwable = Assertions.catchThrowable(()-> service.obterSaldo(numeroCartao));
        assertTrue(throwable instanceof CartaoInexistenteException);
    }

    @Test
    @DisplayName("debitar saldo")
    void debitarSaldo(){

        Cartao cartao = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(900L)).build();

        Cartao cartaoConsultado = Cartao.builder().id(1L).numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();

        when(repository.consultaCartao(cartao.getNumero())).thenReturn(cartaoConsultado);

        repository.validarSenha(cartao);

        when(repository.debito(cartao)).thenReturn(cartao);

        service = CartaoServiceImpl.builder().repository(repository).build();

        Cartao cartaoDebitado = service.debito(cartao);

        assertThat(cartaoDebitado.getValor()).isPositive();
        assertEquals(1L, cartaoDebitado.getId().longValue());
        assertEquals(100L, cartaoDebitado.getValor().doubleValue());
    }

    @Test
    @DisplayName("debitar saldo insuficiente")
    void debitarSaldoInsuficiente(){

        Cartao cartao = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(900L)).build();

        Cartao cartaoConsultado = Cartao.builder().id(1L).numero("123456789").senha("123456").valor(BigDecimal.valueOf(500L)).build();

        when(repository.consultaCartao(cartao.getNumero())).thenReturn(cartaoConsultado);

        repository.validarSenha(cartao);

        when(repository.debito(cartao)).thenThrow(SaldoInsuficienteException.class);

        service = CartaoServiceImpl.builder().repository(repository).build();

        Throwable throwable = Assertions.catchThrowable(()-> service.debito(cartao));
        assertThat(cartao.getValor()).isNegative();
        assertTrue(throwable instanceof SaldoInsuficienteException);

    }

    @Test
    @DisplayName("debitar saldo senha invalida")
    void debitarSaldoSenhaInvalida(){

        Cartao cartao = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(900L)).build();

        Cartao cartaoConsultado = Cartao.builder().id(1L).numero("123456789").senha("123456").valor(BigDecimal.valueOf(500L)).build();

        when(repository.consultaCartao(cartao.getNumero())).thenReturn(cartaoConsultado);

        Mockito.doThrow(SenhaIvalidaException.builder().build()).when(repository).validarSenha(cartao);

        service = CartaoServiceImpl.builder().repository(repository).build();

        Throwable throwable = Assertions.catchThrowable(()-> service.debito(cartao));
        assertTrue(throwable instanceof SenhaIvalidaException);
    }
}

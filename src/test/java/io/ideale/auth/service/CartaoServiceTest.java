package io.ideale.auth.service;

import io.ideale.auth.exception.BuscarSaldoCartaoInexistenteException;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;

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

        service = CartaoServiceImpl.builder().repository(repository).build();

        service.criarCartao(cartao);

        assertThat(cartao.getValor()).isPositive();

    }

    @Test
    @DisplayName("obter saldo cartao")
    void obterSaldoCartao(){

        Cartao cartaoP = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000)).build();

        String numeroCartao = "123456789";

        when(repository.findById(numeroCartao)).thenReturn(Optional.of(cartaoP));

        service = CartaoServiceImpl.builder().repository(repository).build();

        assertThat(service.obterSaldo(numeroCartao)).isPositive();
    }

    @Test
    @DisplayName("obter saldo cartao inexistente")
    void obterSaldoCartaoInexistente(){

        Cartao cartaoP = null;

        String numeroCartao = "123456789";

        Optional<Cartao> op = Optional.ofNullable(cartaoP);

        when(repository.findById(numeroCartao)).thenReturn(op);

        service = CartaoServiceImpl.builder().repository(repository).build();

        Throwable throwable = Assertions.catchThrowable(()-> service.obterSaldo(numeroCartao));
        assertTrue(throwable instanceof BuscarSaldoCartaoInexistenteException);
    }

    @Test
    @DisplayName("debitar saldo")
    void debitarSaldo(){
        Cartao cartaoC = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(900.00)).build();

        Cartao cartaoP = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000.00)).build();

        when(repository.findById(cartaoC.getNumero())).thenReturn(Optional.of(cartaoP));

        when(repository.findByNumeroAndSenha(cartaoC.getNumero(), cartaoC.getSenha()))
                .thenReturn(cartaoP);

        when(repository.debito(cartaoP)).thenReturn(cartaoP);

        service = CartaoServiceImpl.builder().repository(repository).build();

        Cartao cartaoDebitado = service.debito(cartaoC);

        assertThat(cartaoDebitado.getValor()).isPositive();
        //assertEquals(1L, cartaoDebitado.getId().longValue());
        assertEquals(100.00, cartaoDebitado.getValor().doubleValue());
    }

    @Test
    @DisplayName("debitar saldo insuficiente")
    void debitarSaldoInsuficiente(){

        Cartao cartaoC = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(900.00)).build();

        Cartao cartaoP = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(800.00)).build();

        when(repository.findById(cartaoC.getNumero())).thenReturn(Optional.of(cartaoP));

        when(repository.findByNumeroAndSenha(cartaoC.getNumero(), cartaoC.getSenha()))
                .thenReturn(cartaoP);

        when(repository.debito(cartaoP)).thenReturn(cartaoP);

        service = CartaoServiceImpl.builder().repository(repository).build();

        Throwable throwable = Assertions.catchThrowable(()-> service.debito(cartaoC));

        assertTrue(throwable instanceof SaldoInsuficienteException);

        assertThat(cartaoP.getValor()).isNegative();

    }

    @Test
    @DisplayName("debitar saldo senha invalida")
    void debitarSaldoSenhaInvalida(){

        Cartao cartaoC = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(900.00)).build();

        Cartao cartaoP = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(800.00)).build();

        Cartao cartao = null;

        when(repository.findById(cartaoC.getNumero())).thenReturn(Optional.of(cartaoP));

        when(repository.findByNumeroAndSenha(cartaoC.getNumero(), cartaoC.getSenha())).thenReturn(cartao);

        service = CartaoServiceImpl.builder().repository(repository).build();

        Throwable throwable = Assertions.catchThrowable(()-> service.debito(cartaoC));
        assertTrue(throwable instanceof SenhaIvalidaException);
    }
}

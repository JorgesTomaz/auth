package io.ideale.auth.repository;

import io.ideale.auth.exception.CartaoInexistenteException;
import io.ideale.auth.exception.SenhaIvalidaException;
import io.ideale.auth.model.Cartao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CartaoRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CartaoRepository repository;

    @Test
    @DisplayName("criar novo cartao no banco")
    void criarCartao() {

        Cartao cartao = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();

        Cartao cartaoCriado = repository.criarNovo(cartao);

        assertThat(cartaoCriado.getId()).isNotZero();
    }

    @Test
    @DisplayName("criar cartao ja existente no banco")
    void criarCartaoJaExistente() {

        Cartao cartao = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();

        repository.criarNovo(cartao);

        Cartao cartaoExistente = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();

        Throwable throwable = Assertions.catchThrowable(()-> repository.criarNovo(cartaoExistente));

        assertTrue(throwable instanceof DataIntegrityViolationException);

    }

    @Test
    @DisplayName("consultar Cartao")
    void consultaCartao(){
        Cartao cartao = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();

        repository.criarNovo(cartao);

        Cartao cartaoConsultado = repository.consultaCartao(cartao.getNumero());

        assertThat(cartaoConsultado.getId()).isNotZero();

    }

    @Test
    @DisplayName("consultar Cartao Nao Existente")
    void consultaCartaoNaoExistente(){
        Cartao cartao = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();

        repository.criarNovo(cartao);

        Throwable throwable = Assertions.catchThrowable(()-> repository.consultaCartao("123"));

        assertTrue(throwable instanceof CartaoInexistenteException);

    }

    @Test
    @DisplayName("obter saldo cartao")
    void obterSaldo(){
        Cartao cartao = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();

        repository.criarNovo(cartao);

        BigDecimal saldo = repository.obterSaldo(cartao.getNumero());

        assertEquals(cartao.getValor().doubleValue(), saldo.doubleValue());
    }

    @Test
    @DisplayName("obter saldo de cartao nao existente")
    void obterSaldoCartaoNaoExistente(){
        Cartao cartao = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();

        repository.criarNovo(cartao);

        Throwable throwable = Assertions.catchThrowable(()-> repository.obterSaldo("123"));

        assertTrue(throwable instanceof EmptyResultDataAccessException);
    }

    @Test
    @DisplayName("validar senha cartao")
    void validarSenhaCartao(){
        Cartao cartao = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();

        repository.criarNovo(cartao);

        Cartao cartaoConsultado = repository.validarSenha(cartao);

        assertThat(cartaoConsultado.getId()).isNotZero();
    }

    @Test
    @DisplayName("validar senha cartao invalida")
    void validarSenhaInvalida(){
        Cartao cartao = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();

        repository.criarNovo(cartao);

        Throwable throwable = Assertions.catchThrowable(()->
                repository.validarSenha(
                        Cartao.builder().numero("123456789").senha("123").build()));

        assertTrue(throwable instanceof SenhaIvalidaException);
    }

    @Test
    @DisplayName("debitar saldo")
    void debitarSaldo(){
        Cartao cartao = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();
        Cartao cartaoDebito = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(900)).build();

        repository.criarNovo(cartao);

        Cartao cartaoDebitado = repository.debito(cartaoDebito);

        BigDecimal saldo = repository.obterSaldo(cartaoDebito.getNumero());

        assertThat(saldo).isPositive();
        assertEquals(900L, saldo.longValue());

    }
}

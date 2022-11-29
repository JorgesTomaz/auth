package io.ideale.auth.repository;

import io.ideale.auth.exception.SenhaIvalidaException;
import io.ideale.auth.model.Cartao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

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

        Cartao cartao = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000.00)).build();

        repository.criarNovo(cartao);

        Cartao cartaoP = repository.findByNumeroAndSenha(cartao.getNumero(), cartao.getSenha());
        assertThat(cartaoP).extracting(Cartao::getValor).isEqualTo(BigDecimal.valueOf(1000.00));

    }

    @Test
    @DisplayName("criar cartao ja existente no banco")
    void criarCartaoJaExistente() {

        Cartao cartao = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();

        repository.criarNovo(cartao);

        Cartao cartaoExistente = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();

        Throwable throwable = Assertions.catchThrowable(()-> repository.criarNovo(cartaoExistente));

        assertTrue(throwable instanceof RuntimeException);

    }

    @Test
    @DisplayName("consultar Cartao")
    void consultaCartao(){
        //Cartao cartaoC = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(900.00)).build();

        repository.criarNovo(Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(900.00)).build());

        String numeroCartao = "123456789";
        String senha = "123456";
        BigDecimal valor = BigDecimal.valueOf(900.00);

        Optional<Cartao> op = repository.findById(numeroCartao);

        Cartao cartao = op.get();
        assertEquals(cartao.getNumero(), numeroCartao);
        assertThat(cartao.getSenha()).isEqualTo(senha);
        assertThat(cartao.getValor()).isEqualTo(valor);


    }

    @Test
    @DisplayName("consultar Cartao Nao Existente")
    void consultaCartaoNaoExistente(){
        Cartao cartaoC = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(900.00)).build();

        repository.criarNovo(cartaoC);

        String numeroCartao = "12345678";

        Optional<Cartao> op = repository.findById(numeroCartao);

        assertTrue(op.isEmpty());

    }

    @Test
    @DisplayName("obter saldo cartao")
    void obterSaldo(){
        Cartao cartao = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();

        repository.criarNovo(cartao);

        BigDecimal saldo = repository.findById(cartao.getNumero()).get().getValor();

        assertEquals(cartao.getValor().doubleValue(), saldo.doubleValue());
    }

    @Test
    @DisplayName("validar senha cartao")
    void validarSenhaCartao(){
        Cartao cartaoC = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();

        Cartao cartaoP = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();

        repository.criarNovo(cartaoC);

        Cartao cartaoConsultado = repository.findByNumeroAndSenha(cartaoP.getNumero(), cartaoP.getSenha());

        assertThat(cartaoConsultado.getNumero()).isNotEmpty();


    }

    @Test
    @DisplayName("validar senha cartao invalida")
    void validarSenhaInvalida(){
        Cartao cartaoC = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();

        repository.criarNovo(cartaoC);

        Cartao cartaoP = Cartao.builder().numero("123456789").senha("12345").valor(BigDecimal.valueOf(1000L)).build();
        Optional<Cartao> op = Optional.ofNullable(repository.findByNumeroAndSenha(cartaoP.getNumero(), cartaoP.getSenha()));
        Throwable throwable = Assertions.catchThrowable(()->


        op.ifPresentOrElse(val->{},
                ()->{throw SenhaIvalidaException.builder().build();})
        );

        assertTrue(throwable instanceof SenhaIvalidaException);
    }

    @Test
    @DisplayName("debitar saldo")
    void debitarSaldo(){
        Cartao cartaoC = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(1000L)).build();

        Cartao cartaoD = Cartao.builder().numero("123456789").senha("123456").valor(BigDecimal.valueOf(900)).build();

        repository.criarNovo(cartaoC);

        Cartao cartaoDebitado = repository.debito(cartaoD);

        assertEquals(900.00, cartaoDebitado.getValor().doubleValue());

    }
}

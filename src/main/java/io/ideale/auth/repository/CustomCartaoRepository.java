package io.ideale.auth.repository;

import io.ideale.auth.exception.CartaoInexistenteException;
import io.ideale.auth.exception.SaldoInsuficienteException;
import io.ideale.auth.exception.SenhaIvalidaException;
import io.ideale.auth.model.Cartao;

import java.math.BigDecimal;

public interface CustomCartaoRepository {

    Cartao criarNovo(Cartao cartao);
    Cartao consultaCartao(String numero) throws CartaoInexistenteException;

    BigDecimal obterSaldo(String numeroCartao);

    Cartao debito(Cartao cartao) throws SaldoInsuficienteException;

    void validarSenha(Cartao cartao) throws SenhaIvalidaException;
}

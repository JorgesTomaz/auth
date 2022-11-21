package io.ideale.auth.repository;

import io.ideale.auth.model.Cartao;

import java.math.BigDecimal;
import java.util.Optional;

public interface CustomCartaoRepository {

    Cartao criarNovo(Cartao cartao);
    Cartao consultaCartao(String numero);

    BigDecimal obterSaldo(String numeroCartao);
}

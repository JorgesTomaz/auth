package io.ideale.auth.service;

import io.ideale.auth.model.Cartao;

import java.math.BigDecimal;


public interface CartaoService {
    void criarCartao(Cartao cartao);

    BigDecimal obterSaldo(String numeroCartao);

    Cartao debito(Cartao cartao);
}

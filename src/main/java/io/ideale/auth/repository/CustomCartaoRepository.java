package io.ideale.auth.repository;

import io.ideale.auth.model.Cartao;

public interface CustomCartaoRepository {

    void criarNovo(Cartao cartao);
    Cartao debito(Cartao cartao);
}

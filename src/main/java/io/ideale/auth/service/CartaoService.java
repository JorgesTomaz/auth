package io.ideale.auth.service;

import io.ideale.auth.exception.CartaoExistenteException;
import io.ideale.auth.model.Cartao;
import io.ideale.auth.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public interface CartaoService {
    Cartao criarCartao(Cartao cartao) throws Exception;

    BigDecimal obterSaldo(String numeroCartao);
}

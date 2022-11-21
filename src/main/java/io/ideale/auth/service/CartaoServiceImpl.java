package io.ideale.auth.service;

import io.ideale.auth.exception.CartaoExceptionHandler;
import io.ideale.auth.exception.CartaoExistenteException;
import io.ideale.auth.model.Cartao;
import io.ideale.auth.repository.CartaoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Service
public class CartaoServiceImpl implements CartaoService{

    @Autowired
    private CartaoRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Cartao criarCartao(Cartao cartao) throws Exception {


            Cartao cartaoRetornado = repository.consultaCartao(cartao.getNumero());

            if (cartaoRetornado != null) {
                throw CartaoExistenteException.builder()
                        .cartao(modelMapper.map(cartao, CartaoExceptionHandler.class))
                        .build();
            }
            cartao.setValor(new BigDecimal(0.00));

            return repository.criarNovo(cartao);
    }

    @Override
    public BigDecimal obterSaldo(String numeroCartao) {
        return repository.obterSaldo(numeroCartao);
    }
}

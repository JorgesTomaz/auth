package io.ideale.auth.service;

import io.ideale.auth.exception.*;
import io.ideale.auth.model.Cartao;
import io.ideale.auth.repository.CartaoRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
@Builder
public class CartaoServiceImpl implements CartaoService{

    @Autowired
    private CartaoRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Cartao criarCartao(Cartao cartao) {
        try {
            cartao.setValor(BigDecimal.valueOf(1000.00));
            return repository.criarNovo(cartao);
        } catch (CartaoExistenteException e) {
            throw CartaoExistenteException.builder()
                    .cartao(modelMapper.map(cartao, CartaoExceptionHandler.class))
                    .build();
        }
    }

    @Override
    public BigDecimal obterSaldo(String numeroCartao) {
        try {
            return repository.obterSaldo(numeroCartao);
        } catch(Exception e) {
            throw CartaoInexistenteException
                    .builder()
                    .cartao(
                            CartaoExceptionHandler.builder().numero(numeroCartao).build()
                    ).build();
        }

    }

    @Override
    public Cartao debito(Cartao cartao) {

        try {
            Cartao cartaoT = repository.consultaCartao(cartao.getNumero());

            repository.validarSenha(cartao);

            cartao.setId(cartaoT.getId());
            cartao.setValor(cartaoT.getValor().subtract(cartao.getValor()).setScale(2, RoundingMode.HALF_UP));

            cartao = repository.debito(cartao);

            return cartao;

        } catch (SenhaIvalidaException e) {
            throw SenhaIvalidaException.builder().build();
        } catch (SaldoInsuficienteException e) {
            throw SaldoInsuficienteException.builder().build();
        } catch (CartaoInexistenteException e) {
            throw CartaoInexistenteException.builder()
                    .cartao(modelMapper.map(cartao, CartaoExceptionHandler.class))
                    .build();
        } catch (Exception e) {
            throw DadosCartaoInvalidosException.builder().build();
        }

    }
}

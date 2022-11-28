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
import java.util.Optional;

@Service
@AllArgsConstructor
@Builder
public class CartaoServiceImpl implements CartaoService {

    @Autowired
    private CartaoRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Cartao cartaoTransacional;

    @Override
    public void criarCartao(Cartao cartao) {
        try {
            cartao.setValor(BigDecimal.valueOf(1000.00));
            repository.criarNovo(cartao);
        } catch (Exception e) {
            throw CartaoExistenteException.builder()
                    .cartao(modelMapper.map(cartao, CartaoExceptionHandler.class))
                    .build();
        }

    }

    @Override
    public BigDecimal obterSaldo(String numeroCartao) {

        try {
            Optional<Cartao> op = repository.findById(numeroCartao);
            op.ifPresentOrElse(
                    value -> cartaoTransacional = value,
                    ()-> {
                        throw BuscarSaldoCartaoInexistenteException.builder().build();
                    }
            );
            return cartaoTransacional.getValor();
        } catch (BuscarSaldoCartaoInexistenteException e) {
            throw BuscarSaldoCartaoInexistenteException.builder().build();
        }

    }

    @Override
    public Cartao debito(Cartao cartao) {

        try {

            repository.findById(cartao.getNumero())
                .ifPresentOrElse(value -> cartaoTransacional = value,
                            ()->{throw CartaoInexistenteException.builder().build();});

            Optional<Cartao> op = Optional.ofNullable(repository.findByNumeroAndSenha(cartao.getNumero(), cartao.getSenha()));

            op.ifPresentOrElse(value -> cartaoTransacional = value,
                            ()->{throw SenhaIvalidaException.builder().build();});

            cartaoTransacional.setValor(cartaoTransacional.getValor().subtract(cartao.getValor()).setScale(2, RoundingMode.HALF_UP));

            Optional.of(cartaoTransacional.getValor().signum() == -1)
                    .filter(Boolean::booleanValue)
                    .map(bool -> {throw SaldoInsuficienteException.builder().build();});

            repository.debito(cartaoTransacional);

            return cartaoTransacional;

        } catch (SenhaIvalidaException e) {
            throw SenhaIvalidaException.builder().build();
        } catch (SaldoInsuficienteException e) {
            throw SaldoInsuficienteException.builder().build();
        } catch (CartaoInexistenteException e) {
            throw CartaoInexistenteException.builder().build();
        } catch (Exception e) {
            throw DadosCartaoInvalidosException.builder().build();
        }
    }

}

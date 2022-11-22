package io.ideale.auth.service;

import io.ideale.auth.exception.*;
import io.ideale.auth.model.Cartao;
import io.ideale.auth.repository.CartaoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Service
public class CartaoServiceImpl implements CartaoService{

    @Autowired
    private CartaoRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Cartao criarCartao(Cartao cartao) {
        try {
            cartao.setValor(new BigDecimal(1000.00));
            return repository.criarNovo(cartao);
        } catch (DataIntegrityViolationException e) {
            throw CartaoExistenteException.builder()
                    .cartao(modelMapper.map(cartao, CartaoExceptionHandler.class))
                    .build();
        }catch (Exception e) {
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

    // TODO melhorear essa merda
    @Override
    public Cartao debito(Cartao cartao) {

        try {
            Cartao cartaoT = repository.consultaCartao(cartao.getNumero());

            repository.validarSenha(cartao);

            cartaoT.setValor(cartaoT.getValor().subtract(cartao.getValor()).setScale(2, RoundingMode.HALF_UP));

            return repository.debito(cartaoT);

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

package io.ideale.auth.repository;

import io.ideale.auth.exception.CartaoExceptionHandler;
import io.ideale.auth.exception.CartaoInvalidoException;
import io.ideale.auth.exception.DadosCartaoInvalidosException;
import io.ideale.auth.model.Cartao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Transactional
public class CustomCartaoRepositoryImpl implements CustomCartaoRepository{

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Cartao criarNovo(Cartao cartao) throws DadosCartaoInvalidosException {
        try {

            Cartao cartaoResposta = entityManager.merge(cartao);

            return cartaoResposta;
        } catch(Exception e) {

            throw DadosCartaoInvalidosException.builder().cartao(modelMapper.map(cartao, CartaoExceptionHandler.class)).build();
        }

    }

    @Override
    public Cartao consultaCartao(String numero) {
        Cartao cartao = null;

        try {
             cartao = (Cartao) entityManager
                    .createNamedQuery("consultarCartaoExistente")
                    .setParameter("numero", numero)
                    .getSingleResult();
        } catch(Exception e) {
            return null;
        }
        return cartao;
    }

    @Override
    public BigDecimal obterSaldo(String numeroCartao) {
        BigDecimal saldo = null;

        try {
            saldo = (BigDecimal) entityManager
                    .createNamedQuery("consultarSaldo")
                    .setParameter("numero", numeroCartao)
                    .getSingleResult();
        } catch(Exception e) {
            throw CartaoInvalidoException
                    .builder()
                    .cartao(
                            CartaoExceptionHandler.builder().numero(numeroCartao).build()
                    ).build();
        }
        return saldo;
    }
}

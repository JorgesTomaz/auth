package io.ideale.auth.repository;

import io.ideale.auth.exception.*;
import io.ideale.auth.model.Cartao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;


@Transactional
public class CustomCartaoRepositoryImpl implements CustomCartaoRepository{

    public static final String NUMERO = "numero";
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Cartao criarNovo(Cartao cartao) {
        try {
            return entityManager.merge(cartao);
        } catch(Exception e) {
            throw CartaoExistenteException.builder()
                    .cartao(modelMapper.map(cartao, CartaoExceptionHandler.class))
                    .build();
        }
    }

    @Override
    public Cartao consultaCartao(String numero) throws CartaoInexistenteException{
        Cartao cartao = null;
        try {
            cartao = (Cartao) entityManager
                    .createNamedQuery("consultarCartaoExistente")
                    .setParameter(NUMERO, numero)
                    .getSingleResult();
        }catch (Exception e) {
            throw CartaoInexistenteException.builder().build();
        }
        return cartao;
    }

    @Override
    public BigDecimal obterSaldo(String numeroCartao) {
        BigDecimal saldo = null;
        saldo = (BigDecimal) entityManager
                .createNamedQuery("consultarSaldo")
                .setParameter(NUMERO, numeroCartao)
                .getSingleResult();
        return saldo;
    }

    @Override
    public Cartao debito(Cartao cartao) {

        try{
           entityManager
                .createQuery("update Cartao c set c.valor = :valor WHERE c.numero = :numero and c.id= :id")
                .setParameter("valor", cartao.getValor())
                .setParameter(NUMERO, cartao.getNumero())
                .setParameter("id", cartao.getId())
                .executeUpdate();
        } catch(ConstraintViolationException e) {
            throw SaldoInsuficienteException.builder().build();
        } catch (Exception e) {
            throw e;
        }
        return cartao;
    }

    @Override
    public Cartao validarSenha(Cartao cartao) throws SenhaIvalidaException {

        try {
            return (Cartao) entityManager
                    .createNamedQuery("validarSenha")
                    .setParameter(NUMERO, cartao.getNumero())
                    .setParameter("senha", cartao.getSenha())
                    .getSingleResult();
        } catch(NoResultException e) {
            throw SenhaIvalidaException.builder().build();
        }

    }
}

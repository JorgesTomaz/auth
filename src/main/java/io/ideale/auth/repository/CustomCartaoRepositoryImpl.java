package io.ideale.auth.repository;

import io.ideale.auth.exception.CartaoInexistenteException;
import io.ideale.auth.exception.SaldoInsuficienteException;
import io.ideale.auth.exception.SenhaIvalidaException;
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
        return entityManager.merge(cartao);
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
                .createQuery("update Cartao c set c.valor = :valor WHERE c.numero = :numero")
                .setParameter("valor", cartao.getValor())
                .setParameter(NUMERO, cartao.getNumero())
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

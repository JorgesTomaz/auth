package io.ideale.auth.repository;

import io.ideale.auth.model.Cartao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;


@Transactional
public class CustomCartaoRepositoryImpl implements CustomCartaoRepository{

    public static final String NUMERO = "numero";
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void criarNovo(Cartao cartao) {
        entityManager.persist(cartao);
    }

    @Override
    public Cartao debito(Cartao cartao) {
        return entityManager.merge(cartao);
    }
}

package io.ideale.auth.repository;

import io.ideale.auth.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, String>, CustomCartaoRepository {
    Cartao findByNumeroAndSenha(String numero, String senha);
}


package io.ideale.auth.repository;

import io.ideale.auth.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaoRepository extends CrudRepository<Cartao, Long>, CustomCartaoRepository {
}


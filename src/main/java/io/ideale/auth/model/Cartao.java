package io.ideale.auth.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
@Entity

@NamedQuery(
        name = "consultarCartaoExistente",
        query = "SELECT c FROM Cartao c WHERE c.numero = :numero"
)
@NamedQuery(
        name = "consultarSaldo",
        query = "SELECT c.valor FROM Cartao c WHERE c.numero = :numero"
)
@NamedQuery(
        name = "debitarSaldo",
                query = "update Cartao c set c.valor = :valor WHERE c.numero = :numero"
        )
@NamedQuery(
        name = "validarSenha",
        query = "SELECT c FROM Cartao c WHERE c.numero = :numero and c.senha = :senha"
)


public class Cartao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String numero;

    @Column(nullable = false)
    private String senha;

    @Column
    @Positive
    private BigDecimal valor;
}

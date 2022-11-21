package io.ideale.auth.model;

import lombok.*;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
@Entity
@NamedQueries({
        @NamedQuery(
                name = "consultarCartaoExistente",
                query = "SELECT c FROM Cartao c WHERE c.numero = :numero"
        ),
        @NamedQuery(
                name = "consultarSaldo",
                query = "SELECT c.valor FROM Cartao c WHERE c.numero = :numero"
        )
})
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String numero;

    @Column(nullable = false)
    private String senha;

    @Column
    private BigDecimal valor;
}

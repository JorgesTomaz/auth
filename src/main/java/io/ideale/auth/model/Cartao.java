package io.ideale.auth.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
@Entity
@Component
public class Cartao implements Serializable {

    @Id
    @Column(unique = true, nullable = false)
    private String numero;

    @Column(nullable = false)
    private String senha;

    @Column
    @PositiveOrZero(message = "SALDO_INSUFICIENTE")
    private BigDecimal valor;

}

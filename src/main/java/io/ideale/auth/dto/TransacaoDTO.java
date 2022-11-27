package io.ideale.auth.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
public class TransacaoDTO {

    @NotNull
    @NotEmpty
    private String numeroCartao;
    @NotNull
    @NotEmpty
    private String senhaCartao;
    @NotNull
    private BigDecimal valor;
}

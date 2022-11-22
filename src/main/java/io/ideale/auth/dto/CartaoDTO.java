package io.ideale.auth.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
public class CartaoDTO {
    @NotNull(message = "Informar numero cartao")
    @NotEmpty(message = "Informar numero cartao")
    private String numero;
    @NotNull(message = "Informar senha cartao")
    @NotEmpty(message = "Informar senha cartao")
    private String senha;
}

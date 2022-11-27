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
    @NotNull(message = "")
    @NotEmpty(message = "")
    private String numeroCartao;
    @NotNull(message = "")
    @NotEmpty(message = "")
    private String senha;
}

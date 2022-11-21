package io.ideale.auth.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
public class CartaoDTO {
    private String numero;
    private String senha;
}

package io.ideale.auth.exception;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class CartaoExceptionHandler implements Serializable {
    private String senha;
    private String numeroCartao;
}

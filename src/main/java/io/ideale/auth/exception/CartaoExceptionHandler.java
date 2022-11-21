package io.ideale.auth.exception;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class CartaoExceptionHandler {
    private String numero;
    private String senha;
}

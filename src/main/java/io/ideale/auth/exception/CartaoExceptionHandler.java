package io.ideale.auth.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class CartaoExceptionHandler implements Serializable {
    private String numeroCartao;
    private String senha;
    private String tipo;
    private HttpStatus status;
}

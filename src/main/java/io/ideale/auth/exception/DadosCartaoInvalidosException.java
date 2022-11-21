package io.ideale.auth.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class DadosCartaoInvalidosException extends RuntimeException {
    private CartaoExceptionHandler cartao;
}

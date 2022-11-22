package io.ideale.auth.exception;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CartaoInexistenteException extends RuntimeException {
    private final transient CartaoExceptionHandler cartao;
}

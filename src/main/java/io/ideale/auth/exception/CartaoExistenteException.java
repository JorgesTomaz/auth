package io.ideale.auth.exception;


import io.ideale.auth.model.Cartao;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CartaoExistenteException extends RuntimeException {
    private CartaoExceptionHandler cartao;
}

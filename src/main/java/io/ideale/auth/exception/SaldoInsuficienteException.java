package io.ideale.auth.exception;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SaldoInsuficienteException extends RuntimeException {
}

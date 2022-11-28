package io.ideale.auth.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackages = "io.ideale.auth.controller")
public class CartaoControllerAdvice {

    public static final String SALDO_INSUFICIENTE = "SALDO_INSUFICIENTE";
    public static final String SENHA_INVALIDA = "SENHA_INVALIDA";
    public static final String CARTAO_INEXISTENTE = "CARTAO_INEXISTENTE";

    @ResponseBody
    @ExceptionHandler(CartaoExistenteException.class)
    public ResponseEntity<CartaoExceptionHandler> cartaoExistente(CartaoExistenteException exception) {
        return new ResponseEntity<>(exception.getCartao(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ResponseBody
    @ExceptionHandler(DadosCartaoInvalidosException.class)
    public ResponseEntity<CartaoExceptionHandler> dadosInvalidos(DadosCartaoInvalidosException exception) {
        return new ResponseEntity<>(exception.getCartao(), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(BuscarSaldoCartaoInexistenteException.class)
    public ResponseEntity<String> buscarSaldoCartaoInvalido(BuscarSaldoCartaoInexistenteException exception) {
        return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(CartaoInexistenteException.class)
    public ResponseEntity<String> cartaoInvalido(CartaoInexistenteException exception) {
        return new ResponseEntity<>(CARTAO_INEXISTENTE, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ResponseBody
    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<String> saldoInsuficiente(SaldoInsuficienteException exception) {
        return new ResponseEntity<>(SALDO_INSUFICIENTE, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ResponseBody
    @ExceptionHandler(SenhaIvalidaException.class)
    public ResponseEntity<String> cartaoInvalido(SenhaIvalidaException exception) {
        return new ResponseEntity<>(SENHA_INVALIDA, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}

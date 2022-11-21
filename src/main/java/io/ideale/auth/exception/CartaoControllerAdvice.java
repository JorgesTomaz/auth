package io.ideale.auth.exception;


import io.ideale.auth.dto.CartaoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackages = "io.ideale.auth.controller")
public class CartaoControllerAdvice {

    @ResponseBody
    @ExceptionHandler(CartaoExistenteException.class)
    public ResponseEntity<CartaoExceptionHandler> cartaoExistente(CartaoExistenteException exception) {
        return new ResponseEntity<CartaoExceptionHandler>(exception.getCartao(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ResponseBody
    @ExceptionHandler(DadosCartaoInvalidosException.class)
    public ResponseEntity<CartaoExceptionHandler> dadosInvalidos(DadosCartaoInvalidosException exception) {
        return new ResponseEntity<CartaoExceptionHandler>(exception.getCartao(), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(CartaoInvalidoException.class)
    public ResponseEntity<CartaoExceptionHandler> cartaoInvalido(CartaoInvalidoException exception) {
        return new ResponseEntity<CartaoExceptionHandler>(exception.getCartao(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}

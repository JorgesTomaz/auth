package io.ideale.auth.controller;

import io.ideale.auth.dto.CartaoDTO;
import io.ideale.auth.model.Cartao;
import io.ideale.auth.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@Validated
@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<CartaoDTO> criarCartao(@Valid @RequestBody CartaoDTO cartaoDTO) {
        cartaoService.criarCartao(
                Cartao.builder()
                        .numero(cartaoDTO.getNumeroCartao())
                        .senha(cartaoDTO.getSenha())
                        .build()
        );
        return new ResponseEntity<>(cartaoDTO,HttpStatus.CREATED);
    }

    @GetMapping("{numeroCartao}")
    public ResponseEntity<BigDecimal> obtersaldo(@PathVariable String numeroCartao) {
        BigDecimal saldo = cartaoService.obterSaldo(numeroCartao);
        return new ResponseEntity<>(saldo,HttpStatus.OK);
    }

}

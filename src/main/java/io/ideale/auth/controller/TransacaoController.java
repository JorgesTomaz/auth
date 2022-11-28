package io.ideale.auth.controller;

import io.ideale.auth.dto.TransacaoDTO;
import io.ideale.auth.model.Cartao;
import io.ideale.auth.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<String> transacao(@Valid @RequestBody TransacaoDTO transacaoDTO) {
        Cartao cartao  = Cartao.builder()
                .numero(transacaoDTO.getNumeroCartao())
                .senha(transacaoDTO.getSenhaCartao())
                .valor(transacaoDTO.getValor())
                .build();
        cartaoService.debito(cartao);
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

}

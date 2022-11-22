package io.ideale.auth.controller;

import io.ideale.auth.dto.CartaoDTO;
import io.ideale.auth.dto.TransacaoDTO;
import io.ideale.auth.model.Cartao;
import io.ideale.auth.service.CartaoService;
import org.modelmapper.ModelMapper;
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
    private ModelMapper modelMapper;

    @Autowired
    private CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<CartaoDTO> transacao(@Valid @RequestBody TransacaoDTO transacaoDTO) {
        Cartao cartao  = modelMapper.map(transacaoDTO, Cartao.class);
        cartaoService.debito(cartao);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}

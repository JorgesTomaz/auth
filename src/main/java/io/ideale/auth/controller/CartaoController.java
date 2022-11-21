package io.ideale.auth.controller;

import io.ideale.auth.dto.CartaoDTO;
import io.ideale.auth.model.Cartao;
import io.ideale.auth.service.CartaoService;
import org.modelmapper.ModelMapper;
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
    private ModelMapper modelMapper;

    @Autowired
    private CartaoService cartaoService;

    @PostMapping
    public ResponseEntity criarCartao(@Valid @RequestBody CartaoDTO cartaoDTO) throws Exception {

        Cartao cartao  = modelMapper.map(cartaoDTO, Cartao.class);
        cartao = cartaoService.criarCartao(cartao);
        return new ResponseEntity<CartaoDTO>(cartaoDTO,HttpStatus.CREATED);
    }

    @GetMapping("{numeroCartao}")
    public ResponseEntity obtersaldo(@PathVariable String numeroCartao) throws Exception {
        BigDecimal saldo = cartaoService.obterSaldo(numeroCartao);
        return new ResponseEntity(saldo,HttpStatus.OK);
    }

}

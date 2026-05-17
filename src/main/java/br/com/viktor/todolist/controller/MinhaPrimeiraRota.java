package br.com.viktor.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/primeiraRota")
public class MinhaPrimeiraRota {
    
    @GetMapping("/primeiroMetodo")
    public String primeiraMensagem(){
        return "Funcionou!";
    }
}

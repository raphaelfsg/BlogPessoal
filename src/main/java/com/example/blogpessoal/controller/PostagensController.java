package com.example.blogpessoal.controller;

import com.example.blogpessoal.model.Postagens;
import com.example.blogpessoal.repository.PostagensRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

// @RestController define que essa classe é a controladora do programa
//@RequestMapping define o endereço de acesso da classe
//@CrossOrigin permite que aplicações de fora acessem a minha aplicação e o * diz que permite todas
@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagensController {

    // PostagensRepository postagensRepository  Substitui o comando implements na interface,
    // me permitindo ter controle total e assim podendo escolher o que vou pegar da interface
    // @Autowired (Injeção de Dependência), é a implementação utilizada pelo Spring Framework
    // para aplicar a Inversão de Controle (IoC) quando for necessário. A Injeção de Dependência
    // define quais Classes serão instanciadas e em quais lugares serão Injetadas quando houver
    // necessidade.
    @Autowired
    private PostagensRepository postagensRepository;

    //criando método para retornar todas as postagens
    @GetMapping
    public ResponseEntity<List<Postagens>> getAll(){
        return ResponseEntity.ok(postagensRepository.findAll());
    }

    //criando método para retornar uma pesquisa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Postagens> getById(@PathVariable Long id) {
        return postagensRepository.findById(id)
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Postagens>> getByTitulo(@PathVariable String titulo) {
        return ResponseEntity.ok(postagensRepository.findAllByTituloContainingIgnoreCase(titulo));
    }

    @PostMapping
    public ResponseEntity<Postagens> post(@Valid @RequestBody Postagens postagens) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postagensRepository.save(postagens));
    }


    @PutMapping
    public ResponseEntity<Postagens> put(@Valid @RequestBody Postagens postagens) {
        return postagensRepository.findById(postagens.getId())
                .map(resposta -> ResponseEntity.status(HttpStatus.OK)
                .body(postagensRepository.save(postagens)))
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //método de deleção
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Postagens> Postagens = postagensRepository.findById(id);
        if(Postagens.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        postagensRepository.deleteById(id);
    }
}
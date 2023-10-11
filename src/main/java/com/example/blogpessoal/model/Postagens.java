package com.example.blogpessoal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

// A anotação @Entity diz que essa classe é uma declaração de um banco de dados e aqui serão
// criadas as queries de criação das tabelas
// @Table criando a tabela
@Entity
@Table (name = "tb_postagens")
public class Postagens {

    // @Id Criando a id da tabela com bigint, que no spring se traduz com long
    // @GeneratedValue Declarando que ele vai auto incrementar
    // (strategy = GenerationType.IDENTITY) declarando o tipo de incremento
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotBlank declarando que ele não pode ser nulo
    // (message = "") mensagem pro programador
    // @Size diz o tamanho que deve ser usado, sendo mínimo e máximo

    @NotBlank(message = "Atributo Obrigatório")
    @Size(min = 2, max = 255)
    private String titulo;

    @NotBlank(message = "Atributo Obrigatório")
    @Size(min = 10, max = 1000)
    private String texto;

    // @UpdateTimestamp configura o Atributo data como Timestamp, ou seja, o Spring se
    // encarregará de obter a data e a hora do Sistema Operacional e inserir no Atributo
    // data toda vez que um Objeto da Classe Postagem for criado ou atualizado.
    @UpdateTimestamp
    private LocalDateTime data;

    @ManyToOne
    @JsonIgnoreProperties("postagens")
    private Tema tema;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }
}

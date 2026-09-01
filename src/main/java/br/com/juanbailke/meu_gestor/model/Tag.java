package br.com.juanbailke.meu_gestor.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String nome;

    @Column(length = 7, nullable = false)
    private String corHexadecimal;

    public Tag() {
    }

    public Tag(String corHexadecimal, String nome) {
        this.corHexadecimal = corHexadecimal;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCorHexadecimal() {
        return corHexadecimal;
    }

    public void setCorHexadecimal(String corHexadecimal) {
        this.corHexadecimal = corHexadecimal;
    }
}

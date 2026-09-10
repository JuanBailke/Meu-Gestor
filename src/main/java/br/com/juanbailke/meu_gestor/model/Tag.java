package br.com.juanbailke.meu_gestor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private List<Link> links = new ArrayList<>();

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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}

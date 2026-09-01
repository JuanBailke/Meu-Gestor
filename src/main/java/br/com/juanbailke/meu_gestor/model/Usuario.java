package br.com.juanbailke.meu_gestor.model;

import br.com.juanbailke.meu_gestor.model.enums.ProvedorLogin;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String nome;

    @Column(nullable = false, length = 60)
    private String email;

    private String urlFotoPerfil;

    private ProvedorLogin provedorLogin;

    private String idProvedor;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    public Usuario() {
    }

    public Usuario(String nome, String email, String urlFotoPerfil, ProvedorLogin provedorLogin, String idProvedor, LocalDateTime dataCriacao) {
        this.nome = nome;
        this.email = email;
        this.urlFotoPerfil = urlFotoPerfil;
        this.provedorLogin = provedorLogin;
        this.idProvedor = idProvedor;
        this.dataCriacao = dataCriacao;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlFotoPerfil() {
        return urlFotoPerfil;
    }

    public void setUrlFotoPerfil(String urlFotoPerfil) {
        this.urlFotoPerfil = urlFotoPerfil;
    }

    public ProvedorLogin getProvedorLogin() {
        return provedorLogin;
    }

    public void setProvedorLogin(ProvedorLogin provedorLogin) {
        this.provedorLogin = provedorLogin;
    }

    public String getIdProvedor() {
        return idProvedor;
    }

    public void setIdProvedor(String idProvedor) {
        this.idProvedor = idProvedor;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}

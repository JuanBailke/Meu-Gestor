package br.com.juanbailke.meu_gestor.model;

import br.com.juanbailke.meu_gestor.model.enums.Prioridade;
import br.com.juanbailke.meu_gestor.model.enums.StatusLink;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_link")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "URL original do conteúdo", example = "https://github.com/JuanBailke/Meu-Gestor")
    private String url;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String imagemCapa;

    @Schema(description = "Tempo estimado para consumir o conteúdo em minutos", example = "15")
    private Integer tempoEstimadoMinutos;

    private LocalDateTime dataSalvamento;

    private LocalDateTime dataLimite;

    @Enumerated(EnumType.STRING)
    private StatusLink status = StatusLink.NA_FILA;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
            name = "tb_link_tag",
            joinColumns = @JoinColumn(name = "link_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        dataSalvamento = LocalDateTime.now();
    }

    public Link() {
    }

    public Link(String url,
                String titulo,
                String descricao,
                String imagemCapa,
                Integer tempoEstimadoMinutos,
                LocalDateTime dataLimite,
                StatusLink status,
                Prioridade prioridade,
                Usuario usuario,
                List<Tag> tags) {
        this.url = url;
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagemCapa = imagemCapa;
        this.tempoEstimadoMinutos = tempoEstimadoMinutos;
        this.dataLimite = dataLimite;
        this.status = status;
        this.prioridade = prioridade;
        this.usuario = usuario;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagemCapa() {
        return imagemCapa;
    }

    public void setImagemCapa(String imagemCapa) {
        this.imagemCapa = imagemCapa;
    }

    public Integer getTempoEstimadoMinutos() {
        return tempoEstimadoMinutos;
    }

    public void setTempoEstimadoMinutos(Integer tempoEstimadoMinutos) {
        this.tempoEstimadoMinutos = tempoEstimadoMinutos;
    }

    public LocalDateTime getDataSalvamento() {
        return dataSalvamento;
    }

    public LocalDateTime getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDateTime dataLimite) {
        this.dataLimite = dataLimite;
    }

    public StatusLink getStatus() {
        return status;
    }

    public void setStatus(StatusLink status) {
        this.status = status;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}

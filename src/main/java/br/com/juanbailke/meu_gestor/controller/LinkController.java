package br.com.juanbailke.meu_gestor.controller;

import br.com.juanbailke.meu_gestor.model.Link;
import br.com.juanbailke.meu_gestor.repository.LinkRepository;
import br.com.juanbailke.meu_gestor.service.ScrapingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/links")
public class LinkController {

    private final LinkRepository linkRepository;
    private final ScrapingService scrapingService;

    public LinkController(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
        this.scrapingService = new ScrapingService();
    }

    @PostMapping
    public ResponseEntity<Link> criarLink(@RequestBody Link link) {
        if (link.getUrl() != null && !link.getUrl().isEmpty()) {
            ScrapingService.MetaDados metaDados = scrapingService.extrair(link.getUrl());

            if (link.getTitulo() == null || link.getTitulo().isEmpty())
                link.setTitulo(metaDados.titulo());

            if (link.getDescricao() == null || link.getDescricao().isEmpty())
                link.setDescricao(metaDados.descricao());

            if (link.getImagemCapa() == null || link.getImagemCapa().isEmpty())
                link.setImagemCapa(metaDados.imagemCapa());
        }
        Link linkSalvo = linkRepository.save(link);
        return ResponseEntity.status(HttpStatus.CREATED).body(linkSalvo);
    }

    @GetMapping
    public ResponseEntity<List<Link>> listarLinks() {
        return ResponseEntity.ok(linkRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Link> buscarLink(@PathVariable Long id) {
        Optional<Link> link = linkRepository.findById(id);
        return link.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Link> atualizarLink(@PathVariable Long id, @RequestBody Link linkAtualizado) {
        return linkRepository.findById(id)
                .map(linkExistente -> {
                    linkExistente.setUrl(linkAtualizado.getUrl());
                    linkExistente.setTitulo(linkAtualizado.getTitulo());
                    linkExistente.setDescricao(linkAtualizado.getDescricao());
                    linkExistente.setImagemCapa(linkAtualizado.getImagemCapa());
                    linkExistente.setTempoEstimadoMinutos(linkAtualizado.getTempoEstimadoMinutos());
                    linkExistente.setDataLimite(linkAtualizado.getDataLimite());
                    linkExistente.setStatus(linkAtualizado.getStatus());
                    linkExistente.setPrioridade(linkAtualizado.getPrioridade());
                    linkExistente.setTags(linkAtualizado.getTags());

                    linkRepository.save(linkExistente);
                    return ResponseEntity.ok(linkExistente);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLink(@PathVariable Long id) {
        if (linkRepository.existsById(id)) {
            linkRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

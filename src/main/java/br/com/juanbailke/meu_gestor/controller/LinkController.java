package br.com.juanbailke.meu_gestor.controller;

import br.com.juanbailke.meu_gestor.model.Link;
import br.com.juanbailke.meu_gestor.repository.LinkRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/links")
public class LinkController {

    private final LinkRepository linkRepository;

    public LinkController(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @PostMapping
    public ResponseEntity<Link> criarLink(@RequestBody Link link) {
        Link linkSalvo = linkRepository.save(link);
        return ResponseEntity.ok(linkSalvo);
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

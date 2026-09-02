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
}

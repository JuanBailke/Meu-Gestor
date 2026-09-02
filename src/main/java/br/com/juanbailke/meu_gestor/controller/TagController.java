package br.com.juanbailke.meu_gestor.controller;

import br.com.juanbailke.meu_gestor.model.Tag;
import br.com.juanbailke.meu_gestor.repository.TagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagRepository tagRepository;

    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @PostMapping
    public ResponseEntity<Tag> criarTag(@RequestBody Tag tag){
        Tag tagSalva = tagRepository.save(tag);
        return ResponseEntity.status(HttpStatus.CREATED).body(tagSalva);
    }

    @GetMapping
    public ResponseEntity<List<Tag>> listarTodas(){
        List<Tag> tags = tagRepository.findAll();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> buscarPorId(@PathVariable Long id){
        Optional<Tag> tag = tagRepository.findById(id);
        return tag.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

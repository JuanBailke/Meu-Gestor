package br.com.juanbailke.meu_gestor.controller;

import br.com.juanbailke.meu_gestor.model.Tag;
import br.com.juanbailke.meu_gestor.repository.TagRepository;
import br.com.juanbailke.meu_gestor.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "Gestão de Tags", description = "Endpoints para criar, listar, atualizar e deletar tags atreladas às tarefas salvas.")
@RequestMapping("/api/tags")
public class TagController {

    private final TagRepository tagRepository;
    private final UsuarioRepository usuarioRepository;

    public TagController(TagRepository tagRepository, UsuarioRepository usuarioRepository) {
        this.tagRepository = tagRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    @Operation(summary = "Adicionar uma nova tag de usuário")
    public ResponseEntity<Tag> criarTag(@RequestBody Tag tag,
                                        @AuthenticationPrincipal OAuth2User usuarioLogado) {
        String emailAutenticado = usuarioLogado.getAttribute("email");
        usuarioRepository.findByEmail(emailAutenticado).ifPresent(tag::setUsuario);

        Tag tagSalva = tagRepository.save(tag);
        return ResponseEntity.status(HttpStatus.CREATED).body(tagSalva);
    }

    @GetMapping
    @Operation(summary = "Listar minhas tags")
    public ResponseEntity<List<Tag>> listarTodas(@AuthenticationPrincipal OAuth2User usuarioLogado) {
        String emailAutenticado = usuarioLogado.getAttribute("email");
        List<Tag> tags = tagRepository.findByUsuarioEmail(emailAutenticado);
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tag pelo ID")
    public ResponseEntity<Tag> buscarPorId(@PathVariable Long id,
                                           @AuthenticationPrincipal OAuth2User usuarioLogado) {
        String emailAutenticado = usuarioLogado.getAttribute("email");
        Optional<Tag> tag = tagRepository.findByIdAndUsuarioEmail(id, emailAutenticado);
        return tag.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tag", description = "Atualiza os dados de uma tag, desde que pertença ao usuário autenticado.")
    public ResponseEntity<Tag> atualizarTag(@PathVariable Long id,
                                            @RequestBody Tag tag,
                                            @AuthenticationPrincipal OAuth2User usuarioLogado) {
        String emailAutenticado = usuarioLogado.getAttribute("email");

        return tagRepository.findByIdAndUsuarioEmail(id, emailAutenticado)
                .map(tagExistente -> {
                    tagExistente.setNome(tag.getNome());
                    tagExistente.setCorHexadecimal(tag.getCorHexadecimal());

                    tagRepository.save(tagExistente);
                    return ResponseEntity.ok(tagExistente);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar tag", description = "Remove uma tag do sistema, exigindo que o usuário autenticado seja o dono.")
    public ResponseEntity<Void> deletarTag(@PathVariable Long id,
                                            @AuthenticationPrincipal OAuth2User usuarioLogado) {
        String emailAutenticado = usuarioLogado.getAttribute("email");

        Optional<Tag> tagExistente = tagRepository.findByIdAndUsuarioEmail(id, emailAutenticado);

        if (tagExistente.isPresent()) {
            tagRepository.delete(tagExistente.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

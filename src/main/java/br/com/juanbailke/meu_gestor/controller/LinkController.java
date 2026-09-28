package br.com.juanbailke.meu_gestor.controller;

import br.com.juanbailke.meu_gestor.model.Link;
import br.com.juanbailke.meu_gestor.model.enums.StatusLink;
import br.com.juanbailke.meu_gestor.repository.LinkRepository;
import br.com.juanbailke.meu_gestor.repository.UsuarioRepository;
import br.com.juanbailke.meu_gestor.service.ScrapingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Gestão de Links", description = "Endpoints para criar, listar, atualizar e deletar links úteis.")
@RequestMapping("/api/links")
public class LinkController {

    private final LinkRepository linkRepository;
    private final ScrapingService scrapingService;
    private final UsuarioRepository usuarioRepository;

    public LinkController(LinkRepository linkRepository, UsuarioRepository usuarioRepository) {
        this.linkRepository = linkRepository;
        this.scrapingService = new ScrapingService();
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    @Operation(summary = "Salvar um novo link", description = "Cria um link no banco. Caso apenas a URL seja fornecida, o sistema fará Web Scraping autônomo (via Jsoup) para extrair o título e a capa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Link criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "URL inválida ou ausente")
    })
    public ResponseEntity<Link> criarLink(@RequestBody Link link,
                                          @AuthenticationPrincipal OAuth2User usuarioLogado) {
        //Extrai o email autenticado
        String emailAutenticado = usuarioLogado.getAttribute("email");

        usuarioRepository.findByEmail(emailAutenticado).ifPresent(link::setUsuario);

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
    @Operation(summary = "Listar meus links", description = "Retorna exclusivamente a lista de links pertencentes ao usuário autenticado.")
    public ResponseEntity<List<Link>> listarTodos(@AuthenticationPrincipal OAuth2User usuarioLogado) {
        String emailAutenticado = usuarioLogado.getAttribute("email");
        List<Link> meusLinks = linkRepository.findByUsuarioEmail(emailAutenticado);

        return ResponseEntity.ok(meusLinks);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar link por ID", description = "Retorna os detalhes de um link específico, desde que pertença ao usuário autenticado.")
    public ResponseEntity<Link> buscarLink(
            @Parameter(description = "ID numérico gerado pelo banco de dados", example = "1") @PathVariable Long id,
            @AuthenticationPrincipal OAuth2User usuarioLogado) {
        String emailAutenticado = usuarioLogado.getAttribute("email");

        return linkRepository.findByIdAndUsuarioEmail(id, emailAutenticado)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar link", description = "Atualiza os dados de um link, desde que pertença ao usuário autenticado.")
    public ResponseEntity<Link> atualizarLink(@PathVariable Long id,
                                              @RequestBody Link linkAtualizado,
                                              @AuthenticationPrincipal OAuth2User usuarioLogado) {

        String emailAutenticado = usuarioLogado.getAttribute("email");

        return linkRepository.findByIdAndUsuarioEmail(id, emailAutenticado)
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
    @Operation(summary = "Deletar link", description = "Remove um link do sistema, exigindo que o usuário autenticado seja o dono.")
    public ResponseEntity<Void> deletarLink(@PathVariable Long id,
                                            @AuthenticationPrincipal OAuth2User usuarioLogado) {
        String emailAutenticado = usuarioLogado.getAttribute("email");

        Optional<Link> linkExistente = linkRepository.findByIdAndUsuarioEmail(id, emailAutenticado);

        if (linkExistente.isPresent()) {
            linkRepository.delete(linkExistente.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    //Quadro KANBAN

    @GetMapping("/status/{status}")
    @Operation(summary = "Filtrar links por status", description = "Retorna os links do usuário isolados por coluna do Kanban.")
    public ResponseEntity<List<Link>> buscarPorStatus(@PathVariable StatusLink status,
                                                      @AuthenticationPrincipal OAuth2User usuarioLogado) {
        String emailAutenticado = usuarioLogado.getAttribute("email");

        var linksFiltrados = linkRepository.findByUsuarioEmailAndStatus(emailAutenticado, status);
        return ResponseEntity.ok(linksFiltrados);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Mover links no Kanban", description = "Atualiza rapidamente o status de um link (ideal para Drag and Drop).")
    public ResponseEntity<Link> atualizarStatusKanban(@PathVariable Long id,
                                                      @RequestParam StatusLink novoStatus,
                                                      @AuthenticationPrincipal OAuth2User usuarioLogado) {
        String emailAutenticado = usuarioLogado.getAttribute("email");

        return linkRepository.findByIdAndUsuarioEmail(id, emailAutenticado)
                .map(linkExistente -> {
                    linkExistente.setStatus(novoStatus);
                    Link link = linkRepository.save(linkExistente);
                    return ResponseEntity.ok(link);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

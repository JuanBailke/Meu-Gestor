package br.com.juanbailke.meu_gestor.service;

import br.com.juanbailke.meu_gestor.MeuGestorApplication;
import br.com.juanbailke.meu_gestor.model.Link;
import br.com.juanbailke.meu_gestor.model.enums.StatusLink;
import br.com.juanbailke.meu_gestor.repository.LinkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertaProcrastinacaoService {

    public static final Logger LOGGER = LoggerFactory.getLogger(AlertaProcrastinacaoService.class);
    private final LinkRepository linkRepository;

    public AlertaProcrastinacaoService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void verificarLinksProximosAoVencimento() {
        LocalDateTime agora = LocalDateTime.now();

        //Janela de alerta de 3 dias
        LocalDateTime daquiATresDias = agora.plusDays(3);

        List<Link> linksEmRisco = linkRepository.findByDataLimiteBetweenAndStatusNot(agora, daquiATresDias, StatusLink.CONCLUIDO);

        if (!linksEmRisco.isEmpty()) {
            LOGGER.info("===ALERTA DE PROCRASTINAÇÃO===");
            for (Link link : linksEmRisco) {
                LOGGER.warn("O link '{}' do usuário {} expira em breve!", link.getTitulo(), link.getUsuario().getEmail());
            }
        }
    }
}

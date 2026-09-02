package br.com.juanbailke.meu_gestor.repository;

import br.com.juanbailke.meu_gestor.model.Link;
import br.com.juanbailke.meu_gestor.model.enums.StatusLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Long> {

    List<Link> findByUsuarioId(Long id);

    List<Link> findByUsuarioIdAndStatus(Long usuarioId, StatusLink status);
}

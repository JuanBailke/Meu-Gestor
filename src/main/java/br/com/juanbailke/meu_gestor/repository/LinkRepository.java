package br.com.juanbailke.meu_gestor.repository;

import br.com.juanbailke.meu_gestor.model.Link;
import br.com.juanbailke.meu_gestor.model.Usuario;
import br.com.juanbailke.meu_gestor.model.enums.StatusLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, Long> {

    List<Link> findByUsuarioEmail(String email);

    Optional<Link> findByIdAndUsuarioEmail(Long id, String emailAutenticado);

    List<Link> findByUsuarioEmailAndStatus(String email, StatusLink statusLink);

    List<Link> findByUsuarioId(Long id);

    List<Link> findByUsuarioIdAndStatus(Long usuarioId, StatusLink status);
}

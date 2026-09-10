package br.com.juanbailke.meu_gestor.repository;

import br.com.juanbailke.meu_gestor.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByUsuarioEmail(String email);

    Optional<Tag> findByIdAndUsuarioEmail(Long id, String email);

    Optional<Tag> findByNomeIgnoreCase(String nome);
}

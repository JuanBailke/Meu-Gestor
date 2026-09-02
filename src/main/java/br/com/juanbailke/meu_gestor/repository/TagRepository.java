package br.com.juanbailke.meu_gestor.repository;

import br.com.juanbailke.meu_gestor.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByNomeIgnoreCase(String nome);
}

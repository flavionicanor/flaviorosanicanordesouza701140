package br.mt.artists.repository;

import br.mt.artists.domain.entity.Regional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegionalRepository extends JpaRepository<Regional, Integer> {

    Optional<Regional> findTopByIdAndAtivoTrueOrderByCreatedAtDesc(Integer id);

    List<Regional> findAllByAtivoTrue();
}

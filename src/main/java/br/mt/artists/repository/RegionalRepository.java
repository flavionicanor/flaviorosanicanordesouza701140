package br.mt.artists.repository;

import br.mt.artists.domain.entity.Regional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionalRepository extends JpaRepository<Regional, Integer> {
}

package br.mt.artists.repository;

import br.mt.artists.domain.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    Page<Artist> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

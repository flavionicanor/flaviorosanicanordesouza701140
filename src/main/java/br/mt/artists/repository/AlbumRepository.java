package br.mt.artists.repository;

import br.mt.artists.domain.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository  extends JpaRepository<Album, Long> {
}

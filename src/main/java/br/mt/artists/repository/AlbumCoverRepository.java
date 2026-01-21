package br.mt.artists.repository;


import br.mt.artists.domain.entity.AlbumCover;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumCoverRepository extends JpaRepository<AlbumCover, Long> {
    List<AlbumCover> findByAlbumId(Long albumId);
}

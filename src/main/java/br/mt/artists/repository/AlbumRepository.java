package br.mt.artists.repository;

import br.mt.artists.domain.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlbumRepository  extends JpaRepository<Album, Long> {

    @Query("""
    select distinct a
    from Album a
    join a.artists ar
    where ar.id = :artistId
    """)
    Page<Album> findAllByArtistId(@Param("artistId") Long artistId, Pageable pageable);

}

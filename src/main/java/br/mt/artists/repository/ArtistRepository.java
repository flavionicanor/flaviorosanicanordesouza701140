package br.mt.artists.repository;

import br.mt.artists.domain.dto.response.ArtistResponseDTO;
import br.mt.artists.domain.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    Page<ArtistResponseDTO> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("""
    select new br.mt.artists.domain.dto.response.ArtistResponseDTO(
        a.id,
        a.name,
        count(al) as albumsCount
    )
    from Artist a
    left join a.albums al
    where (:name = '' or lower(a.name) like lower(concat('%', :name, '%')))
    group by a.id, a.name
    """)
    Page<ArtistResponseDTO> findAllWithAlbumCount(
            @Param("name") String name,
            Pageable pageable
    );
}

package br.mt.artists.domain.dto.response;

import br.mt.artists.domain.entity.Artist;

public record ArtistResponseDTO(
        Long id,
        String name,
        java.time.LocalDateTime createdAt) {

    public static ArtistResponseDTO fromEntity(Artist artist) {
        return new ArtistResponseDTO(
                artist.getId(),
                artist.getName(),
                artist.getCreatedAt()
        );
    }
}


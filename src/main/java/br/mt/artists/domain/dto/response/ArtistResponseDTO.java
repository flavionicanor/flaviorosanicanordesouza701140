package br.mt.artists.domain.dto.response;

import br.mt.artists.domain.entity.Artist;

public record ArtistResponseDTO(
        Long id,
        String name,
        Long albumsCount) {

    public static ArtistResponseDTO fromEntity(Artist artist) {
        return new ArtistResponseDTO(
                artist.getId(),
                artist.getName(),
                0L
        );
    }
}


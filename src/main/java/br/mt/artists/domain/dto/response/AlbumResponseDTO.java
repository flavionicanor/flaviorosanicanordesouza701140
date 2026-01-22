package br.mt.artists.domain.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record AlbumResponseDTO(
        Long id,
        String title,
        Set<ArtistResponseDTO> artists,
        List<String> covers,
        LocalDateTime createdAt
) {}


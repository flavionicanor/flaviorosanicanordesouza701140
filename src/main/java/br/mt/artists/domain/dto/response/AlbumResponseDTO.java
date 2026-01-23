package br.mt.artists.domain.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class AlbumResponseDTO {

    private Long id;
    private String title;
    private Set<ArtistResponseDTO> artists;
    private List<String> covers;
    private LocalDateTime createdAt;

    public AlbumResponseDTO(
            Long id,
            String title,
            Set<ArtistResponseDTO> artists,
            List<String> covers,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.artists = artists;
        this.covers = covers;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Set<ArtistResponseDTO> getArtists() {
        return artists;
    }

    public List<String> getCovers() {
        return covers;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

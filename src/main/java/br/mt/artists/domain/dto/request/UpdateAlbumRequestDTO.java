package br.mt.artists.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateAlbumRequestDTO {

    @NotBlank(message = "Título do album é obrigatório")
    private String title;

    public String getTitle() {
        return title;
    }
}

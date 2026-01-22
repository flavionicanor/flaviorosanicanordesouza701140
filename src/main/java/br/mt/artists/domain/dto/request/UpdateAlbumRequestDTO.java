package br.mt.artists.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateAlbumRequestDTO {

    @NotBlank(message = "title é obrigatório")
    @Size(min = 2, max = 200, message = "Título deve ter entre 2 e 200 caracteres")
    private String title;

    public String getTitle() {
        return title;
    }
}

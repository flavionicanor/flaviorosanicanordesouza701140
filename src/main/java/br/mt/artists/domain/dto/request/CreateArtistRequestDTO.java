package br.mt.artists.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateArtistRequestDTO {

    @NotBlank(message = "Nome do artista é obrigatório")
    @Size(min =2, max = 200, message = "Nome deve ter entre 2 e 200 caracteres")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

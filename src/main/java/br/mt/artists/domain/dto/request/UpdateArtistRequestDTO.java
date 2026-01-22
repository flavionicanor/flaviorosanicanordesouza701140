package br.mt.artists.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateArtistRequestDTO {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

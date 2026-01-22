package br.mt.artists.controller.v1;

import br.mt.artists.domain.dto.request.CreateArtistRequestDTO;
import br.mt.artists.domain.dto.request.UpdateArtistRequestDTO;
import br.mt.artists.domain.entity.Artist;
import br.mt.artists.service.ArtistService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/artists")
public class ArtistController {
    private final ArtistService artisService;

    public ArtistController(ArtistService artisService) {
        this.artisService = artisService;
    }

    @GetMapping
    public Page<Artist> list(
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            Pageable pageable
    ) {
        return artisService.search(name == null ? "" : name, pageable);
    }

    @GetMapping("/{id}")
    public Artist findById(@PathVariable(name = "id") Long id) {
        return artisService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Artist create(
            @Valid @RequestBody CreateArtistRequestDTO request
    ) {
        return artisService.create(request.getName());
    }

    @PutMapping("/{id}")
    public Artist Update(@PathVariable("id") Long id,
                         @Valid @RequestBody UpdateArtistRequestDTO requestDTO){
        return artisService.update(id, requestDTO.getName());
    }


}

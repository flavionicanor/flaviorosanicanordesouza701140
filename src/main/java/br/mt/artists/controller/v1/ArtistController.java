package br.mt.artists.controller.v1;

import br.mt.artists.domain.dto.request.CreateArtistRequestDTO;
import br.mt.artists.domain.dto.request.UpdateArtistRequestDTO;
import br.mt.artists.domain.dto.response.ArtistResponseDTO;
import br.mt.artists.domain.entity.Artist;
import br.mt.artists.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/artists")
public class ArtistController {
    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public Page<ArtistResponseDTO> list(
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            Pageable pageable
    ) {
        return artistService.search(name, pageable);
    }

    @GetMapping("/{id}")
    public ArtistResponseDTO findById(@PathVariable(name = "id") Long id) {
        return artistService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArtistResponseDTO create(
            @Valid @RequestBody CreateArtistRequestDTO request
    ) {
        return artistService.create(request.getName());
    }

    @PutMapping("/{id}")
    public ArtistResponseDTO Update(@PathVariable("id") Long id,
                         @Valid @RequestBody UpdateArtistRequestDTO requestDTO){
        return artistService.update(id, requestDTO.getName());
    }


}

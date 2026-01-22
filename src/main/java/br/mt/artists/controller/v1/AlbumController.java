package br.mt.artists.controller.v1;

import br.mt.artists.domain.dto.request.CreateAlbumRequestDTO;
import br.mt.artists.domain.dto.request.UpdateAlbumRequestDTO;
import br.mt.artists.domain.dto.response.AlbumResponseDTO;
import br.mt.artists.domain.entity.Album;
import br.mt.artists.service.AlbumService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/albums")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    // POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Album create(
            @Valid @RequestBody CreateAlbumRequestDTO request
    ) {
        return albumService.create(
                request.getTitle(),
                request.getArtistId()
        );
    }

    // GET ALL
    @GetMapping
    public List<Album> findAll() {
        return albumService.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public AlbumResponseDTO findById(
            @PathVariable("id") Long id
    ) {
        return albumService.getById(id);
    }

    // PUT
    @PutMapping("/{id}")
    public Album update(@PathVariable("id") Long id,
                        @Valid @RequestBody UpdateAlbumRequestDTO requestDTO){

        return albumService.update(id, requestDTO.getTitle());

    }

}


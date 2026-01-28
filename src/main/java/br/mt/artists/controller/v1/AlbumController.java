package br.mt.artists.controller.v1;

import br.mt.artists.domain.dto.request.CreateAlbumRequestDTO;
import br.mt.artists.domain.dto.request.UpdateAlbumRequestDTO;
import br.mt.artists.domain.dto.response.AlbumResponseDTO;
import br.mt.artists.domain.entity.Album;
import br.mt.artists.service.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/albums")
public class AlbumController {

    private final AlbumService albumService;

    // POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlbumResponseDTO create(
            @Valid @RequestBody CreateAlbumRequestDTO request
    ) {
        return albumService.create(
                request.getTitle(),
                request.getArtistId()
        );
    }

    @GetMapping
    public Page<AlbumResponseDTO> findAll(
            @RequestParam(name = "artistId", required = false) Long artistId,
            Pageable pageable) {
        return albumService.findAll(artistId, pageable);
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
    public AlbumResponseDTO update(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateAlbumRequestDTO requestDTO){

        return albumService.update(id, requestDTO.getTitle());

    }

}


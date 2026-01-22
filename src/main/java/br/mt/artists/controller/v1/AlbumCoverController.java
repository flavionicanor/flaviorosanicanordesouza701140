package br.mt.artists.controller.v1;

import br.mt.artists.domain.dto.request.UpdateAlbumRequestDTO;
import br.mt.artists.domain.dto.response.AlbumCoverResponseDTO;
import br.mt.artists.domain.entity.Album;
import br.mt.artists.service.AlbumCoverService;
import br.mt.artists.service.AlbumService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/albums/{id}/covers")
public class AlbumCoverController {

    private final AlbumCoverService albumCoverService;
    private final AlbumService albumService;

    public AlbumCoverController(AlbumCoverService albumCoverService, AlbumService albumService) {
        this.albumCoverService = albumCoverService;
        this.albumService = albumService;
    }

    @PostMapping
    public ResponseEntity<List<String>> uploadCovers(
            @PathVariable("id") Long id,
            @RequestParam("files") List<MultipartFile> files
    ) {
        return ResponseEntity.ok(albumService.uploadCovers(id, files));
    }

    @GetMapping
    public List<AlbumCoverResponseDTO> list(
            @PathVariable("id") Long id
    ) {
        return albumCoverService.listByAlbum(id);
    }

}

package br.mt.artists.controller.v1;

import br.mt.artists.domain.dto.AlbumCoverResponseDTO;
import br.mt.artists.service.AlbumCoverService;
import br.mt.artists.service.AlbumService;
import br.mt.artists.service.StorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/albums")
public class AlbumCoverController {

    private final AlbumCoverService albumCoverService;
    private final AlbumService albumService;

    public AlbumCoverController(AlbumCoverService albumCoverService, AlbumService albumService) {
        this.albumCoverService = albumCoverService;
        this.albumService = albumService;
    }

    @PostMapping(value = "/{id}/covers")
    public ResponseEntity<List<String>> uploadCovers(
            @PathVariable("id") Long id,
            @RequestParam("files") List<MultipartFile> files
    ) {
        return ResponseEntity.ok(albumService.uploadCovers(id, files));
    }

    @GetMapping(value = "/{id}/covers")
    public List<AlbumCoverResponseDTO> list(
            @PathVariable("id") Long id
    ) {
        System.out.println("chegou aqui");
        return albumCoverService.listByAlbum(id);
    }




}

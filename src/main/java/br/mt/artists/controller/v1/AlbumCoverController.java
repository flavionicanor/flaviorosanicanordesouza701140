package br.mt.artists.controller.v1;

import br.mt.artists.domain.dto.request.UpdateAlbumRequestDTO;
import br.mt.artists.domain.dto.response.AlbumCoverResponseDTO;
import br.mt.artists.domain.entity.Album;
import br.mt.artists.service.AlbumCoverService;
import br.mt.artists.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/albums")
//@RequestMapping("/api/v1/albums/{id}/covers")
public class AlbumCoverController {

    private final AlbumCoverService albumCoverService;
    private final AlbumService albumService;

    public AlbumCoverController(AlbumCoverService albumCoverService, AlbumService albumService) {
        this.albumCoverService = albumCoverService;
        this.albumService = albumService;
    }

    @PostMapping(value = "/{id}/covers",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload de capas do álbum",
            description = "Faz upload de uma ou mais imagens para o álbum"
    )
    public ResponseEntity<Void> uploadCover(
            @PathVariable("id") Long albumId,
            @RequestPart("files")
            @Schema(
                    type = "string",
                    format = "binary"
            )
            MultipartFile[] files
    ) {
        albumService.uploadCovers(albumId, List.of(files));
        return ResponseEntity.ok().build();
    }


    @GetMapping(value = "/{id}/covers")
    public List<AlbumCoverResponseDTO> list(
            @PathVariable("id") Long id
    ) {
        return albumCoverService.listByAlbum(id);
    }

}

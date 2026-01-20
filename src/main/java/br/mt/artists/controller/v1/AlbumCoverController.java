package br.mt.artists.controller.v1;

import br.mt.artists.service.StorageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/albums")
public class AlbumCoverController {

    private final StorageService storageService;

    public AlbumCoverController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(
            value = "/{albumId}/covers",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Map<String, String> uploadCover(
            @PathVariable(name = "albumId") Long albumId,
            @RequestPart("file") MultipartFile file
    ) throws IOException {

        String objectName =
                "albums/" + albumId + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        storageService.upload(
                objectName,
                file.getInputStream(),
                file.getContentType()
        );

        String url = storageService.generatePresignedUrl(objectName);

        return Map.of("url", url);
    }

}

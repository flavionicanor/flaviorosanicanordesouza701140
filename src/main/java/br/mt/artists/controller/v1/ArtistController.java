package br.mt.artists.controller.v1;

import br.mt.artists.domain.entity.Artist;
import br.mt.artists.service.ArtistService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/artists")
public class ArtistController {
    private final ArtistService artisService;


    public ArtistController(ArtistService artisService) {
        this.artisService = artisService;
    }

    @GetMapping
    public Page<Artist> list(@RequestParam(required = false) String name, Pageable pageable) {
        return artisService.search(name == null ? "" : name, pageable);
    }
}

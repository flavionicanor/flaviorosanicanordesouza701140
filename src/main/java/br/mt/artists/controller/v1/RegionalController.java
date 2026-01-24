package br.mt.artists.controller.v1;

import br.mt.artists.domain.entity.Regional;
import br.mt.artists.service.RegionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/regionais")
@RequiredArgsConstructor
public class RegionalController {

    private final RegionalService service;

    @PostMapping("/sync")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sync() {
        service.syncRegionais();
    }

    @GetMapping
    public List<Regional> list() {
        return service.listAtivos();
    }
}

package br.mt.artists.service;

import br.mt.artists.domain.entity.Artist;
import br.mt.artists.repository.ArtistRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
public class ArtistService {

    private final ArtistRepository artistiRepository;


    public ArtistService(ArtistRepository artistiRepository) {
        this.artistiRepository = artistiRepository;
    }

    public Page<Artist> search(String name, Pageable pageable) {
        return artistiRepository.findByNameContainingIgnoreCase(name, pageable);
    }
}

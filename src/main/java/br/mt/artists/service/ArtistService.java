package br.mt.artists.service;

import br.mt.artists.domain.entity.Artist;
import br.mt.artists.exception.ResourceNotFoundException;
import br.mt.artists.repository.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ArtistService {

    private final ArtistRepository artistRepository;


    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public Page<Artist> search(String name, Pageable pageable) {
        return artistRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional
    public Artist create(String name) {
        Artist artist = new Artist();
        artist.setName(name);
        return artistRepository.save(artist);
    }


    @Transactional(readOnly = true)
    public Artist findById(Long id) {
        return artistRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Artista não encontrado")
                );
    }

    @Transactional
    public Artist update(Long id, String name) {
        Artist artist = artistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Artista não encontrado"));

        artist.setName(name);
        return artistRepository.save(artist);
    }

}

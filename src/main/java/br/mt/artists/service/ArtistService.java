package br.mt.artists.service;

import br.mt.artists.domain.dto.response.ArtistResponseDTO;
import br.mt.artists.domain.entity.Artist;
import br.mt.artists.exception.ResourceNotFoundException;
import br.mt.artists.repository.ArtistRepository;
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

    @Transactional(readOnly = true)
    public Page<ArtistResponseDTO> search(String name, Pageable pageable) {
        return artistRepository.findAllWithAlbumCount(name, pageable);
    }

    @Transactional
    public ArtistResponseDTO create(String name) {
        Artist artist = new Artist();
        artist.setName(name);
        return ArtistResponseDTO.fromEntity(artistRepository.save(artist));
    }


    @Transactional(readOnly = true)
    public ArtistResponseDTO findById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Artista não encontrado")
                );

        return ArtistResponseDTO.fromEntity(artist);
    }

    @Transactional
    public ArtistResponseDTO update(Long id, String name) {
        Artist artist = artistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Artista não encontrado"));

        artist.setName(name);

        return ArtistResponseDTO.fromEntity(artistRepository.save(artist));
    }

}

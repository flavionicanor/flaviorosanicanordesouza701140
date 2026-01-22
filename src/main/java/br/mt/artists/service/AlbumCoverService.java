package br.mt.artists.service;

import br.mt.artists.domain.dto.response.AlbumCoverResponseDTO;
import br.mt.artists.repository.AlbumCoverRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumCoverService {

    private final AlbumCoverRepository albumCoverRepository;
    private final StorageService storageService;


    public AlbumCoverService(AlbumCoverRepository albumCoverRepository, StorageService storageService) {
        this.albumCoverRepository = albumCoverRepository;
        this.storageService = storageService;
    }

    public List<AlbumCoverResponseDTO> listByAlbum(Long albumId){
        return albumCoverRepository.findByAlbumId(albumId)
                .stream()
                .map(cover -> new AlbumCoverResponseDTO(
                        cover.getId(),storageService.getPresignedUrl(cover.getObjectName())
                ))
                .toList();
    }


}

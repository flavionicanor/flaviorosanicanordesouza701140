package br.mt.artists.service;

import br.mt.artists.domain.dto.AlbumCoverResponseDTO;
import br.mt.artists.domain.entity.Album;
import br.mt.artists.repository.AlbumCoverRepository;
import br.mt.artists.repository.AlbumRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

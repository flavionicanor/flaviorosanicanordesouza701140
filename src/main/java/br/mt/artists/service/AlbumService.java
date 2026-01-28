package br.mt.artists.service;

import br.mt.artists.domain.dto.response.AlbumResponseDTO;
import br.mt.artists.domain.dto.response.ArtistResponseDTO;
import br.mt.artists.domain.entity.Album;
import br.mt.artists.domain.entity.AlbumCover;
import br.mt.artists.domain.entity.Artist;
import br.mt.artists.exception.BusinessException;
import br.mt.artists.exception.ResourceNotFoundException;
import br.mt.artists.repository.AlbumCoverRepository;
import br.mt.artists.repository.AlbumRepository;
import br.mt.artists.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final StorageService storageService;
    private final ArtistRepository artistRepository;
    private final AlbumCoverRepository albumCoverRepository;
    private final AlbumNotificationService albumNotificationService;


    @Transactional
    public List<String> uploadCovers(Long albumId, List<MultipartFile> files){
        if(files == null || files.isEmpty()){
            throw new ResourceNotFoundException("Nenhum arquivo enviado");
        }

        Album album = albumRepository.findById(albumId).orElseThrow(() -> new RuntimeException("Album não encontrado"));

        List<String> uploadedObjects = new ArrayList<>();

        for(MultipartFile file : files){
            try {

                String objectName = buildObjectName(albumId, file.getOriginalFilename());
                storageService.upload(
                        objectName,
                        file.getInputStream(),
                        file.getContentType()
                );

                album.addCover(objectName);
                uploadedObjects.add(objectName);
            } catch (Exception e) {
                throw new BusinessException("Erro ao fazer upload do arquivo");
            }

        }

        albumRepository.save(album);
        return uploadedObjects;
    }

    private String buildObjectName(Long albumId, String originalFilename){
        return "albuns/" +albumId + "/" + UUID.randomUUID() + "_" + originalFilename;
    }

    @Transactional
    public AlbumResponseDTO create(String title, Long artistId) {

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artista não encontrado"));

        Album album = new Album();
        album.setTitle(title);
        album.getArtists().add(artist);

        Album saved = albumRepository.save(album);

        // Dispara evento websocket
        albumNotificationService.notifyAlbumCreated(saved);

        return mapToDTO(saved);
    }

    private AlbumResponseDTO mapToDTO(Album album) {

        Set<ArtistResponseDTO> artists = album.getArtists().stream()
                .map(ArtistResponseDTO::fromEntity)
                .collect(Collectors.toSet());

        List<String> covers = album.getCovers().stream()
                .map(storageService::generatePresignedUrl)
                .toList();

        return new AlbumResponseDTO(
                album.getId(),
                album.getTitle(),
                artists,
                covers,
                album.getCreatedAt()
        );
    }

    @Transactional
    public AlbumResponseDTO update(Long id, String title) {

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album não encontrado"));

        album.setTitle(title);

        Album updated = albumRepository.save(album);

        return mapToDTO(updated);
    }

    @Transactional(readOnly = true)
    public Page<AlbumResponseDTO> findAll(Long artistId,Pageable pageable) {

        Page<Album> page;

        if (artistId != null) {
            page = albumRepository.findAllByArtistId(artistId, pageable);
        } else {
            page = albumRepository.findAll(pageable);
        }




        return page.map(album -> {

                    List<String> covers = album.getCovers().stream()
                            .map(storageService::generatePresignedUrl)
                            .toList();

                    Set<ArtistResponseDTO> artists = album.getArtists().stream()
                            .map(ArtistResponseDTO::fromEntity)
                            .collect(Collectors.toSet());

                    return new AlbumResponseDTO(
                            album.getId(),
                            album.getTitle(),
                            artists,
                            covers,
                            album.getCreatedAt()
                    );
                });
    }


    private AlbumResponseDTO mapToResponse(Album album) {

        Set<ArtistResponseDTO> artists = album.getArtists().stream()
                .map(ArtistResponseDTO::fromEntity)
                .collect(Collectors.toSet());

        List<String> covers = album.getCovers().stream()
                .map(storageService::generatePresignedUrl)
                .toList();

        return new AlbumResponseDTO(
                album.getId(),
                album.getTitle(),
                artists,
                covers,
                album.getCreatedAt()
        );
    }

    public Album findById(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Album não encontrado com id " + id
                ));
    }

    @Transactional(readOnly = true)
    public AlbumResponseDTO getById(Long id) {

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album não encontrado"));

        Set<ArtistResponseDTO> artists = album.getArtists().stream()
                .map(ArtistResponseDTO::fromEntity)
                .collect(Collectors.toSet());

        List<String> covers = album.getCovers().stream()
                .map(storageService::generatePresignedUrl)
                .toList();

        return new AlbumResponseDTO(
                album.getId(),
                album.getTitle(),
                artists,
                covers,
                album.getCreatedAt()
        );
    }



}

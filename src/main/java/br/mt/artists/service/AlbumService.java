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
import jakarta.transaction.Transactional;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final StorageService storageService;
    private final ArtistRepository artistRepository;
    private final AlbumCoverRepository albumCoverRepository;


    public AlbumService(AlbumRepository albumRepository, StorageService storageService, ArtistRepository artistRepository, AlbumCoverRepository albumCoverRepository) {
        this.albumRepository = albumRepository;
        this.storageService = storageService;
        this.artistRepository = artistRepository;
        this.albumCoverRepository = albumCoverRepository;
    }

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

    public Album create(String title, Long artistId){
        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artista não encontrado com o ID informado"));

        Album album = new Album();
        album.setTitle(title);
        album.getArtists().add(artist);
        artist.getAlbums().add(album);

        return albumRepository.save(album);

    }

    public Album update(Long id, String title){
        Album album = albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Album não encontrado"));

        album.setTitle(title);
        return albumRepository.save(album);

    }

    public List<Album> findAll() {
        return albumRepository.findAll();
    }

    public Album findById(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Álbum não encontrado com id " + id
                ));
    }

    @Transactional()
    public AlbumResponseDTO getById(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Álbum não encontrado"));

            Set<ArtistResponseDTO> artists = album.getArtists()
                .stream()
                .map(artist -> new ArtistResponseDTO(
                        artist.getId(),
                        artist.getName()
                ))
                .collect(Collectors.toSet());


        List<String> covers = albumCoverRepository
                .findByAlbumId(album.getId())
                .stream()
                .map(AlbumCover::getObjectName)
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

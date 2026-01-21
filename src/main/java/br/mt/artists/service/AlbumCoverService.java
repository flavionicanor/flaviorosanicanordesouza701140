package br.mt.artists.service;

import br.mt.artists.domain.entity.Album;
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

    private final AlbumRepository albumRepository;
    private final StorageService storageService;


    public AlbumCoverService(AlbumRepository albumRepository, StorageService storageService) {
        this.albumRepository = albumRepository;
        this.storageService = storageService;
    }

    @Transactional
    public List<String> uploadCovers(Long albumId, List<MultipartFile> files){
        if(files == null || files.isEmpty()){
            throw new IllegalArgumentException("Nenhum arquivo enviado");
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
            } catch (IOException e) {
                throw new RuntimeException("Erro ao fazer upload do arquivo", e);
            }

        }

        albumRepository.save(album);
        return uploadedObjects;
    }

    private String buildObjectName(Long albumId, String originalFilename){
        return "albuns/" +albumId + "/" + UUID.randomUUID() + "_" + originalFilename;
    }
}

package br.mt.artists.service;

import br.mt.artists.domain.dto.event.AlbumCreatedEventDTO;
import br.mt.artists.domain.entity.Album;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlbumNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyAlbumCreated(Album album) {
        AlbumCreatedEventDTO eventDTO = new AlbumCreatedEventDTO(
                album.getId(),
                album.getTitle(),
                album.getCreatedAt()
        );

        messagingTemplate.convertAndSend("/topic/albums", eventDTO);
    }
}

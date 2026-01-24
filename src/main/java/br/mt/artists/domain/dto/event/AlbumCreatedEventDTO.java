package br.mt.artists.domain.dto.event;

import java.time.LocalDateTime;

public record AlbumCreatedEventDTO(Long id, String title,LocalDateTime createdAt){}

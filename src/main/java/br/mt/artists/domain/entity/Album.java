package br.mt.artists.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "album")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @ManyToMany(mappedBy = "albums")
    private Set<Artist> artists;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

}

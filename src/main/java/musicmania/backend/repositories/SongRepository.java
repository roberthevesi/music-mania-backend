package musicmania.backend.repositories;

import musicmania.backend.entities.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SongRepository extends JpaRepository<Song, UUID> {
}

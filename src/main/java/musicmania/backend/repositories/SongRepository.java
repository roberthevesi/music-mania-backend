package musicmania.backend.repositories;

import musicmania.backend.entities.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    @Query(value =  "SELECT * FROM songs WHERE id NOT IN (" +
                                                        "SELECT song_id FROM user_song_history WHERE user_id = :user_id " +
                                                        "AND last_played_date > current_date - interval '1 week') " +
                    "ORDER BY RANDOM() LIMIT 4",
            nativeQuery = true)
    List<Song> getFourSongs(@Param("user_id") Long user_id);

}

package musicmania.backend.repositories;

import musicmania.backend.entities.UserSongHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSongHistoryRepository extends JpaRepository<UserSongHistory, Long> {
}

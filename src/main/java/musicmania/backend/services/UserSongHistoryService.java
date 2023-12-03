package musicmania.backend.services;

import musicmania.backend.entities.UserSongHistory;
import musicmania.backend.repositories.UserSongHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserSongHistoryService {
    private final UserSongHistoryRepository userSongHistoryRepository;

    @Autowired
    public UserSongHistoryService(UserSongHistoryRepository userSongHistoryRepository) {
        this.userSongHistoryRepository = userSongHistoryRepository;
    }

    public UserSongHistory saveUserSongHistory(long user_id, long song_id, LocalDateTime date){
        UserSongHistory history = new UserSongHistory(user_id, song_id, date);
        userSongHistoryRepository.save(history);

        return history;
    }
}

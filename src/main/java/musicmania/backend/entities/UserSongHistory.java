package musicmania.backend.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_song_history")
public class UserSongHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long user_id;
    private long song_id;
    private LocalDateTime last_played_date;

    public UserSongHistory(long id, long user_id, long song_id, LocalDateTime last_played_date) {
        this.id = id;
        this.user_id = user_id;
        this.song_id = song_id;
        this.last_played_date = last_played_date;
    }

    public UserSongHistory() {

    }

    public UserSongHistory(long user_id, long song_id, LocalDateTime last_played_date) {
        this.user_id = user_id;
        this.song_id = song_id;
        this.last_played_date = last_played_date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getSong_id() {
        return song_id;
    }

    public void setSong_id(long song_id) {
        this.song_id = song_id;
    }

    public LocalDateTime getLast_played_date() {
        return last_played_date;
    }

    public void setLast_played_date(LocalDateTime last_played_date) {
        this.last_played_date = last_played_date;
    }
}

package musicmania.backend.entities;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "songs")
public class Song implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String artist;
    private byte[] audioData;

    public Song(String title, String artist, byte[] audioData) {
        this.title = title;
        this.artist = artist;
        this.audioData = audioData;
    }

    public Song(long id, String title, String artist, byte[] audioData) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.audioData = audioData;
    }

    public Song() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public byte[] getAudioData() {
        return audioData;
    }

    public void setAudioData(byte[] audioData) {
        this.audioData = audioData;
    }
}

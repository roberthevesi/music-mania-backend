package musicmania.backend.entities;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "songs")
public class Song implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "song_id_seq")
//    @SequenceGenerator(name = "song_id_seq", sequenceName = "song_id_seq", allocationSize = 1)
    private long id;
    private String title;
    private String artist;
    private String fileURL;
    private String imageURL;

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String songURL) {
        this.fileURL = songURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Song(long id, String title, String artist, String fileURL, String imageURL) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.fileURL = fileURL;
        this.imageURL = imageURL;
    }

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
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
}

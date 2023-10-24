package musicmania.backend.entities;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private UUID id;
    private String email;
    private String password;
    private Integer score;
    private String profilePictureURL;

    public User() {
    }

    public User(String email, String password, Integer score, String profilePictureURL) {
        this.email = email;
        this.password = password;
        this.score = score;
        this.profilePictureURL = profilePictureURL;
    }

    public User(UUID id, String email, String password, Integer score, String profilePictureURL) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.score = score;
        this.profilePictureURL = profilePictureURL;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", score=" + score +
                ", profilePictureURL='" + profilePictureURL + '\'' +
                '}';
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }
}

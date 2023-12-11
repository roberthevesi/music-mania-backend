package musicmania.backend.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_codes")
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String user_email;
    private String code;
    private LocalDateTime generation_date;
    private LocalDateTime expiration_date;
    private boolean used;

    public VerificationCode(long id, String user_email, String code, LocalDateTime generation_date, LocalDateTime expiration_date, Boolean used) {
        this.id = id;
        this.user_email = user_email;
        this.code = code;
        this.generation_date = generation_date;
        this.expiration_date = expiration_date;
        this.used = used;
    }

    public VerificationCode(String user_email, String code, LocalDateTime generation_date, LocalDateTime expiration_date) {
        this.user_email = user_email;
        this.code = code;
        this.generation_date = generation_date;
        this.expiration_date = expiration_date;
    }

    public VerificationCode() {

    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getGeneration_date() {
        return generation_date;
    }

    public void setGeneration_date(LocalDateTime generation_date) {
        this.generation_date = generation_date;
    }

    public LocalDateTime getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(LocalDateTime expiration_date) {
        this.expiration_date = expiration_date;
    }
}

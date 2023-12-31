package musicmania.backend.entities;

import jakarta.persistence.*;
import musicmania.backend.models.VerificationCodeType;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

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
    @Enumerated(EnumType.STRING)
    private VerificationCodeType type;

    public VerificationCode(long id, String user_email, String code, LocalDateTime generation_date, LocalDateTime expiration_date, Boolean used, VerificationCodeType type) {
        this.id = id;
        this.user_email = user_email;
        this.code = code;
        this.generation_date = generation_date;
        this.expiration_date = expiration_date;
        this.used = used;
        this.type = type;
    }

    public VerificationCode(String user_email, LocalDateTime generation_date, LocalDateTime expiration_date, VerificationCodeType type) {
        this.user_email = user_email;
        this.code = generateCode();
        this.generation_date = generation_date;
        this.expiration_date = expiration_date;
        this.type = type;
    }

    public VerificationCode() {

    }

    public String generateCode(){
        String CHARACTERS = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";
        int LENGTH = 6;

        Random random = new SecureRandom();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }

    public VerificationCodeType getType() {
        return type;
    }

    public void setType(VerificationCodeType type) {
        this.type = type;
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

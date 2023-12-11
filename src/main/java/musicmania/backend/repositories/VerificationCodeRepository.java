package musicmania.backend.repositories;

import musicmania.backend.entities.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    @Query("SELECT v FROM VerificationCode v WHERE v.code = :code AND v.user_email = :user_email AND v.expiration_date > :now")
    VerificationCode findValidCode(String code, String user_email, LocalDateTime now);
}

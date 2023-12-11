package musicmania.backend.services;

import jakarta.transaction.Transactional;
import musicmania.backend.entities.User;
import musicmania.backend.entities.VerificationCode;
import musicmania.backend.repositories.UserRepository;
import musicmania.backend.repositories.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final VerificationCodeRepository verificationCodeRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, VerificationCodeRepository verificationCodeRepository, BCryptPasswordEncoder passwordEncoder, S3Service s3Service, EmailService emailService) {
        this.userRepository = userRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3Service = s3Service;
        this.emailService = emailService;
    }

    public User getUser(String email, String password){
        User user = userRepository.findUserByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Email Not Found")
        );

        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Password Not Correct");

        return user;
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID Not Found")
        );
    }

    public User register(User user){
        if(userRepository.existsByEmail(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Email Already Taken");

        if(userRepository.existsByUsername(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Username Already Taken");

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    @Transactional
    public User updateUserScore(long userId, Integer newScore){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID Not Found")
        );

        if(newScore != null && !newScore.equals(user.getScore()))
            user.setScore(newScore);

        return user;
    }

    @Transactional
    public User updateUserPassword(long userId, String oldPassword, String newPassword){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID Not Found")
        );

        if(!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Old Password Not Correct");

        if(newPassword == null || newPassword.length() == 0)
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "New Password Not Acceptable");

        if(newPassword.equals(oldPassword))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "New Password Cannot Be Equal To Old Password");

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        return user;
    }

    public String getFinalName(long id, MultipartFile file){
        int lastDotIndex;
        String fileExtension = null;

        String fileOriginalFilename = file.getOriginalFilename();
        if(fileOriginalFilename != null){
            lastDotIndex = fileOriginalFilename.lastIndexOf(".");
            if(lastDotIndex != -1)
                fileExtension = fileOriginalFilename.substring(lastDotIndex);
        }

        return "users/profile-pictures/" + String.valueOf(id) + "_image" + fileExtension;
    }

    @Transactional
    public User updateUserProfilePicture(long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID Not Found")
        );

        String fileName = getFinalName(userId, file);

        String fileURL = s3Service.uploadFile("music-mania-s3-bucket", file, fileName);

        user.setProfilePictureURL(fileURL);

        return user;
    }

    public String generateCode(){
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int LENGTH = 6;

        Random random = new SecureRandom();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }

    @Transactional
    public VerificationCode sendForgotPasswordCode(String email){
        User user = userRepository.findUserByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Email Not Found")
        );

        LocalDateTime now = LocalDateTime.now();
        String code = generateCode();

        VerificationCode verificationCode = new VerificationCode(user.getEmail(), code, now, now.plusMinutes(10));
        verificationCodeRepository.save(verificationCode);

        emailService.sendEmail(user.getEmail(), "Password Reset Code", "You have requested a password reset. The generated code is " + code + ". If you did not request this code, you can simply ignore this message.");

        return verificationCode;
    }

    @Transactional
    public boolean verifyCode(String verification_code, String email){
        LocalDateTime now = LocalDateTime.now();

        VerificationCode verificationCode = verificationCodeRepository.findValidCode(verification_code, email, now);

        if(verificationCode == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Verification Code Not Correct");
        }

        verificationCode.setUsed(true);

        return true;
    }

    @Transactional
    public User setNewPassword(String email, String newPassword){

        User user = userRepository.findUserByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Email Not Found")
        );

        if(newPassword == null || newPassword.length() == 0)
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "New Password Not Acceptable");

        String newPasswordEncoded = passwordEncoder.encode(newPassword);

        if(newPasswordEncoded.equals(user.getPassword()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "New Password Cannot Be Equal To Old Password");

        user.setPassword(newPasswordEncoded);

        return user;
    }
}

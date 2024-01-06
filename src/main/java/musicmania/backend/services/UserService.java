package musicmania.backend.services;

import jakarta.transaction.Transactional;
import musicmania.backend.entities.User;
import musicmania.backend.entities.VerificationCode;
import musicmania.backend.handlers.*;
import musicmania.backend.models.VerificationCodeType;
import musicmania.backend.repositories.UserRepository;
import musicmania.backend.repositories.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

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
                () -> new UserEmailNotFoundException("User Email Not Found")
        );

        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new IncorrectPasswordException("Incorrect Password");

        return user;

    }

    public VerificationCode sendNewUserCode(User user){
        if(userRepository.existsByEmail(user.getEmail()))
            throw new EmailAlreadyTakenException("Email Already Taken");

        if(userRepository.existsByUsername(user.getUsername()))
            throw new UsernameAlreadyTakenException("Username Already Taken");

        return sendVerificationCode(user, VerificationCodeType.ACCOUNT_CREATION);
    }

    public VerificationCode sendForgotPasswordCode(String email){
        User user = userRepository.findUserByEmail(email).orElseThrow(
                () -> new UserEmailNotFoundException("Email Not Found")
        );

        return sendVerificationCode(user, VerificationCodeType.PASSWORD_RESET);
    }

    @Transactional
    public VerificationCode sendVerificationCode(User user, VerificationCodeType type){
        LocalDateTime now = LocalDateTime.now();

        VerificationCode verificationCode = new VerificationCode(user.getEmail(), now, now.plusMinutes(10), type);
        verificationCodeRepository.save(verificationCode);

        if(type == VerificationCodeType.PASSWORD_RESET)
            emailService.sendEmail(user.getEmail(), "Password Reset Code", "You have requested a password reset. The verification code is " + verificationCode.getCode() + ".\nIf you did not make this request, you can simply ignore this message.");
        else if(type == VerificationCodeType.ACCOUNT_CREATION)
            emailService.sendEmail(user.getEmail(), "New Account Code", "You have requested a new account. The verification code is " + verificationCode.getCode() + ".\nIf you did not make this request, you can simply ignore this message.");

        return verificationCode;
    }

    public User register(User user){
        if(userRepository.existsByEmail(user.getEmail()))
            throw new EmailAlreadyTakenException("Email Already Taken");

        if(userRepository.existsByUsername(user.getUsername()))
            throw new UsernameAlreadyTakenException("Username Already Taken");

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    @Transactional
    public User updateUserScore(long userId, Integer newScore){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User Not Found")
        );

        if(newScore != null && !newScore.equals(user.getScore()))
            user.setScore(newScore);

        return user;
    }

    @Transactional
    public User changeUserPassword(long userId, String oldPassword, String newPassword){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User Not Found")
        );

        if(!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new IncorrectPasswordException("Incorrect Password");

        if(newPassword == null || newPassword.length() == 0)
            throw new IncorrectPasswordException("New Password Not Acceptable");

        if(newPassword.equals(oldPassword))
            throw new IncorrectPasswordException("New Password Cannot Be Equal To Old Password");

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
                () -> new UserNotFoundException("User Not Found")
        );

        String fileName = getFinalName(userId, file);

        String fileURL = s3Service.uploadFile("music-mania-s3-bucket", file, fileName);

        user.setProfilePictureURL(fileURL);

        return user;
    }

    @Transactional
    public boolean verifyCode(String code, String email, VerificationCodeType type){
        LocalDateTime now = LocalDateTime.now();

        VerificationCode verificationCode = verificationCodeRepository.findValidCode(code, email, now, type);

        if(verificationCode == null)
            return false;
//            throw new IncorrectVerificationCodeException("Incorrect Verification Code");

        if(verificationCode.isUsed())
            return false;
//            throw new UsedVerificationCodeException("Used Verification Code");

        verificationCode.setUsed(true);
        verificationCodeRepository.save(verificationCode);

        return true;
    }

    public boolean verifyNewUserCode(String code, String email){
        return verifyCode(code, email, VerificationCodeType.ACCOUNT_CREATION);
    }

    public boolean verifyForgotPasswordCode(String code, String email){
        return verifyCode(code, email, VerificationCodeType.PASSWORD_RESET);
    }

    @Transactional
    public User setNewPassword(String email, String newPassword){
        User user = userRepository.findUserByEmail(email).orElseThrow(
                () -> new UserEmailNotFoundException("User Email Not Found")
        );

        if(newPassword == null || newPassword.length() == 0)
            throw new IncorrectPasswordException("New Password Not Acceptable");

        String newPasswordEncoded = passwordEncoder.encode(newPassword);

//        if(newPasswordEncoded.equals(user.getPassword()))
//        if(passwordEncoder.matches(newPassword, user.getPassword()))
//            throw new IncorrectPasswordException("New Password Cannot Be Equal To Old Password");

        user.setPassword(newPasswordEncoded);

        return user;
    }

    @Transactional
    public void deleteUser(long userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User Not Found")
        );
        userRepository.delete(user);
    }
}

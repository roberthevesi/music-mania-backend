package musicmania.backend.services;

import jakarta.transaction.Transactional;
import musicmania.backend.entities.User;
import musicmania.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByCredentials(String email, String password){
        Optional<User> userOptional = userRepository.findUserByEmail(email);

        if(userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getPassword()))
            return userOptional.get();

        return null;
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

    // + recoverPassword endpoint
}

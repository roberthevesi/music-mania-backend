package musicmania.backend.services;

import musicmania.backend.entities.User;
import musicmania.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String email, String password){
        return null;
    }

    public void registerUser(User user){

    }

    public void updateUserScore(UUID id, Integer newScore){

    }

    public void changeUserPassword(UUID id, String oldPassword, String newPassword){

    }

    public void changeUserProfilePicture(UUID id, String newProfilePictureURL){

    }

    // + recoverPassword endpoint
}

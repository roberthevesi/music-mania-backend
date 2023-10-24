package musicmania.backend.controllers;

import musicmania.backend.entities.User;
import musicmania.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("getUser")
    public User getUser(@RequestParam String email, @RequestParam String password){
        return null;
    }

    @PostMapping("registerUser")
    public void registerUser(@RequestBody User user){

    }

    @PutMapping("updateUserScore")
    public void updateUserScore(@RequestParam UUID id, @RequestParam Integer newScore){

    }

    @PutMapping("changeUserPassword")
    public void changeUserPassword(@RequestParam UUID id, @RequestParam String oldPassword, @RequestParam String newPassword){

    }

    @PutMapping("changeUserProfilePicture")
    public void changeUserProfilePicture(@RequestParam UUID id, @RequestParam String newProfilePictureURL){

    }

    // + recoverPassword endpoint
}

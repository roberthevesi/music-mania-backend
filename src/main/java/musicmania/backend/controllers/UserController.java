package musicmania.backend.controllers;

import musicmania.backend.entities.User;
import musicmania.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // get user for login
    @GetMapping("/getUserByCredentials")
    public ResponseEntity<User> getUserByCredentials(@RequestParam String email, @RequestParam String password){
        User userResponse = userService.getUserByCredentials(email, password);

        if(userResponse == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");

        return ResponseEntity.ok(userResponse);
    }

    // register new user
    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody User user){
        return ResponseEntity.ok(userService.register(user));
    }

    // update the user's score
    @PutMapping("/updateUserScore")
    public ResponseEntity<User> updateUserScore(@RequestParam long userId, @RequestParam Integer newScore){
        return ResponseEntity.ok(userService.updateUserScore(userId, newScore));
    }

    // update the user's password
    @PutMapping("/updateUserPassword")
    public ResponseEntity<User> changeUserPassword(@RequestParam long userId, @RequestParam String oldPassword, @RequestParam String newPassword){
        return ResponseEntity.ok(userService.updateUserPassword(userId, oldPassword, newPassword));
    }

    @PostMapping("/updateUserProfilePicture")
    public ResponseEntity<?> updateUserProfilePicture(@RequestParam long userId, @RequestParam("file") MultipartFile file) {
//        User updatedUser = userService.updateUserProfilePicture(userId, file);
//        return ResponseEntity.ok("Profile picture updated successfully. New URL: " + updatedUser.getProfilePictureURL());
        return null;
    }

    // + recoverPassword endpoint
}

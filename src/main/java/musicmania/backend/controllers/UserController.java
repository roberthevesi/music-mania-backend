package musicmania.backend.controllers;

import musicmania.backend.entities.User;
import musicmania.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // get user for login
    @GetMapping("/get-user")
    public ResponseEntity<User> getUser(@RequestParam String email, @RequestParam String password){
        User userResponse = userService.getUser(email, password);

        if(userResponse == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");

        return ResponseEntity.ok(userResponse);
    }

    // POST check if new user confirms email
    @PostMapping("/send-new-user-code")
    public ResponseEntity<?> sendNewUserCode(@RequestBody User user){
        return ResponseEntity.ok(userService.sendNewUserCode(user));
    }

    @PutMapping("/verify-new-user-code")
    public ResponseEntity<?> verifyNewUserCode(@RequestParam String verification_code, @RequestParam String email){
        return ResponseEntity.ok(userService.verifyNewUserCode(verification_code, email));
    }

    // register new user
    @PostMapping("/add-user")
    public ResponseEntity<User> addUser(@RequestBody User user){
        return ResponseEntity.ok(userService.register(user));
    }

    // update the user's score
    @PutMapping("/update-user-score")
    public ResponseEntity<User> updateUserScore(@RequestParam long userId, @RequestParam Integer newScore){
        return ResponseEntity.ok(userService.updateUserScore(userId, newScore));
    }

    // update the user's password
    @PutMapping("/update-user-password")
    public ResponseEntity<User> updateUserPassword(@RequestParam long userId, @RequestParam String oldPassword, @RequestParam String newPassword){
        return ResponseEntity.ok(userService.updateUserPassword(userId, oldPassword, newPassword));
    }

    // update the user's profile picture
    @PutMapping("/update-user-profile-picture")
    public ResponseEntity<?> updateUserProfilePicture(@RequestPart("userId") long userId, @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(userService.updateUserProfilePicture(userId, file));
    }

    // send forgot password verification code
    @PostMapping("/send-forgot-password-code")
    public ResponseEntity<?> sendForgotPasswordCode(@RequestParam String email){
        return ResponseEntity.ok(userService.sendForgotPasswordCode(email));
    }

    @PutMapping("/verify-forgot-password-code")
    public ResponseEntity<?> verifyForgotPasswordCode(@RequestParam String verification_code, @RequestParam String email){
        return ResponseEntity.ok(userService.verifyForgotPasswordCode(verification_code, email));
    }

    @PutMapping("/set-new-password")
    public ResponseEntity<?> setNewPassword(@RequestParam String email, @RequestParam String password){
        return ResponseEntity.ok(userService.setNewPassword(email, password));
    }
}

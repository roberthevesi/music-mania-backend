package musicmania.backend.controllers;

import musicmania.backend.entities.User;
import musicmania.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        return ResponseEntity.ok(userService.getUser(email, password));
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

    // change user's password (when old password is known)
    @PutMapping("/change-user-password")
    public ResponseEntity<User> changeUserPassword(@RequestParam long userId, @RequestParam String oldPassword, @RequestParam String newPassword){
        return ResponseEntity.ok(userService.changeUserPassword(userId, oldPassword, newPassword));
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

    // verify the forgot password verification code
    @PutMapping("/verify-forgot-password-code")
    public ResponseEntity<?> verifyForgotPasswordCode(@RequestParam String verification_code, @RequestParam String email){
        return ResponseEntity.ok(userService.verifyForgotPasswordCode(verification_code, email));
    }

    // set new password for users who forgot their password (when old password is unknown)
    @PutMapping("/set-new-password")
    public ResponseEntity<?> setNewPassword(@RequestParam String email, @RequestParam String password){
        return ResponseEntity.ok(userService.setNewPassword(email, password));
    }
}

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
    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestParam String email, @RequestParam String password){
        User userResponse = userService.getUser(email, password);

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
    public ResponseEntity<User> updateUserPassword(@RequestParam long userId, @RequestParam String oldPassword, @RequestParam String newPassword){
        return ResponseEntity.ok(userService.updateUserPassword(userId, oldPassword, newPassword));
    }

    // update the user's profile picture
    @PostMapping("/updateUserProfilePicture")
    public ResponseEntity<?> updateUserProfilePicture(@RequestPart("userId") long userId, @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(userService.updateUserProfilePicture(userId, file));
    }

    // send forgot password verification code
    @PutMapping("/sendForgotPasswordCode")
    public ResponseEntity<?> sendForgotPasswordCode(@RequestParam String email){
        return ResponseEntity.ok(userService.sendForgotPasswordCode(email));
    }

    @PutMapping("/verifyCode")
    public ResponseEntity<?> verifyCode(@RequestParam String verification_code, @RequestParam String email){
        return ResponseEntity.ok(userService.verifyCode(verification_code, email));
    }

    @PutMapping("/setNewPassword")
    public ResponseEntity<?> setNewPassword(@RequestParam String email, @RequestParam String password){
        return ResponseEntity.ok(userService.setNewPassword(email, password));
    }
}

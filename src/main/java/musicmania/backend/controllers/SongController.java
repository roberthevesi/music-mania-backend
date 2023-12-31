package musicmania.backend.controllers;

import musicmania.backend.entities.Song;
import musicmania.backend.services.S3Service;
import musicmania.backend.services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/songs")
public class SongController {
    private final SongService songService;
    private final S3Service s3Service;

    @Autowired
    public SongController(SongService songService, S3Service s3Service) {
        this.songService = songService;
        this.s3Service = s3Service;
    }

    @PutMapping("/add-song")
    public ResponseEntity<Song> addSong(@RequestPart("song") Song song, @RequestPart("file") MultipartFile file, @RequestPart("image") MultipartFile image) throws IOException {
        return ResponseEntity.ok(songService.addSong(song, file, image));
    }

    // currently not working due to access denied
    @DeleteMapping("/delete-song")
    public ResponseEntity<Song> deleteSong(@RequestParam long id){
        return ResponseEntity.ok(songService.deleteSong(id));
    }

    @GetMapping("/get-songs")
    public ResponseEntity<List<Song>> getFourSongs(@RequestParam long user_id){
        return ResponseEntity.ok(songService.getFourSongs(user_id));
    }

    @GetMapping("/verify-song-guess")
    public boolean verifySongGuess(@RequestBody Song song){
        return false;
    }
}

package musicmania.backend.controllers;

import musicmania.backend.entities.Song;
import musicmania.backend.services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/songs")
public class SongController {
    private final SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("getSongs")
    public List<Song> getFourSongs(){
        return null;
    }

    @GetMapping("verifySongGuess")
    public boolean verifySongGuess(@RequestBody Song song){
        return false;
    }
}

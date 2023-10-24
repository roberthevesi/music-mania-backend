package musicmania.backend.services;

import musicmania.backend.entities.Song;
import musicmania.backend.repositories.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class SongService {
    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Song getFourSongs(){
        return null;
    }

    public boolean verifySongGuess(Song song){
        return false;
    }
}

package musicmania.backend.services;

import jakarta.transaction.Transactional;
import musicmania.backend.entities.Song;
import musicmania.backend.repositories.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SongService {
    private final SongRepository songRepository;
    private final S3Service s3Service;
    private final UserSongHistoryService userSongHistoryService;

    @Autowired
    public SongService(SongRepository songRepository, S3Service s3Service, UserSongHistoryService userSongHistoryService) {
        this.songRepository = songRepository;
        this.s3Service = s3Service;
        this.userSongHistoryService = userSongHistoryService;
    }

    public String getFinalName(long id, String type, MultipartFile file){
        int lastDotIndex;
        String fileExtension = null;

        String fileOriginalFilename = file.getOriginalFilename();
        if(fileOriginalFilename != null){
            lastDotIndex = fileOriginalFilename.lastIndexOf(".");
            if(lastDotIndex != -1)
                fileExtension = fileOriginalFilename.substring(lastDotIndex);
        }

        return String.valueOf(id) + "_" + type + fileExtension;
    }

    @Transactional
    public Song addSong(Song song, MultipartFile file, MultipartFile image) throws IOException {
        song = songRepository.save(song);

        String fileFinalName = getFinalName(song.getId(), "file", file);
        String imageFinalName = getFinalName(song.getId(), "image", image);

        String fileURL = s3Service.uploadFile("music-mania-songs-bucket", file, fileFinalName);
        String imageURL = s3Service.uploadFile("music-mania-songs-bucket", image, imageFinalName);

        song.setFileURL(fileURL);
        song.setImageURL(imageURL);

        songRepository.save(song);

        return song;
    }

    public List<Song> getFourSongs(long user_id){
        LocalDateTime now = LocalDateTime.now();
        List<Song> songs = songRepository.getFourSongs(user_id);

        for(Song song : songs){
            userSongHistoryService.saveUserSongHistory(user_id, song.getId(), now);
        }

        return songs;
    }

    public boolean verifySongGuess(Song song){
        return false;
    }
}

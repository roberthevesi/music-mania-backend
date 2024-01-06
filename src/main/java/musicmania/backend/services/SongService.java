package musicmania.backend.services;

import jakarta.transaction.Transactional;
import musicmania.backend.entities.Song;
import musicmania.backend.repositories.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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

        return "songs/" + String.valueOf(id) + "_" + type + fileExtension;
    }

    @Transactional
    public Song addSong(Song song, MultipartFile file, MultipartFile image) throws IOException {
        song = songRepository.save(song);

        String fileFinalName = getFinalName(song.getId(), "file", file);
        String imageFinalName = getFinalName(song.getId(), "image", image);

        String fileURL = s3Service.uploadFile("music-mania-s3-bucket", file, fileFinalName);
        String imageURL = s3Service.uploadFile("music-mania-s3-bucket", image, imageFinalName);

        song.setFileURL(fileURL);
        song.setImageURL(imageURL);

        songRepository.save(song);

        return song;
    }

    public String getFileName(String url){
        String baseURL = "https://music-mania-s3-bucket.s3.eu-west-3.amazonaws.com";
        return url.substring(baseURL.length());
    }

    public Song deleteSong(long id){
        Song song = songRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Song ID Not Found")
        );
//        songRepository.delete(song);

        String fileName = getFileName(song.getFileURL());
        String imageName = getFileName(song.getImageURL());

        s3Service.deleteFile("music-mania-s3-bucket", fileName);
        s3Service.deleteFile("music-mania-s3-bucket", imageName);

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
}

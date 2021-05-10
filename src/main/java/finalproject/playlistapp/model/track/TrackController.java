package finalproject.playlistapp.model.track;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping(path = "api/track")
@AllArgsConstructor
public class TrackController {

    private final TrackService trackService;

    @GetMapping(path = "/five")
    public ResponseEntity<List<Track>> getFiveTracks() {
        List<Track> tracks = trackService.getFiveTracks();
        return new ResponseEntity<>(tracks, HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Track>> getTracks() {
        List<Track> tracks = trackService.getTracks();
        return new ResponseEntity<>(tracks, HttpStatus.OK);
    }

    @GetMapping(path = "/play/{trackId}")
    public ResponseEntity<InputStreamResource> playTrackById(@PathVariable("trackId") Long id) throws FileNotFoundException {
        String filePath = trackService.playTrackById(id).get().getPath();

        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(filePath);
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Ranges", "bytes");
        headers.set("Content-Type", "audio/mpeg");
        headers.set("Content-Range", "bytes 50-30000000");
        headers.set("Content-Length", String.valueOf(file.length()));
        return new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK);
    }

    @PutMapping(path = "add-track-to-playlist/{playlistId}")
    public ResponseEntity<?> removeTrack(@RequestBody Track track, @PathVariable Long playlistId) {
        trackService.addTrackToPlaylist(track, playlistId);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}

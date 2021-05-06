package finalproject.playlistapp.model.track;

import finalproject.playlistapp.model.playlist.Playlist;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Track> playTrackById(@PathVariable("trackId") Long id) {
        Track track = trackService.playTrackById(id).get();
        return new ResponseEntity<>(track, HttpStatus.OK);
    }

    @PutMapping(path = "add-track-to-playlist/{playlistId}")
    public ResponseEntity<?> removeTrack(@RequestBody Track track, @PathVariable Long playlistId) {
        trackService.addTrackToPlaylist(track, playlistId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

package finalproject.playlistapp.model.playlist;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/playlist")
@AllArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping(path = "user/{userId}")
    public ResponseEntity<List<Playlist>> getUserPlaylists(@PathVariable Long userId) {
        List<Playlist> playlists = playlistService.getPlaylists(userId);
        return new ResponseEntity<>(playlists, HttpStatus.OK);
    }

    @PostMapping(path = "post/{userId}")
    public void postPlaylist(@RequestBody Playlist playlist, @PathVariable Long userId) {
        playlistService.postPlaylist(playlist, userId);
    }

    @DeleteMapping(path = "delete/{playlistId}")
    public ResponseEntity<?> deletePlaylist(@PathVariable("playlistId") Long id) {
        playlistService.deletePlaylist(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "put")
    public ResponseEntity<?> putPlaylist(@RequestBody Playlist playlist) {
        playlistService.putUser(playlist);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "remove-track/{trackId}")
    public ResponseEntity<?> removeTrack(@RequestBody Playlist playlist, @PathVariable Long trackId) {
        System.out.println(" asd" + playlist.getId() + " ");
        playlistService.removeTrack(playlist, trackId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

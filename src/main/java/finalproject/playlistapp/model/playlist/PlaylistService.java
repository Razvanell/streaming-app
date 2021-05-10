package finalproject.playlistapp.model.playlist;

import finalproject.playlistapp.model.track.Track;
import finalproject.playlistapp.model.track.TrackRepository;
import finalproject.playlistapp.model.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;

    public List<Playlist> getPlaylists(Long userId) {
        return playlistRepository.findAllByUserId(userId);
    }

    public void postPlaylist(Playlist playlist, Long userId) {
        if(playlistRepository.findByName(playlist.getName()).isPresent()) {
            throw new IllegalStateException("A playlist with this name already exists");
        }
        playlist.setUser(userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User does not exist")));
        playlistRepository.save(playlist);
    }

    @Transactional
    public void deletePlaylist(Long playlistId) {
        if(!playlistRepository.existsById(playlistId)) {
            throw new IllegalStateException("Playlist does not exist in the database");
        }
        System.out.println("what the fuck");
        playlistRepository.deleteById(playlistId);
    }

    @Transactional
    public void putUser(Playlist playlist) {
        Playlist oldPlaylist = playlistRepository.findById(playlist.getId()).orElseThrow(() -> new IllegalStateException("Playlist does not exist"));

        if(playlist.getName() != null) oldPlaylist.setName(playlist.getName());

    }

    @Transactional
    public void removeTrack(Playlist playlist, Long trackId) {
        Playlist oldPlaylist = playlistRepository.findById(playlist.getId()).orElseThrow(() -> new IllegalStateException("Playlist does not exist"));
        Track track = trackRepository.findById(trackId).orElseThrow(() -> new IllegalStateException("Track does not exist in playlist"));
        oldPlaylist.getTracks().remove(track);
    }

}

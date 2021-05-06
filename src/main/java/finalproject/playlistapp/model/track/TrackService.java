package finalproject.playlistapp.model.track;

import finalproject.playlistapp.model.playlist.Playlist;
import finalproject.playlistapp.model.playlist.PlaylistRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;
    private final PlaylistRepository playlistRepository;

    public List<Track> getTracks() {
        return trackRepository.findAll();
    }

    public List<Track> getFiveTracks() {
        return trackRepository.findTop5ByOrderByArtistAsc();
    }

    public Optional<Track> playTrackById(Long id) {
        if (!trackRepository.existsById(id)) {
            throw new IllegalStateException("No track with id: " + id + " exists in the database");
        }
        return trackRepository.findById(id);
    }

    @Transactional
    public void addTrackToPlaylist(Track track, Long playlistId) {
        Playlist oldPlaylist = playlistRepository.findById(playlistId).orElseThrow(() -> new IllegalStateException("Playlist does not exist"));
        Track trackToBeAdded = trackRepository.findById(track.getId()).orElseThrow(() -> new IllegalStateException("Track does not exist"));
        oldPlaylist.getTracks().add(trackToBeAdded);
    }

}

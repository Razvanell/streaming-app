package finalproject.playlistapp.model.track;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;

    public List<Track> getTracks() {
        return trackRepository.findAll();
    }

    public List<Track> getFiveTracks() {
        return trackRepository.findTop5ByOrderByArtistAsc();
    }

    public Track randomTrack() {
        Long qty = trackRepository.count();
        int idx = (int)(Math.random() * qty);
        Page<Track> questionPage = trackRepository.findAll(PageRequest.of(idx, 1));
        Track track = null;
        if (questionPage.hasContent()) {
            track = questionPage.getContent().get(0);
        }
        return track;
    }

    public Optional<Track> playTrackById(Long id) {
        if (!trackRepository.existsById(id)) {
            throw new IllegalStateException("No track with id: " + id + " exists in the database");
        }
        return trackRepository.findById(id);
    }

}

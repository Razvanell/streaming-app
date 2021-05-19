package finalproject.playlistapp.model.track;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TrackService.class})
@ExtendWith(SpringExtension.class)
public class TrackServiceTest {
    @MockBean
    private TrackRepository trackRepository;

    @Autowired
    private TrackService trackService;

    @Test
    public void testGetTracks() {
        ArrayList<Track> trackList = new ArrayList<>();
        when(this.trackRepository.findAll()).thenReturn(trackList);
        List<Track> actualTracks = this.trackService.getTracks();
        assertSame(trackList, actualTracks);
        assertTrue(actualTracks.isEmpty());
        verify(this.trackRepository).findAll();
    }

    @Test
    public void testGetFiveTracks() {
        ArrayList<Track> trackList = new ArrayList<>();
        when(this.trackRepository.findTop5ByOrderByArtistAsc()).thenReturn(trackList);
        List<Track> actualFiveTracks = this.trackService.getFiveTracks();
        assertSame(trackList, actualFiveTracks);
        assertTrue(actualFiveTracks.isEmpty());
        verify(this.trackRepository).findTop5ByOrderByArtistAsc();
    }

    @Test
    public void testRandomTrack() {
        when(this.trackRepository.findAll((org.springframework.data.domain.Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(this.trackRepository.count()).thenReturn(3L);
        assertNull(this.trackService.randomTrack());
        verify(this.trackRepository).count();
        verify(this.trackRepository).findAll((org.springframework.data.domain.Pageable) any());
    }

    @Test
    public void testRandomTrack2() {
        Track track = new Track();
        track.setId(123L);
        track.setPlaylists(new HashSet<>());
        track.setTitle("Dr");
        track.setArtist("Artist");
        track.setPath("Path");

        ArrayList<Track> trackList = new ArrayList<>();
        trackList.add(track);
        PageImpl<Track> pageImpl = new PageImpl<>(trackList);
        when(this.trackRepository.findAll((org.springframework.data.domain.Pageable) any())).thenReturn(pageImpl);
        when(this.trackRepository.count()).thenReturn(3L);
        assertSame(track, this.trackService.randomTrack());
        verify(this.trackRepository).count();
        verify(this.trackRepository).findAll((org.springframework.data.domain.Pageable) any());
    }

    @Test
    public void testRandomTrack3() {
        when(this.trackRepository.findAll((org.springframework.data.domain.Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(this.trackRepository.count()).thenReturn(0L);
        assertNull(this.trackService.randomTrack());
        verify(this.trackRepository).count();
        verify(this.trackRepository).findAll((org.springframework.data.domain.Pageable) any());
    }

    @Test
    public void testPlayTrackById() {
        Track track = new Track();
        track.setId(123L);
        track.setPlaylists(new HashSet<>());
        track.setTitle("Dr");
        track.setArtist("Artist");
        track.setPath("Path");
        Optional<Track> ofResult = Optional.of(track);
        when(this.trackRepository.findById(any())).thenReturn(ofResult);
        when(this.trackRepository.existsById(any())).thenReturn(true);
        Optional<Track> actualPlayTrackByIdResult = this.trackService.playTrackById(123L);
        assertSame(ofResult, actualPlayTrackByIdResult);
        assertTrue(actualPlayTrackByIdResult.isPresent());
        verify(this.trackRepository).findById(any());
        verify(this.trackRepository).existsById(any());
    }

    @Test
    public void testPlayTrackById2() {
        Track track = new Track();
        track.setId(123L);
        track.setPlaylists(new HashSet<>());
        track.setTitle("Dr");
        track.setArtist("Artist");
        track.setPath("Path");
        Optional<Track> ofResult = Optional.of(track);
        when(this.trackRepository.findById(any())).thenReturn(ofResult);
        when(this.trackRepository.existsById(any())).thenReturn(false);
        assertThrows(IllegalStateException.class, () -> this.trackService.playTrackById(123L));
        verify(this.trackRepository).existsById(any());
    }


}


package finalproject.playlistapp.model.track;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TrackTest {
    @Test
    public void testHashCode() {
        assertEquals(923521, (new Track()).hashCode());
    }
}


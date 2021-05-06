package finalproject.playlistapp.model.track;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

    List<Track> findTop5ByOrderByArtistAsc();

    @Query("SELECT track FROM Track track WHERE track.title = ?1")
    Optional<Track> findByTitle(String title);

}

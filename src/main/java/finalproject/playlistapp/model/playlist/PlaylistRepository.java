package finalproject.playlistapp.model.playlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    @Query("SELECT playlist FROM Playlist playlist WHERE playlist.name = ?1")
    Optional<Playlist> findByName(String name);

    @Query("SELECT playlist FROM Playlist playlist WHERE playlist.user.id = :id")
    List<Playlist> findAllByUserId(Long id);
}

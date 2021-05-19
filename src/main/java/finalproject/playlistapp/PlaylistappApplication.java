package finalproject.playlistapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //(exclude = {SecurityAutoConfiguration.class})
public class PlaylistappApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaylistappApplication.class, args);
    }

}

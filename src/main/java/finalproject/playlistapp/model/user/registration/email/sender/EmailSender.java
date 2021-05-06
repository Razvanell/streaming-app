package finalproject.playlistapp.model.user.registration.email.sender;

public interface EmailSender {
    void send(String to, String email);
    //using maildev to recieve emails
}

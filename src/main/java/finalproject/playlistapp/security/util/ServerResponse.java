package finalproject.playlistapp.security.util;

public class ServerResponse {
    private int status; // 200, 400, 500
    private String message;
    private String error;
    private Object result;

    public ServerResponse(int status, String error){
        this(status, "", error, null);
    }

    public ServerResponse(int status, String message, Object result){
        this.status = status;
        this.message = message;
        this.error = " ";
        this.result = result;
    }

    public ServerResponse(int status, String message, String error, Object result) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
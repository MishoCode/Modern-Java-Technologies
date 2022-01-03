package bg.sofia.uni.fmi.mjt.twitch.user;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}

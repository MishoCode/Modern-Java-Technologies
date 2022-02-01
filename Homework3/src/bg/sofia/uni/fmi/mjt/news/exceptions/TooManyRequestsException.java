package bg.sofia.uni.fmi.mjt.news.exceptions;

public class TooManyRequestsException extends NewsFeedException {
    public TooManyRequestsException(String message) {
        super(message);
    }
}

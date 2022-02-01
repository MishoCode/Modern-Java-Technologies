package bg.sofia.uni.fmi.mjt.news.exceptions;

public class WrongAPIKeyException extends NewsFeedException {
    public WrongAPIKeyException(String message) {
        super(message);
    }
}

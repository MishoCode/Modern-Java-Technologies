package bg.sofia.uni.fmi.mjt.news.exceptions;

public class UnacceptableRequestException extends NewsFeedException {
    public UnacceptableRequestException(String message) {
        super(message);
    }
}

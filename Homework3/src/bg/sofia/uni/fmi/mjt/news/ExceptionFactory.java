package bg.sofia.uni.fmi.mjt.news;

import bg.sofia.uni.fmi.mjt.news.exceptions.NewsFeedException;
import bg.sofia.uni.fmi.mjt.news.exceptions.ServerException;
import bg.sofia.uni.fmi.mjt.news.exceptions.TooManyRequestsException;
import bg.sofia.uni.fmi.mjt.news.exceptions.UnacceptableRequestException;
import bg.sofia.uni.fmi.mjt.news.exceptions.WrongAPIKeyException;

public class ExceptionFactory {
    public static final int BAD_REQUEST_CODE = 400;
    public static final int UNAUTHORIZED_CODE = 401;
    public static final int TOO_MANY_REQUESTS_CODE = 429;
    public static final int SERVER_ERROR_CODE = 500;

    public static NewsFeedException of(int statusCode) {

        if (statusCode == UNAUTHORIZED_CODE) {
            return new WrongAPIKeyException("Your API key is not valid.");
        }

        if (statusCode == BAD_REQUEST_CODE) {
            return new UnacceptableRequestException("The request is no acceptable.");
        }

        if (statusCode == TOO_MANY_REQUESTS_CODE) {
            return new TooManyRequestsException("No more requests are allowed.");
        }

        if (statusCode == SERVER_ERROR_CODE) {
            return new ServerException("A problem with the server has occurred.");
        }

        return new NewsFeedException("Unexpected response code from the news feed service");
    }
}

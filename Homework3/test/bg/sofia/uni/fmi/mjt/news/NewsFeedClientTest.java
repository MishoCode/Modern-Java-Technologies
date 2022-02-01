package bg.sofia.uni.fmi.mjt.news;

import bg.sofia.uni.fmi.mjt.news.dto.Article;
import bg.sofia.uni.fmi.mjt.news.dto.NewsCollection;
import bg.sofia.uni.fmi.mjt.news.dto.Source;
import bg.sofia.uni.fmi.mjt.news.exceptions.NewsFeedException;
import bg.sofia.uni.fmi.mjt.news.exceptions.ServerException;
import bg.sofia.uni.fmi.mjt.news.exceptions.TooManyRequestsException;
import bg.sofia.uni.fmi.mjt.news.exceptions.UnacceptableRequestException;
import bg.sofia.uni.fmi.mjt.news.exceptions.WrongAPIKeyException;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewsFeedClientTest {
    private static final PartialQuery QUERY = PartialQuery.builder(List.of("key1", "key2", "key3"))
        .addCountry("bg")
        .addCategory("science")
        .build();

    private static NewsCollection news;
    private static String newsJson;

    @Mock
    private HttpClient newsFeedHttpClientMock;

    @Mock
    private HttpResponse<String> newsFeedHttpResponseMock;

    private NewsFeedClient newsFeedClient;

    @BeforeAll
    public static void setUpClass() {
        Source source = new Source("id1", "name");
        Article article = new Article(source, "author", "title", "description",
            "url", "image", "publishedAt", "content");
        news = new NewsCollection(50, new Article[] {article});
        newsJson = new Gson().toJson(news);
    }

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        Mockito.lenient().when(
                newsFeedHttpClientMock.send(Mockito.any(HttpRequest.class),
                    Mockito.<HttpResponse.BodyHandler<String>>any()))
            .thenReturn(newsFeedHttpResponseMock);

        newsFeedClient = new NewsFeedClient(newsFeedHttpClientMock);
    }

    @Test
    public void testGetNewsValidRequest() throws NewsFeedException {
        String assertMessage = "The returned collection of news was not correct.";

        when(newsFeedHttpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(newsFeedHttpResponseMock.body()).thenReturn(newsJson);

        List<NewsCollection> result = newsFeedClient.getNews(QUERY);
        assertEquals(result.get(0), news, assertMessage);
    }

    @Test
    public void testGetNewsWrongAPIKeyException() {
        String assertMessage = "Your API key is wrong.";

        when(newsFeedHttpResponseMock.statusCode()).thenReturn(ExceptionFactory.UNAUTHORIZED_CODE);
        PartialQuery query = PartialQuery.builder(List.of("key1", "key2", "key3")).build();

        assertThrows(WrongAPIKeyException.class,
            () -> newsFeedClient.getNews(query), assertMessage);
    }

    @Test
    public void testGetNewsBadRequestException() {
        String assertMessage = "Incorrect request.";

        when(newsFeedHttpResponseMock.statusCode()).thenReturn(ExceptionFactory.BAD_REQUEST_CODE);

        assertThrows(UnacceptableRequestException.class,
            () -> newsFeedClient.getNews(QUERY), assertMessage);
    }

    @Test
    public void testGetNewsTooManyRequestsException() {
        String assertMessage = "You have made too many requests to the server.";

        when(newsFeedHttpResponseMock.statusCode())
            .thenReturn(ExceptionFactory.TOO_MANY_REQUESTS_CODE);

        assertThrows(TooManyRequestsException.class,
            () -> newsFeedClient.getNews(QUERY), assertMessage);
    }

    @Test
    public void testGetNewsServerException() {
        String assertMessage = "A problem in the server has occurred.";

        when(newsFeedHttpResponseMock.statusCode()).thenReturn(ExceptionFactory.SERVER_ERROR_CODE);

        assertThrows(ServerException.class,
            () -> newsFeedClient.getNews(QUERY), assertMessage);
    }

    @Test
    public void testGetNewsUnknownStatusCode() {
        String assertMessage = "The server responded with an unknown status code.";

        when(newsFeedHttpResponseMock.statusCode()).thenReturn(0);

        assertThrows(NewsFeedException.class,
            () -> newsFeedClient.getNews(QUERY), assertMessage);
    }

    @Test
    public void testGetNewsFeedHttpClientIOExceptionIsWrapped() throws Exception {
        String assetMessage =
            "NewsFeedClientException should properly wrap the causing IOException or InterruptedException";

        IOException expectedExc = new IOException();
        when(newsFeedHttpClientMock.send(Mockito.any(HttpRequest.class),
            ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
            .thenThrow(expectedExc);

        try {
            newsFeedClient.getNews(QUERY);
        } catch (Exception actualExc) {
            assertEquals(expectedExc, actualExc.getCause(), assetMessage);
        }
    }
}

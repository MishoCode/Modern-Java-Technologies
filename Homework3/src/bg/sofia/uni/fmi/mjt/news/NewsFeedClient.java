package bg.sofia.uni.fmi.mjt.news;

import bg.sofia.uni.fmi.mjt.news.dto.NewsCollection;
import bg.sofia.uni.fmi.mjt.news.exceptions.NewsFeedException;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.List;

public class NewsFeedClient {
    private static final String API_ENDPOINT_SCHEMA = "http";
    private static final String API_ENDPOINT_HOST = "newsapi.org";
    private static final String API_ENDPOINT_PATH = "/v2/top-headlines";
    private static final String API_ENDPOINT_QUERY = "%s&page=%d&pageSize=%d&apiKey=%s";
    private static final String API_KEY = "Your API key";


    private static final int MAX_PAGE_SIZE = 50;
    private static final int MAX_PAGES_COUNT = 3;

    private static final Gson GSON = new Gson();

    private final HttpClient newsFeedHttpClient;
    private final String apiKey;

    public NewsFeedClient(HttpClient newsFeedHttpClient) {
        this(newsFeedHttpClient, API_KEY);
    }

    public NewsFeedClient(HttpClient newsFeedHttpClient, String apiKey) {
        this.newsFeedHttpClient = newsFeedHttpClient;
        this.apiKey = apiKey;
    }

    public List<NewsCollection> getNews(PartialQuery query) throws NewsFeedException {
        HttpResponse<String> response;
        List<NewsCollection> allNews = new LinkedList<>();
        int expectedPages = 1;

        try {
            for (int i = 1; i <= MAX_PAGES_COUNT; i++) {
                String queryString = String.format(
                    API_ENDPOINT_QUERY, query.toString(), i, MAX_PAGE_SIZE, API_KEY);
                URI uri = new URI(
                    API_ENDPOINT_SCHEMA,
                    API_ENDPOINT_HOST,
                    API_ENDPOINT_PATH,
                    queryString,
                    null);
                HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

                if (i > expectedPages) {
                    break;
                }

                response = newsFeedHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                    NewsCollection newsCollection = GSON.fromJson(response.body(), NewsCollection.class);
                    expectedPages = newsCollection.getTotalResults() / MAX_PAGE_SIZE + 1;
                    allNews.add(newsCollection);
                } else {
                    throw ExceptionFactory.of(response.statusCode());
                }
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new NewsFeedException("Could not retrieve news", e);
        }

        return allNews;
    }
}

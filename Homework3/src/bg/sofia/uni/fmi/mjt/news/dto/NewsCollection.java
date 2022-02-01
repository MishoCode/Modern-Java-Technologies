package bg.sofia.uni.fmi.mjt.news.dto;

import java.util.Arrays;

public class NewsCollection {
    private final int totalResults;
    private final Article[] articles;

    public NewsCollection(int totalResults, Article[] articles) {
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public int getTotalResults() {
        return totalResults;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsCollection newsCollection = (NewsCollection) o;
        return Arrays.equals(articles, newsCollection.articles);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(articles);
    }
}

package bg.sofia.uni.fmi.mjt.news;

import java.util.List;

public class PartialQuery {
    private final List<String> keywords;
    private final String country;
    private final String category;

    private PartialQuery(PartialQueryBuilder builder) {
        this.keywords = builder.keywords;
        this.country = builder.country;
        this.category = builder.category;
    }

    public static PartialQueryBuilder builder(List<String> keywords) {
        return new PartialQueryBuilder(keywords);
    }

    @Override
    public String toString() {
        StringBuilder joinKeywords = new StringBuilder();
        joinKeywords.append("q=");
        joinKeywords.append(String.join("+", keywords));
        if (country != null) {
            joinKeywords.append("&country=").append(country);
        }

        if (category != null) {
            joinKeywords.append("&category=").append(category);
        }

        return joinKeywords.toString();
    }

    public static class PartialQueryBuilder {
        private final List<String> keywords;
        private String country;
        private String category;

        private PartialQueryBuilder(List<String> keywords) {
            this.keywords = keywords;
        }

        public PartialQueryBuilder addCountry(String country) {
            this.country = country;
            return this;
        }

        public PartialQueryBuilder addCategory(String category) {
            this.category = category;
            return this;
        }

        public PartialQuery build() {
            return new PartialQuery(this);
        }
    }
}

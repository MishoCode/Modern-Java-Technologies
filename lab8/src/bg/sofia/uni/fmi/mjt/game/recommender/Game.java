package bg.sofia.uni.fmi.mjt.game.recommender;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public record Game(String name,
                   String platform,
                   LocalDate releaseDate,
                   String summary,
                   int metaScore,
                   double userReview
) {
    public static final int NAME = 0;
    public static final int PLATFORM = 1;
    public static final int RELEASE_DATE = 2;
    public static final int SUMMARY = 3;
    public static final int META_SCORE = 4;
    public static final int USER_REVIEW = 5;

    public static Game of(String line) {
        String[] tokens = line.split(",");

        String name = tokens[NAME];
        String platform = tokens[PLATFORM];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-LLL-yyyy", Locale.ENGLISH);
        LocalDate releaseDate = LocalDate.parse(tokens[RELEASE_DATE], formatter);
        String summary = tokens[SUMMARY];
        int metaScore = Integer.parseInt(tokens[META_SCORE]);
        double userReview = Double.parseDouble(tokens[USER_REVIEW]);

        return new Game(name, platform, releaseDate, summary, metaScore, userReview);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return metaScore == game.metaScore && Double.compare(game.userReview, userReview) == 0 &&
            Objects.equals(name, game.name) && Objects.equals(platform, game.platform) &&
            Objects.equals(releaseDate, game.releaseDate) && Objects.equals(summary, game.summary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, platform, releaseDate, summary, metaScore, userReview);
    }
}

package bg.sofia.uni.fmi.mjt.spotify.playable;

public sealed abstract class AbstractPlayableContent
    implements Playable permits Video, Audio {
    protected static final String MESSAGE = "Currently playing %s: %s";

    private final String title;
    private final String artist;
    private final int year;
    private final double duration;
    protected int totalPlays;

    public AbstractPlayableContent(String title, String artist, int year, double duration) {
        this.title = title;
        this.artist = artist;
        this.year = year;
        this.duration = duration;
        totalPlays = 0;
    }

    public abstract String play();

    @Override
    public int getTotalPlays() {
        return totalPlays;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public double getDuration() {
        return duration;
    }
}

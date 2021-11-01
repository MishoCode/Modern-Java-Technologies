package bg.sofia.uni.fmi.mjt.spotify.playable;

public final class Video extends AbstractPlayableContent {
    public Video(String title, String artist, int year, double duration) {
        super(title, artist, year, duration);
    }

    @Override
    public String play() {
        totalPlays++;
        return String.format(MESSAGE, "video", getTitle());
    }
}

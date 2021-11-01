package bg.sofia.uni.fmi.mjt.spotify.playable;

public final class Audio extends AbstractPlayableContent {
    public Audio(String title, String artist, int year, double duration) {
        super(title, artist, year, duration);
    }

    @Override
    public String play() {
        totalPlays++;
        return String.format(MESSAGE, "audio", getTitle());
    }
}

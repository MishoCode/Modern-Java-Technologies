package bg.sofia.uni.fmi.mjt.twitch.content.video;

import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;

public class VideoImpl implements Video {

    private final Stream stream;
    private final Duration duration;
    private int numberOfViews;

    public VideoImpl(Stream stream, Duration duration) {
        this.stream = stream;
        this.duration = duration;
    }

    @Override
    public Metadata getMetadata() {
        return stream.getMetadata();
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void startWatching(User user) {
        numberOfViews++;
    }

    @Override
    public void stopWatching(User user) {
    }

    @Override
    public int getNumberOfViews() {
        return numberOfViews;
    }
}

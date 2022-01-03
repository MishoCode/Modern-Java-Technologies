package bg.sofia.uni.fmi.mjt.twitch.content.stream;

import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class StreamImpl implements Stream {
    private static final int DEFAULT_DURATION = 20;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int numberOfViews;
    private final Metadata metadata;

    public StreamImpl(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public Duration getDuration() {
        if (endTime == null) {
            return Duration.between(startTime, LocalDateTime.now());
        }

        return Duration.between(startTime, endTime);
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

    @Override
    public void startStreaming() {
        startTime = LocalDateTime.now();
        metadata.getStreamer().setStatus(UserStatus.STREAMING);
    }

    @Override
    public void stopStreaming() {
        endTime = startTime.plusMinutes(DEFAULT_DURATION);
        metadata.getStreamer().setStatus(UserStatus.OFFLINE);
    }
}

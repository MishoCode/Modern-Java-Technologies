package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.StreamImpl;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.content.video.VideoImpl;
import bg.sofia.uni.fmi.mjt.twitch.user.*;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Twitch implements StreamingPlatform {

    public static final String USER_NOT_FOUND_MSG = "The user %s is not found in the service.";
    public static final String USER_IS_STREAMING_MSG = "The user %s is streaming now.";
    public static final String USER_IS_NOT_STREAMING_MSG = "The user %s is not streaming now.";

    public static final String USERNAME_PARAM_NAME = "Username";
    public static final String CONTENT_PARAM_NAME = "Content";
    public static final String STREAM_PARAM_NAME = "Stream";
    public static final String TITLE_PARAM_NAME = "Title";
    public static final String CATEGORY_PARAM_NAME = "Category";

    private final UserService userService;
    private final Map<String, UserAnalyzer> usersInfo;
    private Content mostWatchedContent;

    public Twitch(UserService userService) {
        this.userService = userService;
        usersInfo = new HashMap<>();
        initializeUsersInfo();
    }

    @Override
    public Stream startStream(String username, String title, Category category)
        throws UserNotFoundException, UserStreamingException {

        assertNonNull(username, USERNAME_PARAM_NAME);
        assertNonNull(title, TITLE_PARAM_NAME);
        assertNonNull(category, CATEGORY_PARAM_NAME);
        assertNonEmptyString(username, USERNAME_PARAM_NAME);
        assertNonEmptyString(title, TITLE_PARAM_NAME);

        User user = getUserByName(username);
        if (user == null) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_MSG, username));
        }

        if (user.getStatus() == UserStatus.STREAMING) {
            throw new UserStreamingException(String.format(USER_IS_STREAMING_MSG, username));
        }

        Stream stream = new StreamImpl(new Metadata(title, category, user));
        stream.startStreaming();

        return stream;
    }

    @Override
    public Video endStream(String username, Stream stream) throws UserNotFoundException, UserStreamingException {
        assertNonNull(username, USERNAME_PARAM_NAME);
        assertNonNull(stream, STREAM_PARAM_NAME);
        assertNonEmptyString(username, USERNAME_PARAM_NAME);

        User user = getUserByName(username);
        if (user == null) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_MSG, username));
        }

        if (!(user.getStatus() == UserStatus.STREAMING)) {
            throw new UserStreamingException(String.format(USER_IS_NOT_STREAMING_MSG, username));
        }

        stream.stopStreaming();
        return new VideoImpl(stream, stream.getDuration());
    }

    @Override
    public void watch(String username, Content content) throws UserNotFoundException, UserStreamingException {
        assertNonNull(content, CONTENT_PARAM_NAME);
        assertNonNull(username, USERNAME_PARAM_NAME);
        assertNonEmptyString(username, USERNAME_PARAM_NAME);

        User user = getUserByName(username);
        if (user == null) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_MSG, username));
        }

        if (user.getStatus() == UserStatus.STREAMING) {
            throw new UserStreamingException(String.format(USER_IS_STREAMING_MSG, username));
        }

        content.startWatching(user);

        UserAnalyzer watcherContent = usersInfo.get(username);
        watcherContent.updateWatchedContentInfo(content);

        User streamer = content.getMetadata().getStreamer();
        UserAnalyzer streamerAnalyzer = usersInfo.get(streamer.getName());
        streamerAnalyzer.updateStreamerViews();

        if (mostWatchedContent == null || content.getNumberOfViews() > mostWatchedContent.getNumberOfViews()) {
            mostWatchedContent = content;
        }

        content.stopWatching(user);
    }

    @Override
    public User getMostWatchedStreamer() {
        Map<String, User> allUsers = userService.getUsers();
        if (allUsers.isEmpty()) {
            return null;
        }

        User mostWatchedStreamer = Collections.max(allUsers.values(), new Comparator<>() {
            @Override
            public int compare(User user1, User user2) {
                UserAnalyzer userAnalyzer1 = usersInfo.get(user1.getName());
                UserAnalyzer userAnalyzer2 = usersInfo.get(user2.getName());
                return Integer.compare(userAnalyzer1.getTotalViews(), userAnalyzer2.getTotalViews());
            }
        });

        UserAnalyzer userAnalyzer = usersInfo.get(mostWatchedStreamer.getName());
        return userAnalyzer.getTotalViews() == 0 ? null : mostWatchedStreamer;
    }

    @Override
    public Content getMostWatchedContent() {
        return mostWatchedContent;
    }

    @Override
    public Content getMostWatchedContentFrom(String username) throws UserNotFoundException {
        assertNonNull(username, USERNAME_PARAM_NAME);
        assertNonEmptyString(username, USERNAME_PARAM_NAME);

        User user = getUserByName(username);
        if (user == null) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_MSG, username));
        }

        UserAnalyzer userAnalyzer = usersInfo.get(username);
        return userAnalyzer.getMostWatchedContent();
    }

    @Override
    public List<Category> getMostWatchedCategoriesBy(String username) throws UserNotFoundException {
        assertNonNull(username, USERNAME_PARAM_NAME);
        assertNonEmptyString(username, USERNAME_PARAM_NAME);

        User user = getUserByName(username);
        if (user == null) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_MSG, username));
        }

        UserAnalyzer userAnalyzer = usersInfo.get(username);
        return userAnalyzer.getMostWatchedCategories();
    }

    private User getUserByName(String username) {
        Map<String, User> users = userService.getUsers();
        return users.getOrDefault(username, null);
    }

    private void initializeUsersInfo() {
        Map<String, User> users = userService.getUsers();

        for (String username : users.keySet()) {
            User currentUser = users.get(username);
            usersInfo.put(username, new UserAnalyzer(currentUser));
        }
    }

    private void assertNonNull(Object object, String paramName) {
        if (object == null ) {
            throw new IllegalArgumentException(paramName + " should not be null.");
        }
    }

    private void assertNonEmptyString(String string, String paramName) {
        if (string.isEmpty()) {
            throw new IllegalArgumentException(paramName + " should not be an empty string.");
        }
    }
}

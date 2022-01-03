package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.util.*;

public class UserAnalyzer {
    private final User user;
    private final Map<Content, Integer> watchedContents;
    private final Map<Category, Integer> watchedCategories;
    private int totalViews;

    public UserAnalyzer(User user) {
        this.user = user;
        watchedContents = new HashMap<>();
        watchedCategories = new EnumMap<>(Category.class);
        totalViews = 0;
    }

    public Content getMostWatchedContent() {
        if (watchedContents.isEmpty()) {
            return null;
        }

        Map.Entry<Content, Integer> mostWatched = Collections.max(watchedContents.entrySet(),
            new Comparator<>() {
                @Override
                public int compare(Map.Entry<Content, Integer> entry1, Map.Entry<Content, Integer> entry2) {
                    return Integer.compare(entry1.getValue(), entry2.getValue());
                }
            });

        return mostWatched.getKey();
    }

    public List<Category> getMostWatchedCategories() {
        if (watchedCategories.isEmpty()) {
            return List.copyOf(new ArrayList<>());
        }

        List<Category> sortedCategories = new ArrayList<>(watchedCategories.keySet());
        Collections.sort(sortedCategories, new Comparator<>() {
            @Override
            public int compare(Category category1, Category category2) {
                return watchedCategories.get(category2) - watchedCategories.get(category1);
            }
        });

        return List.copyOf(sortedCategories);
    }

    public void updateStreamerViews() {
        totalViews++;
    }

    public int getTotalViews() {
        return totalViews;
    }

    public void updateWatchedContentInfo(Content content) {
        if (!watchedContents.containsKey(content)) {
            watchedContents.put(content, 0);
        }

        watchedContents.put(content, watchedContents.get(content) + 1);


        Category category = content.getMetadata().getCategory();
        if (!watchedCategories.containsKey(category)) {
            watchedCategories.put(category, 0);
        }

        watchedCategories.put(category, watchedCategories.get(category) + 1);
    }
}

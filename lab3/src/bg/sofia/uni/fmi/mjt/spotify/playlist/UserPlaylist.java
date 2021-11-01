package bg.sofia.uni.fmi.mjt.spotify.playlist;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public class UserPlaylist implements Playlist {
    public static final int PLAYLIST_CAPACITY = 20;
    public static final String FULL_PLAYLIST_MSG = "The playlist is full.";

    private String name;
    private Playable[] list;
    private int size;

    public UserPlaylist(String name) {
        size = 0;
        this.name = name;
        list = new Playable[PLAYLIST_CAPACITY];
    }

    @Override
    public void add(Playable playable) throws PlaylistCapacityExceededException {
        if (size == PLAYLIST_CAPACITY) {
            throw new PlaylistCapacityExceededException(FULL_PLAYLIST_MSG);
        }

        list[size] = playable;
        size++;
    }

    @Override
    public String getName() {
        return name;
    }
}

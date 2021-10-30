package bg.sofia.uni.fmi.mjt.spotify.playlist;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public class UserPlaylist implements Playlist {
    public static final int PLAYLIST_CAPACITY = 20;

    private String name;
    private Playable[] list = new Playable[PLAYLIST_CAPACITY];

    public UserPlaylist(String name) {
        this.name = name;
    }

    @Override
    public void add(Playable playable) throws PlaylistCapacityExceededException {
        if(list.length == PLAYLIST_CAPACITY){
            throw new PlaylistCapacityExceededException("The playlist is full.");
        }

        list[list.length] = playable;
    }

    @Override
    public String getName() {
        return name;
    }
}

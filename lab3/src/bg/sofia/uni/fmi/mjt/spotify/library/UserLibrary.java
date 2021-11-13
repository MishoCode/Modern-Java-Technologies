package bg.sofia.uni.fmi.mjt.spotify.library;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.EmptyLibraryException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.LibraryCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.playlist.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.playlist.UserPlaylist;

public class UserLibrary implements Library {
    public static final int LIBRARY_CAPACITY = 21;
    public static final String FULL_LIBRARY_MSG = "The library is full.";
    public static final String CANNOT_REMOVE_LIKED_LIST = "This playlist name matches the liked playlist name.";
    public static final String EMPTY_LIBRARY_MSG = "The library is empty.";
    public static final String PLAYLIST_NOT_FOUND_MSG = "This playlist is not present in the library.";

    private Playlist[] userPlaylists;
    private int size;

    private int findPlaylist(String name) {
        for (int i = 0; i < size; i++) {
            if (userPlaylists[i].getName().equals(name)) {
                return i;
            }
        }

        return -1;
    }

    public UserLibrary() {
        size = 1;
        userPlaylists = new Playlist[LIBRARY_CAPACITY];
        userPlaylists[0] = new UserPlaylist("Liked Content");
    }

    @Override
    public void add(Playlist playlist) throws LibraryCapacityExceededException {
        if (size == LIBRARY_CAPACITY) {
            throw new LibraryCapacityExceededException(FULL_LIBRARY_MSG);
        }
        userPlaylists[size] = playlist;
        size++;
    }

    @Override
    public void remove(String name) throws EmptyLibraryException, PlaylistNotFoundException {
        if (name.equals(userPlaylists[0].getName())) {
            throw new IllegalArgumentException(CANNOT_REMOVE_LIKED_LIST);
        }

        if (size <= 1) {
            throw new EmptyLibraryException(EMPTY_LIBRARY_MSG);
        }

        int playlistToRemoveIndex = findPlaylist(name);
        if (playlistToRemoveIndex == -1) {
            throw new PlaylistNotFoundException(PLAYLIST_NOT_FOUND_MSG);
        }

        Playlist[] newPlaylists = new Playlist[userPlaylists.length - 1];
        for (int i = 0; i < size; i++) {
            if (i != playlistToRemoveIndex) {
                newPlaylists[i] = userPlaylists[i];
            }
        }

        size--;
        userPlaylists = newPlaylists;
    }

    @Override
    public Playlist getLiked() {
        return userPlaylists[0];
    }
}

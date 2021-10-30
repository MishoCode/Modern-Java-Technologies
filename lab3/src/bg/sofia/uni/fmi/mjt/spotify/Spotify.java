package bg.sofia.uni.fmi.mjt.spotify;

import bg.sofia.uni.fmi.mjt.spotify.account.Account;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlayableNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.StreamingServiceException;
import bg.sofia.uni.fmi.mjt.spotify.library.Library;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;
import bg.sofia.uni.fmi.mjt.spotify.playlist.Playlist;

public class Spotify implements StreamingService{
    public static final String NULL_ARGS_MSG = "Arguments cannot be null.";
    public static final String ACCOUNT_NOT_FOUND_MSG = "This account is not present in the streaming service.";
    public static final String CONTENT_NOT_FOUND_MSG = "This content is not present in the streaming service.";
    public static final String CONTENT_CANNOT_BE_ADDED = "The content cannot be added.";

    private Account[] accounts;
    private Playable[] playableContent;

    private boolean isRegisteredAccount(Account account){
        for(Account acc: accounts){
            if(acc.getEmail().equals(account.getEmail())){
                return true;
            }
        }

        return false;
    }

    private Playable findContent(String title){
        for(Playable playable : playableContent){
            if(playable.getTitle().equals(title)){
                return playable;
            }
        }

        return null;
    }

    public Spotify(Account[] accounts, Playable[] playableContent) {
        this.accounts = accounts;
        this.playableContent = playableContent;
    }

    @Override
    public void play(Account account, String title) throws AccountNotFoundException, PlayableNotFoundException {
        if(account == null || title == null || title.isEmpty()){
            throw new IllegalArgumentException(NULL_ARGS_MSG);
        }

        if(isRegisteredAccount(account)){
            throw new AccountNotFoundException(ACCOUNT_NOT_FOUND_MSG);
        }

        Playable playable = findContent(title);
        if(playable == null){
            throw new PlayableNotFoundException(CONTENT_NOT_FOUND_MSG);
        }

        playable.play();
        account.listen(playable);
    }

    @Override
    public void like(Account account, String title)
            throws AccountNotFoundException, PlayableNotFoundException, StreamingServiceException {
        if(account == null || title == null || title.isEmpty()){
            throw new IllegalArgumentException(NULL_ARGS_MSG);
        }

        if(isRegisteredAccount(account)){
            throw new AccountNotFoundException(ACCOUNT_NOT_FOUND_MSG);
        }

        Playable playable = findContent(title);
        if(playable == null){
            throw new PlayableNotFoundException(CONTENT_NOT_FOUND_MSG);
        }

        Playlist likedPlaylist = account.getLibrary().getLiked();
        try{
            likedPlaylist.add(playable);
        } catch (PlaylistCapacityExceededException e) {
            e.printStackTrace();
            throw new StreamingServiceException("The content cannot be added.");
        }
    }

    @Override
    public Playable findByTitle(String title) throws PlayableNotFoundException {
        return null;
    }

    @Override
    public Playable getMostPlayed() {
        return null;
    }

    @Override
    public double getTotalListenTime() {
        return 0;
    }

    @Override
    public double getTotalMoneyEarned() {
        return 0;
    }
}

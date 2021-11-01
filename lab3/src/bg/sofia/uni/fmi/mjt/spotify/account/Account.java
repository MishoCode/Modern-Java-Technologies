package bg.sofia.uni.fmi.mjt.spotify.account;

import bg.sofia.uni.fmi.mjt.spotify.library.Library;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public abstract class Account {
    private String email;
    private Library library;
    protected double totalListenTime;
    protected int listenedCount;

    public Account(String email, Library library) {
        this.email = email;
        this.library = library;
        totalListenTime = 0.0;
        listenedCount = 0;
    }

    public String getEmail() {
        return email;
    }

    public abstract int getAdsListenedTo();

    public abstract AccountType getType();

    public void listen(Playable playable) {
        totalListenTime += playable.getDuration();
        listenedCount++;
        System.out.println(playable.play());
    }

    public Library getLibrary() {
        return library;
    }

    public double getTotalListenTime() {
        return totalListenTime;
    }
}

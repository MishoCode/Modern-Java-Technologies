package bg.sofia.uni.fmi.mjt.spotify.account;

import bg.sofia.uni.fmi.mjt.spotify.library.Library;

public class FreeAccount extends Account{
    public FreeAccount(String email, Library library) {
        super(email, library);
    }

    @Override
    public int getAdsListenedTo() {
        return listenedCount/5;
    }

    @Override
    public AccountType getType() {
        return AccountType.FREE;
    }
}

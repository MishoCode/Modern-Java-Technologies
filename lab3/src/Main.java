import bg.sofia.uni.fmi.mjt.spotify.Spotify;
import bg.sofia.uni.fmi.mjt.spotify.StreamingService;
import bg.sofia.uni.fmi.mjt.spotify.account.Account;
import bg.sofia.uni.fmi.mjt.spotify.account.FreeAccount;
import bg.sofia.uni.fmi.mjt.spotify.account.PremiumAccount;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.EmptyLibraryException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.LibraryCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlayableNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.library.Library;
import bg.sofia.uni.fmi.mjt.spotify.library.UserLibrary;
import bg.sofia.uni.fmi.mjt.spotify.playable.Audio;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;
import bg.sofia.uni.fmi.mjt.spotify.playable.Video;
import bg.sofia.uni.fmi.mjt.spotify.playlist.UserPlaylist;

public class Main {
    public static void main(String[] args) {
        Account account1 = new FreeAccount("mishooo@abv.bg", new UserLibrary());
        Account account2 = new FreeAccount("peshoo@abv.bg", new UserLibrary());
        Account account3 = new PremiumAccount("gosho@gmail.com", new UserLibrary());

        Account account4 = new PremiumAccount("fake@abv.bg", new UserLibrary());

        Playable playable1 = new Audio("title1", "artist1", 1997, 0.3);
        Playable playable2 = new Audio("title2", "artist2", 2005, 1.2);
        Playable playable3 = new Audio("title3", "artist3", 2014, 1.0);
        Playable playable4 = new Video("tile4", "artist4", 2010, 2.5);
        Playable playable5 = new Video("title5", "artist5", 2008, 1.5);

        Account[] accounts = new Account[]{account1, account2, account3};
        Playable[] playableContent = new Playable[]{playable1, playable2, playable3, playable4, playable5};
        StreamingService spotify = new Spotify(accounts, playableContent);

        try {
            System.out.println(spotify.findByTitle("title1").getTitle());
            //System.out.println(spotify.findByTitle("title").getTitle());
        } catch (PlayableNotFoundException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }

        try {
            for (int i = 0; i < 17; i++) {
                spotify.play(account1, "title2");
            }
            spotify.play(account2, "title2");
            spotify.play(account3, "title1");
            spotify.play(account3, "title2");
        } catch (AccountNotFoundException | PlayableNotFoundException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }

        /*try {
            spotify.like(account1, "title2");
            spotify.like(account1,"title5");
            spotify.like(account2, "title2");
            spotify.like(account3, "title1");

            System.out.println(account1.getLibrary().getLiked().getName());
            System.out.println(account2.getLibrary().getLiked().getName());
            System.out.println(account3.getLibrary().getLiked().getName());
        } catch (AccountNotFoundException | PlayableNotFoundException | StreamingServiceException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }*/

        Playable mostPlayed = spotify.getMostPlayed();
        System.out.println(mostPlayed.getTitle() + " : " + mostPlayed.getTotalPlays());

        double totalListenTime = spotify.getTotalListenTime();
        System.out.printf("%.2f\n", totalListenTime);

        double totalMoneyEarned = spotify.getTotalMoneyEarned();
        System.out.printf("%.2f\n", totalMoneyEarned); //25.30

        Library library = new UserLibrary();
        try {
            library.add(new UserPlaylist("playlist1"));
            library.add(new UserPlaylist("playlist2"));
            library.remove("playlist2");
            //library.remove("playlist2"); // content not found because it has already been removed
            library.remove("playlist1");
            //library.remove("Liked Content");
            //library.remove("playlist1"); Empty library (only lined playlist; no other playlists)
        } catch (LibraryCapacityExceededException | PlaylistNotFoundException | EmptyLibraryException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}

package bg.sofia.uni.fmi.mjt.game.recommender;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class GameRecommenderTest {
    private static List<Game> games;
    private static GameRecommender recommender;

    @BeforeAll
    public static void setUp() throws IOException {
        Reader gamesReader = initGames();

        try (BufferedReader reader = new BufferedReader(gamesReader)) {
            games = reader.lines()
                .skip(1)
                .map(Game::of)
                .toList();
        }

        recommender = new GameRecommender(initGames());
    }

    @Test
    public void testIfExistingDatasetIsLoadedCorrectly() {
        String assertMessage =
            "Number of games in the GameRecommender does not match the number of games in the dataset.";
        int expected = games.size();
        int actual = recommender.getAllGames().size();
        assertEquals(expected, actual, assertMessage);
    }

    @Test
    public void testGetExplicitGamesUnmodifiable() {
        List<Game> allGames = recommender.getAllGames();

        try {
            allGames.clear();
        } catch (UnsupportedOperationException e) {
            return;
        }

        fail("Returned collection is not unmodifiable.");
    }

    @Test
    public void testGetGamesReleasedAfterSuccess() {
        List<Game> expected = List.of(
            Game.of(
                "Red Dead Redemption 2,Xbox One,26-Oct-2018,Developed by the creators of Grand Theft Auto V and Red Dead Redemption Red Dead Redemption 2 is an epic tale of life in America???s unforgiving heartland. The game???s vast and atmospheric world also provides the foundation for a brand new online multiplayer experience. America 1899. The end of the Wild West era has begun. After a robbery goes badly wrong in the western town of Blackwater Arthur Morgan and the Van der Linde gang are forced to flee. With federal agents and the best bounty hunters in the nation massing on their heels the gang has to rob steal and fight their way across the rugged heartland of America in order to survive. As deepening internal fissures threaten to tear the gang apart Arthur must make a choice between his own ideals and loyalty to the gang that raised him. [Rockstar],97,8"),
            Game.of(
                "Disco Elysium: The Final Cut,PC,30-Mar-2021,Disco Elysium - The Final Cut is the definitive edition of the smash-hit RPG. Pursue your political dreams in new quests meet and question more of the city's locals and explore a whole extra area. Full voice-acting controller support and expanded language options also included. Get even more out of this award-winning open world. You're a detective with a unique skill system at your disposal and a whole city block to carve your path across. Interrogate unforgettable characters crack murders or take bribes. Become a hero or an absolute disaster of a human being.,97,8.3"));
        List<Game> actual = recommender.getGamesReleasedAfter(LocalDate.of(2018, 9, 1));

        assertTrue(assertCollectionsEquals(expected, actual), "The list of games is not determined correctly");
    }

    @Test
    public void testGetGamesReleasedAfterReturnsEmptyList() {
        List<Game> actual = recommender.getGamesReleasedAfter(LocalDate.of(2022, 1, 1));
        assertTrue(actual.isEmpty(), "Empty list is expected");
    }

    @Test
    public void testGetTopNUserRatedGamesThrowsExceptionWhenNisNegative() {
        assertThrows(IllegalArgumentException.class,
            () -> recommender.getTopNUserRatedGames(-3),
            "IllegalArgumentException is expected to be thrown when n is negative");
    }

    @Test
    public void testGetTopNUserRatedGamesSuccess() {
        List<Game> expected = List.of(
            Game.of(
                "The Legend of Zelda: Ocarina of Time,Nintendo 64,23-Nov-1998,As a young boy Link is tricked by Ganondorf the King of the Gerudo Thieves. The evil human uses Link to gain access to the Sacred Realm where he places his tainted hands on Triforce and transforms the beautiful Hyrulean landscape into a barren wasteland. Link is determined to fix the problems he helped to create so with the help of Rauru he travels through time gathering the powers of the Seven Sages.,99,9.1"),
            Game.of(
                "Super Mario Galaxy,Wii,12-Nov-2007,[Metacritic's 2007 Wii Game of the Year] The ultimate Nintendo hero is taking the ultimate step ... out into space. Join Mario as he ushers in a new era of video games defying gravity across all the planets in the galaxy. When some creature escapes into space with Princess Peach Mario gives chase exploring bizarre planets all across the galaxy. Mario Peach and enemies new and old are here. Players run jump and battle enemies as they explore all the planets in the galaxy. Since this game makes full use of all the features of the Wii Remote players have to do all kinds of things to succeed: pressing buttons swinging the Wii Remote and the Nunchuk and even pointing at and dragging things with the pointer. Since he's in space Mario can perform mind-bending jumps unlike anything he's done before. He'll also have a wealth of new moves that are all based around tilting pointing and shaking the Wii Remote. Shake tilt and point! Mario takes advantage of all the unique aspects of the Wii Remote and Nunchuk controller unleashing new moves as players shake the controller and even point at and drag items with the pointer. [Nintendo],97,9.1")
        );

        List<Game> actual = recommender.getTopNUserRatedGames(2);

        assertTrue(assertCollectionsEquals(expected, actual), "The list of games is not determined correctly");
    }

    @Test
    public void testGetTopNUserRatedGamesReturnUnmodifiableList() {
        List<Game> result = recommender.getTopNUserRatedGames(3);

        try {
            result.clear();
        } catch (UnsupportedOperationException e) {
            return;
        }

        fail("The returned list is not unmodifiable");
    }

    @Test
    public void testGetYearsWithTopScoringGamesSuccess() {
        List<Integer> expected = List.of(1998);
        List<Integer> actual = recommender.getYearsWithTopScoringGames(99);

        assertIterableEquals(expected, actual, "The list of years is not determined correctly");
    }

    @Test
    public void testGetAllNamesOfGamesReleasesInGivenYearSuccess() {
        String expected = "Grand Theft Auto IV, Grand Theft Auto IV";
        String actual = recommender.getAllNamesOfGamesReleasedIn(2008);

        assertEquals(expected, actual, "The list of names is not determined correctly");
    }

    @Test
    public void testGetAllNamesOfGamesReleasedInGivenYearReturnsAnEmptyStringSuccess() {
        String result = recommender.getAllNamesOfGamesReleasedIn(2022);
        assertTrue(result.isEmpty(), "An empty string is expected");
    }

    @Test
    public void testGetHighestUserRatedGameByPlatformThrowsExceptionWhenPlatformIsNull() {
        assertThrows(NoSuchElementException.class,
            () -> recommender.getHighestUserRatedGameByPlatform(null),
            "NoSuchElementException is expected to be thrown when platform is null");
    }

    @Test
    public void testGetHighestUserRatedGameByPlatformThrowsExceptionWhenPlatformIsEmptyString() {
        assertThrows(NoSuchElementException.class,
            () -> recommender.getHighestUserRatedGameByPlatform(""),
            "NoSuchElementException is expected to be thrown when platform is an empty string");
    }

    @Test
    public void testGetHighestUserRatedGameByPlatformSuccess() {
        Game expected = Game.of(
            "The Legend of Zelda: Ocarina of Time,Nintendo 64,23-Nov-1998,As a young boy Link is tricked by Ganondorf the King of the Gerudo Thieves. The evil human uses Link to gain access to the Sacred Realm where he places his tainted hands on Triforce and transforms the beautiful Hyrulean landscape into a barren wasteland. Link is determined to fix the problems he helped to create so with the help of Rauru he travels through time gathering the powers of the Seven Sages.,99,9.1"
        );
        Game actual = recommender.getHighestUserRatedGameByPlatform("Nintendo 64");

        assertEquals(expected, actual, "The highest user rated game by platform is not determined correctly");
    }

    @Test
    public void getAllGamesByPlatformSuccess() {
        Map<String, Set<Game>> expected = Map.of(
            "Nintendo 64", Set.of(
                Game.of(
                    "The Legend of Zelda: Ocarina of Time,Nintendo 64,23-Nov-1998,As a young boy Link is tricked by Ganondorf the King of the Gerudo Thieves. The evil human uses Link to gain access to the Sacred Realm where he places his tainted hands on Triforce and transforms the beautiful Hyrulean landscape into a barren wasteland. Link is determined to fix the problems he helped to create so with the help of Rauru he travels through time gathering the powers of the Seven Sages.,99,9.1")),
            "PlayStation", Set.of(
                Game.of(
                    "Tony Hawk's Pro Skater 2,PlayStation,20-Sep-2000,As most major publishers' development efforts shift to any number of next-generation platforms Tony Hawk 2 will likely stand as one of the last truly fantastic games to be released on the PlayStation.,98,7.4")),
            "PlayStation 3", Set.of(
                Game.of(
                    "Grand Theft Auto IV,PlayStation 3,29-Apr-2008,[Metacritic's 2008 PS3 Game of the Year	 Also known as GTA IV] What does the American Dream mean today? For Niko Belic fresh off the boat from Europe. It's the hope he can escape his past. For his cousin Roman it is the vision that together they can find fortune in Liberty City gateway to the land of opportunity. As they slip into debt and are dragged into a criminal underworld by a series of shysters thieves and sociopaths they discover that the reality is very different from the dream in a city that worships money and status and is heaven for those who have them an a living nightmare for those who don't. [Rockstar Games],98,7.7"),
                Game.of(
                    "Grand Theft Auto V,PlayStation 3,17-Sep-2013,Los Santos is a vast sun-soaked metropolis full of self-help gurus starlets and once-important formerly-known-as celebrities. The city was once the envy of the Western world but is now struggling to stay afloat in an era of economic uncertainty and reality TV. Amidst the chaos three unique criminals plot their own chances of survival and success: Franklin a former street gangster in search of real opportunities and serious cheddar	 Michael a professional ex-con whose retirement is a lot less rosy than he hoped it would be	 and Trevor a violent maniac driven by the chance of a cheap high and the next big score. Quickly running out of options the crew risks it all in a sequence of daring and dangerous heists that could set them up for life.,97,8.3")),
            "Dreamcast", Set.of(
                Game.of(
                    "SoulCalibur,Dreamcast,08-Sep-1999,This is a tale of souls and swords transcending the world and all its history told for all eternity... The greatest weapons-based fighter returns this time on Sega Dreamcast. Soul Calibur unleashes incredible graphics fantastic fighters and combos so amazing they'll make your head spin!,98,8.4")),
            "Xbox 360", Set.of(
                Game.of(
                    "Grand Theft Auto IV,Xbox 360,29-Apr-2008,[Metacritic's 2008 Xbox 360 Game of the Year	 Also known as GTA IV] What does the American Dream mean today? For Niko Belic fresh off the boat from Europe. It's the hope he can escape his past. For his cousin Roman it is the vision that together they can find fortune in Liberty City gateway to the land of opportunity. As they slip into debt and are dragged into a criminal underworld by a series of shysters thieves and sociopaths they discover that the reality is very different from the dream in a city that worships money and status and is heaven for those who have them an a living nightmare for those who don't. [Rockstar Games],98,7.9")),
            "Wii", Set.of(
                Game.of(
                    "Super Mario Galaxy,Wii,12-Nov-2007,[Metacritic's 2007 Wii Game of the Year] The ultimate Nintendo hero is taking the ultimate step ... out into space. Join Mario as he ushers in a new era of video games defying gravity across all the planets in the galaxy. When some creature escapes into space with Princess Peach Mario gives chase exploring bizarre planets all across the galaxy. Mario Peach and enemies new and old are here. Players run jump and battle enemies as they explore all the planets in the galaxy. Since this game makes full use of all the features of the Wii Remote players have to do all kinds of things to succeed: pressing buttons swinging the Wii Remote and the Nunchuk and even pointing at and dragging things with the pointer. Since he's in space Mario can perform mind-bending jumps unlike anything he's done before. He'll also have a wealth of new moves that are all based around tilting pointing and shaking the Wii Remote. Shake tilt and point! Mario takes advantage of all the unique aspects of the Wii Remote and Nunchuk controller unleashing new moves as players shake the controller and even point at and drag items with the pointer. [Nintendo],97,9.1"),
                Game.of(
                    "Super Mario Galaxy 2,Wii,23-May-2010,Super Mario Galaxy 2 the sequel to the galaxy-hopping original game includes the gravity-defying physics-based exploration from the first game but is loaded with entirely new galaxies and features to challenge players. On some stages Mario can pair up with his dinosaur buddy Yoshi and use his tongue to grab items and spit them back at enemies. Players can also have fun with new items such as a drill that lets our hero tunnel through solid rock. [Nintendo],97,9.1")),
            "Xbox One", Set.of(
                Game.of(
                    "Red Dead Redemption 2,Xbox One,26-Oct-2018,Developed by the creators of Grand Theft Auto V and Red Dead Redemption Red Dead Redemption 2 is an epic tale of life in America???s unforgiving heartland. The game???s vast and atmospheric world also provides the foundation for a brand new online multiplayer experience. America 1899. The end of the Wild West era has begun. After a robbery goes badly wrong in the western town of Blackwater Arthur Morgan and the Van der Linde gang are forced to flee. With federal agents and the best bounty hunters in the nation massing on their heels the gang has to rob steal and fight their way across the rugged heartland of America in order to survive. As deepening internal fissures threaten to tear the gang apart Arthur must make a choice between his own ideals and loyalty to the gang that raised him. [Rockstar],97,8"),
                Game.of(
                    "Grand Theft Auto V,Xbox One,18-Nov-2014,Grand Theft Auto 5 melds storytelling and gameplay in unique ways as players repeatedly jump in and out of the lives of the game's three protagonists playing all sides of the game's interwoven story.,97,7.9")),
            "PC", Set.of(
                Game.of(
                    "Disco Elysium: The Final Cut,PC,30-Mar-2021,Disco Elysium - The Final Cut is the definitive edition of the smash-hit RPG. Pursue your political dreams in new quests meet and question more of the city's locals and explore a whole extra area. Full voice-acting controller support and expanded language options also included. Get even more out of this award-winning open world. You're a detective with a unique skill system at your disposal and a whole city block to carve your path across. Interrogate unforgettable characters crack murders or take bribes. Become a hero or an absolute disaster of a human being.,97,8.3"))
        );

        Map<String, Set<Game>> actual = recommender.getAllGamesByPlatform();

        assertEquals(expected.keySet(), actual.keySet(), "Wrong grouping of the games by a platform");
        assertTrue(expected.entrySet().stream()
                .allMatch(t -> assertCollectionsEquals(t.getValue(), actual.get(t.getKey()))),
            "Wrong grouping of the games by a platform");
    }

    @Test
    public void testGetYearsActiveWhenPlatformIsNull() {
        int expected = 0;
        int actual = recommender.getYearsActive(null);

        assertEquals(expected, actual, "0 active years are expected when the platform is null");
    }

    @Test
    public void testGetYearsActiveWhenPlatformIsEmptyString() {
        int expected = 0;
        int actual = recommender.getYearsActive("");

        assertEquals(expected, actual, "0 active years are expected when the platform is an empty string");
    }

    @Test
    public void testGetYearsActiveWhenPlatformIsUnknown() {
        int expected = 0;
        int actual = recommender.getYearsActive("unknown platform");

        assertEquals(expected, actual, "0 active years are expected when the platform is unknown");
    }

    @Test
    public void testGetYearsActiveOnly1ActiveYearSuccess() {
        int expected = 1;
        int actual = recommender.getYearsActive("Nintendo 64");

        assertEquals(expected, actual, "Exactly one active year is expected");
    }

    @Test
    public void testGetYearsActiveMoreThan1Success() {
        int expected = 6;
        int actual = recommender.getYearsActive("PlayStation 3");

        assertEquals(expected, actual, "Active years are not calculated correctly");
    }

    @Test
    public void testGetGamesSimilarToSuccess() {
        List<Game> expected = List.of(
            Game.of(
                "Red Dead Redemption 2,Xbox One,26-Oct-2018,Developed by the creators of Grand Theft Auto V and Red Dead Redemption Red Dead Redemption 2 is an epic tale of life in America???s unforgiving heartland. The game???s vast and atmospheric world also provides the foundation for a brand new online multiplayer experience. America 1899. The end of the Wild West era has begun. After a robbery goes badly wrong in the western town of Blackwater Arthur Morgan and the Van der Linde gang are forced to flee. With federal agents and the best bounty hunters in the nation massing on their heels the gang has to rob steal and fight their way across the rugged heartland of America in order to survive. As deepening internal fissures threaten to tear the gang apart Arthur must make a choice between his own ideals and loyalty to the gang that raised him. [Rockstar],97,8"),
            Game.of(
                "Grand Theft Auto V,Xbox One,18-Nov-2014,Grand Theft Auto 5 melds storytelling and gameplay in unique ways as players repeatedly jump in and out of the lives of the game's three protagonists playing all sides of the game's interwoven story.,97,7.9")
        );
        List<Game> actual = recommender.getGamesSimilarTo("Grand", "Theft");

        assertTrue(assertCollectionsEquals(expected, actual), "The list of games is not determined correctly");
    }

    public static Reader initGames() {
        String[] games = {
            "",
            "The Legend of Zelda: Ocarina of Time,Nintendo 64,23-Nov-1998,As a young boy Link is tricked by Ganondorf the King of the Gerudo Thieves. The evil human uses Link to gain access to the Sacred Realm where he places his tainted hands on Triforce and transforms the beautiful Hyrulean landscape into a barren wasteland. Link is determined to fix the problems he helped to create so with the help of Rauru he travels through time gathering the powers of the Seven Sages.,99,9.1",
            "Tony Hawk's Pro Skater 2,PlayStation,20-Sep-2000,As most major publishers' development efforts shift to any number of next-generation platforms Tony Hawk 2 will likely stand as one of the last truly fantastic games to be released on the PlayStation.,98,7.4",
            "Grand Theft Auto IV,PlayStation 3,29-Apr-2008,[Metacritic's 2008 PS3 Game of the Year	 Also known as GTA IV] What does the American Dream mean today? For Niko Belic fresh off the boat from Europe. It's the hope he can escape his past. For his cousin Roman it is the vision that together they can find fortune in Liberty City gateway to the land of opportunity. As they slip into debt and are dragged into a criminal underworld by a series of shysters thieves and sociopaths they discover that the reality is very different from the dream in a city that worships money and status and is heaven for those who have them an a living nightmare for those who don't. [Rockstar Games],98,7.7",
            "SoulCalibur,Dreamcast,08-Sep-1999,This is a tale of souls and swords transcending the world and all its history told for all eternity... The greatest weapons-based fighter returns this time on Sega Dreamcast. Soul Calibur unleashes incredible graphics fantastic fighters and combos so amazing they'll make your head spin!,98,8.4",
            "Grand Theft Auto IV,Xbox 360,29-Apr-2008,[Metacritic's 2008 Xbox 360 Game of the Year	 Also known as GTA IV] What does the American Dream mean today? For Niko Belic fresh off the boat from Europe. It's the hope he can escape his past. For his cousin Roman it is the vision that together they can find fortune in Liberty City gateway to the land of opportunity. As they slip into debt and are dragged into a criminal underworld by a series of shysters thieves and sociopaths they discover that the reality is very different from the dream in a city that worships money and status and is heaven for those who have them an a living nightmare for those who don't. [Rockstar Games],98,7.9",
            "Super Mario Galaxy,Wii,12-Nov-2007,[Metacritic's 2007 Wii Game of the Year] The ultimate Nintendo hero is taking the ultimate step ... out into space. Join Mario as he ushers in a new era of video games defying gravity across all the planets in the galaxy. When some creature escapes into space with Princess Peach Mario gives chase exploring bizarre planets all across the galaxy. Mario Peach and enemies new and old are here. Players run jump and battle enemies as they explore all the planets in the galaxy. Since this game makes full use of all the features of the Wii Remote players have to do all kinds of things to succeed: pressing buttons swinging the Wii Remote and the Nunchuk and even pointing at and dragging things with the pointer. Since he's in space Mario can perform mind-bending jumps unlike anything he's done before. He'll also have a wealth of new moves that are all based around tilting pointing and shaking the Wii Remote. Shake tilt and point! Mario takes advantage of all the unique aspects of the Wii Remote and Nunchuk controller unleashing new moves as players shake the controller and even point at and drag items with the pointer. [Nintendo],97,9.1",
            "Super Mario Galaxy 2,Wii,23-May-2010,Super Mario Galaxy 2 the sequel to the galaxy-hopping original game includes the gravity-defying physics-based exploration from the first game but is loaded with entirely new galaxies and features to challenge players. On some stages Mario can pair up with his dinosaur buddy Yoshi and use his tongue to grab items and spit them back at enemies. Players can also have fun with new items such as a drill that lets our hero tunnel through solid rock. [Nintendo],97,9.1",
            "Red Dead Redemption 2,Xbox One,26-Oct-2018,Developed by the creators of Grand Theft Auto V and Red Dead Redemption Red Dead Redemption 2 is an epic tale of life in America???s unforgiving heartland. The game???s vast and atmospheric world also provides the foundation for a brand new online multiplayer experience. America 1899. The end of the Wild West era has begun. After a robbery goes badly wrong in the western town of Blackwater Arthur Morgan and the Van der Linde gang are forced to flee. With federal agents and the best bounty hunters in the nation massing on their heels the gang has to rob steal and fight their way across the rugged heartland of America in order to survive. As deepening internal fissures threaten to tear the gang apart Arthur must make a choice between his own ideals and loyalty to the gang that raised him. [Rockstar],97,8",
            "Grand Theft Auto V,Xbox One,18-Nov-2014,Grand Theft Auto 5 melds storytelling and gameplay in unique ways as players repeatedly jump in and out of the lives of the game's three protagonists playing all sides of the game's interwoven story.,97,7.9",
            "Grand Theft Auto V,PlayStation 3,17-Sep-2013,Los Santos is a vast sun-soaked metropolis full of self-help gurus starlets and once-important formerly-known-as celebrities. The city was once the envy of the Western world but is now struggling to stay afloat in an era of economic uncertainty and reality TV. Amidst the chaos three unique criminals plot their own chances of survival and success: Franklin a former street gangster in search of real opportunities and serious cheddar	 Michael a professional ex-con whose retirement is a lot less rosy than he hoped it would be	 and Trevor a violent maniac driven by the chance of a cheap high and the next big score. Quickly running out of options the crew risks it all in a sequence of daring and dangerous heists that could set them up for life.,97,8.3",
            "Disco Elysium: The Final Cut,PC,30-Mar-2021,Disco Elysium - The Final Cut is the definitive edition of the smash-hit RPG. Pursue your political dreams in new quests meet and question more of the city's locals and explore a whole extra area. Full voice-acting controller support and expanded language options also included. Get even more out of this award-winning open world. You're a detective with a unique skill system at your disposal and a whole city block to carve your path across. Interrogate unforgettable characters crack murders or take bribes. Become a hero or an absolute disaster of a human being.,97,8.3"
        };

        return new StringReader(Arrays.stream(games).collect(Collectors.joining(System.lineSeparator())));
    }

    private boolean assertCollectionsEquals(Collection<Game> expected, Collection<Game> actual) {
        return expected.containsAll(actual) && actual.containsAll(expected);
    }
}

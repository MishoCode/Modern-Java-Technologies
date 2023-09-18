package bg.sofia.uni.fmi.mjt.logger;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultLogParserTest {
    public static final Path DIRECTORY = Path.of("").toAbsolutePath();
    public static final String DEFAULT_PACKAGE_NAME = "default.package";
    private static final LocalDateTime TIMESTAMP1 =
        LocalDateTime.of(2022, 1, 2, 9, 53, 53, 415438500);
    private static final LocalDateTime TIMESTAMP2 =
        LocalDateTime.of(2022, 1, 2, 9, 54, 33, 415438500);

    private LogParser parser;

    @BeforeEach
    public void setUp() {
        Path path = Path.of(DIRECTORY + File.separator + "parse.txt");
        parser = new DefaultLogParser(path);
    }

    @Test
    public void testGetLogsByLevelSuccess() {
        List<Log> infoLogs = List.of(new Log(Level.INFO, TIMESTAMP1, DEFAULT_PACKAGE_NAME, "ala bala keks"),
            new Log(Level.INFO, TIMESTAMP1, DEFAULT_PACKAGE_NAME, "hahahaha"));

        List<Log> warnLogs = List.of(new Log(Level.WARN, TIMESTAMP1, DEFAULT_PACKAGE_NAME, "hi, what`s up"));

        assertIterableEquals(infoLogs, parser.getLogs(Level.INFO), "The list of logs is incorrect");
        assertIterableEquals(warnLogs, parser.getLogs(Level.WARN), "The list of logs is incorrect");
    }

    @Test
    public void testGetLogsByLevelThrowsExceptionWhenLevelIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> parser.getLogs(null),
            "IllegalArgumentException is expected to be thrwon when level is null");
    }

    @Test
    public void testGetLogsBetweenTwoTimestampsSuccess() {
        LocalDateTime from = TIMESTAMP2.minusMinutes(20);
        LocalDateTime to = TIMESTAMP2.plusMinutes(50);

        List<Log> logs2 = List.of(
            new Log(Level.INFO, TIMESTAMP1, DEFAULT_PACKAGE_NAME, "ala bala keks"),
            new Log(Level.INFO, TIMESTAMP1, DEFAULT_PACKAGE_NAME, "hahahaha"),
            new Log(Level.WARN, TIMESTAMP1, DEFAULT_PACKAGE_NAME, "hi, what`s up"),
            new Log(Level.ERROR, TIMESTAMP2, DEFAULT_PACKAGE_NAME, "error"));
        assertIterableEquals(logs2, parser.getLogs(from, to),
            "The list of logs is incorrect");
    }

    @Test
    public void testGetLogsBetweenTwoTimestampsThrowsExceptionWhenFromIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> parser.getLogs(null, LocalDateTime.now()),
            "Illegal argument exception is expected to be thrown when from is null");
    }

    @Test
    public void testGetLogsBetweenTwoTimestampsThrowsExceptionWhenToIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> parser.getLogs(LocalDateTime.now(), null),
            "Illegal argument exception is expected to be thrown when to is null");
    }

    @Test
    public void testGetLogsTailWhenLogsCountIsLessThanTailSizeSuccess() {
        List<Log> tail = List.of(new Log(Level.INFO, TIMESTAMP1, DEFAULT_PACKAGE_NAME, "ala bala keks"),
            new Log(Level.INFO, TIMESTAMP1, DEFAULT_PACKAGE_NAME, "hahahaha"),
            new Log(Level.WARN, TIMESTAMP1, DEFAULT_PACKAGE_NAME, "hi, what`s up"),
            new Log(Level.ERROR, TIMESTAMP2, DEFAULT_PACKAGE_NAME, "error"));

        assertIterableEquals(tail, parser.getLogsTail(10), "The list of logs is incorrect");
    }

    @Test
    public void testGetLogsTailWhenTailSizeIsZeroSuccess() {
        List<Log> tail = new LinkedList<>();
        assertIterableEquals(tail, parser.getLogsTail(0), "An empty list is expected");
    }

    @Test
    public void testGetLogsTailWhenTailSizeIsLessThanLogsCountSuccess() {
        List<Log> tail = List.of(new Log(Level.WARN, TIMESTAMP1, DEFAULT_PACKAGE_NAME, "hi, what`s up"),
            new Log(Level.ERROR, TIMESTAMP2, DEFAULT_PACKAGE_NAME, "error"));

        assertIterableEquals(tail, parser.getLogsTail(2), "The list of logs is incorrect");
    }

    @Test
    public void testGetLogsTailThrowsExceptionWhenTailSizeIsNegative() {
        assertThrows(IllegalArgumentException.class,
            () -> parser.getLogsTail(-3),
            "Illegal argument exception is expected to be thrown when N is negative");
    }
}


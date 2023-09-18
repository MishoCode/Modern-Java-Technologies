package bg.sofia.uni.fmi.mjt.logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class DefaultLoggerTest {
    public static final Path DIRECTORY = Path.of("").toAbsolutePath();
    public static final String DEFAULT_PACKAGE_NAME = "default.package";
    public static final int FILE_SIZE = 256;

    private DefaultLogger logger;

    @BeforeEach
    public void setUp() {
        LoggerOptions options = new LoggerOptions(DefaultLogger.class, DIRECTORY.toString());
        options.setMaxFileSizeBytes(FILE_SIZE);
        logger = new DefaultLogger(options);
    }

    @Test
    public void testLoggerWritesTheCorrectLog() {
        String logMessage = "ala bala keks";
        LocalDateTime timestamp = LocalDateTime.now();
        Level level = Level.INFO;

        String line = "[INFO]|" + timestamp + "|" + DEFAULT_PACKAGE_NAME + "|" + logMessage;

        String[] tokens = line.split("\\|");
        assertEquals(tokens.length, 4);
        assertEquals(tokens[3], logMessage);
        assertEquals(tokens[0], "[" + level + "]");
        assertEquals(tokens[1], timestamp.toString());
        assertEquals(tokens[2], DEFAULT_PACKAGE_NAME);
    }

    @Test
    public void testLoggerCreatesNewFileWhenTheCurrentIsFullSuccess() {
        String logMessage = "ala bala keks";

        logger.log(Level.INFO, LocalDateTime.now(), logMessage);
        String line = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(logger.getCurrentFilePath())))) {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 7; i++) {
            logger.log(Level.INFO, LocalDateTime.now(), logMessage);
        }

        long maxLogsInFile = logger.getOptions().getMaxFileSizeBytes() / line.getBytes().length;
        long expected = 8 % maxLogsInFile == 0 ? 8 / maxLogsInFile : 8 / maxLogsInFile + 1;

        File directory = new File(DIRECTORY.toString());
        File[] files = Objects.requireNonNull(directory.listFiles());
        long actual = 0;
        for (File f : files) {
            if (f.isFile() && !f.getName().equals("parse.txt") && f.getName().contains(".txt")) {
                actual++;
            }
        }

        assertEquals(expected, actual, "log does not create the right count of log files");
    }

    @Test
    public void testLogThrowsExceptionWhenLevelParamIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> logger.log(null, LocalDateTime.now(), "ala bala keks"),
            "level should not be null");
    }

    @Test
    public void testLogThrowsExceptionWhenTimestampParamIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> logger.log(null, LocalDateTime.now(), "ala bala keks"),
            "timestamp should not be null");
    }

    @Test
    public void testLogThrowsExceptionWhenMessageParamIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> logger.log(null, LocalDateTime.now(), "ala bala keks"),
            "message should not be null");
    }

    @Test
    public void testLogThrowsExceptionWhenMessageParamIsEmptyString() {
        assertThrows(IllegalArgumentException.class,
            () -> logger.log(null, LocalDateTime.now(), "ala bala keks"),
            "IllegalArgumentException is expected to be thrown when message is an em[ty string");
    }

    @Test
    public void testLogThrowsExceptionWhenThrowingErrorsIsAllowed() {
        LoggerOptions options = new LoggerOptions(DefaultLogger.class, "\\fake");
        options.setShouldThrowErrors(true);
        logger = new DefaultLogger(options);
        assertThrows(LogException.class,
            () -> logger.log(Level.INFO, LocalDateTime.now(), "ala bala keks"),
            "Log exception is thrown when problems while logging occur");
    }

    @Test
    public void testLogSuppressProblemsWhenThrowingErrorsIsNotAllowed() {
        LoggerOptions options = new LoggerOptions(DefaultLogger.class, "\\fake");
        logger = new DefaultLogger(options);

        try {
            logger.log(Level.INFO, LocalDateTime.now(), "ala bala keks");
        } catch (LogException e) {
            fail("log should not throw exceptions when shouldThrowErrors is false");
        }

    }

    @Test
    public void testGetOptionsReturnsTheCorrectOptions() {
        LoggerOptions options = logger.getOptions();

        assertEquals(options.getMinLogLevel(), Level.INFO, "the minimal level severity is incorrect");
        assertEquals(options.getMaxFileSizeBytes(), FILE_SIZE, "the max file size is incorrect");
        assertEquals(options.getDirectory(), DIRECTORY.toString(), "the logging directory is incorrect");
        assertFalse(options.shouldThrowErrors());
    }

    @Test
    public void testGetCurrentFilePathWorksProperly() {
        logger.log(Level.INFO, LocalDateTime.now(), "ala bala keks");

        String expected = DIRECTORY + File.separator + "logs-0.txt";
        String actual = logger.getCurrentFilePath().toString();
        assertEquals(expected, actual, "getCurrentFilePath does not work properly");
    }
}

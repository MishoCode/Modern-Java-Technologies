package bg.sofia.uni.fmi.mjt.logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DefaultLogParser implements LogParser {
    public static final int LEVEL = 0;
    public static final int TIMESTAMP = 1;
    public static final int PACKAGE = 2;
    public static final int MESSAGE = 3;

    public static final String FROM_PARAM_NAME = "from";
    public static final String TO_PARAM_NAME = "to";
    public static final String LEVEL_PRAM_NAME = "level";

    private final Path logsFilePath;
    private List<Log> logs;

    public DefaultLogParser(Path logsFilePath) {
        this.logsFilePath = logsFilePath;
        File fileToParse = new File(String.valueOf(logsFilePath));
        try {
            fileToParse.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logs = new LinkedList<>();
        readLogs();
    }

    @Override
    public List<Log> getLogs(Level level) {

        assertNotNull(level, LEVEL_PRAM_NAME);

        List<Log> result = new LinkedList<>();
        readLogs();

        for (Log log : logs) {
            if (log.level().equals(level)) {
                result.add(log);
            }
        }

        return result;
    }

    @Override
    public List<Log> getLogs(LocalDateTime from, LocalDateTime to) {
        assertNotNull(from, FROM_PARAM_NAME);
        assertNotNull(to, TO_PARAM_NAME);

        List<Log> result = new LinkedList<>();
        readLogs();

        for (Log log : logs) {
            if (log.timestamp().isAfter(from) && log.timestamp().isBefore(to)) {
                result.add(log);
            }
        }

        return result;
    }

    @Override
    public List<Log> getLogsTail(int n) {
        assertNotNegative(n);

        readLogs();
        if (logs.size() < n) {
            return logs;
        }

        return logs.subList(logs.size() - n, logs.size());
    }

    private void readLogs() {
        logs = new LinkedList<>();
        try (BufferedReader reader = Files.newBufferedReader(logsFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\|");
                Level level = Level.valueOf(tokens[LEVEL].substring(1, tokens[LEVEL].length() - 1));
                LocalDateTime timestamp = LocalDateTime.parse(tokens[TIMESTAMP]);
                logs.add(new Log(level, timestamp, tokens[PACKAGE], tokens[MESSAGE]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void assertNotNull(Object object, String paramName) {
        if (object == null) {
            throw new IllegalArgumentException(paramName + " should not be null");
        }
    }

    private void assertNotNegative(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("N should not be negative");
        }
    }
}
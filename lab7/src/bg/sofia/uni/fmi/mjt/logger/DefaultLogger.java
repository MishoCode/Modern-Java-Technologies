package bg.sofia.uni.fmi.mjt.logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class DefaultLogger implements Logger {
    private final LoggerOptions loggerOptions;
    private String currentLogFileName;
    private int logFileNumber = 0;
    private int currentLogFileSize = 0;

    public DefaultLogger(LoggerOptions loggerOptions) {
        this.loggerOptions = loggerOptions;
        currentLogFileName = "logs-0.txt";
        File firstFile = new File(currentLogFileName);
        try {
            firstFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void log(Level level, LocalDateTime timestamp, String message) {
        if (level == null || timestamp == null || message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Arguments cannot be null or empty strings");
        }

        if (level.getLevel() < loggerOptions.getMinLogLevel().getLevel()) {
            return;
        }

        String line = String.format("[%s]|%s|%s|%s\n",
            level, timestamp, loggerOptions.getClazz().getPackageName(), message);
        long nextLogLength = line.getBytes(StandardCharsets.UTF_8).length;
        if (currentLogFileSize + nextLogLength > loggerOptions.getMaxFileSizeBytes()) {
            currentLogFileName = nextFile(currentLogFileName);
        }

        try (Writer writer = Files.newBufferedWriter(getCurrentFilePath(), StandardOpenOption.APPEND)) {
            writer.write(line);
            writer.flush();

            currentLogFileSize += nextLogLength;
        } catch (IOException e) {
            if (loggerOptions.shouldThrowErrors()) {
                throw new LogException("Error while logging the message");
            }
        }
    }

    @Override
    public LoggerOptions getOptions() {
        return loggerOptions;
    }

    @Override
    public Path getCurrentFilePath() {
        return Path.of(loggerOptions.getDirectory() + File.separator + currentLogFileName).toAbsolutePath();
    }

    private String nextFile(String currentLogFile) {
        logFileNumber++;
        currentLogFileSize = 0;

        int index = currentLogFile.lastIndexOf('-');
        return currentLogFile.substring(0, index) + "-" + logFileNumber + ".txt";
    }
}
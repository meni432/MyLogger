package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileLoggingHandler implements LoggingInterface {
    private final ExecutorService m_loggingExecutor;
    private final String m_fileName;

    public FileLoggingHandler(String fileName) throws IOException {
        m_loggingExecutor = Executors.newSingleThreadExecutor();
        m_fileName = fileName;
        if (!Files.exists(Paths.get(fileName)))
        {
            Files.createFile(Paths.get(fileName));
        }
    }

    @Override
    public void info(String s) {
        writeToFile("[INFO] " + s);
    }

    @Override
    public void warning(String s) {
        writeToFile("[WARNING] " + s);
    }

    @Override
    public void error(String s) {
        writeToFile("[ERROR] " + s);
    }

    /**
     * here use in executor because is big operation to write file, so we want free thread as soon as possible
     * @param s
     */
    private void writeToFile(String s) {
        m_loggingExecutor.submit(() -> {
            try {
                Files.write(
                        Paths.get(m_fileName),
                        (s + "\n").getBytes(),
                        StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

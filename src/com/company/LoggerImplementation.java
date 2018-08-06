package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LoggerImplementation implements Logger{
    private static Lock m_instanceLock = new ReentrantLock();
    private static LoggerImplementation m_ourInstance;

    private final ExecutorService m_loggingExecutor;
    private final List<LoggingInterface> m_loggingInterfaces;

    public static LoggerImplementation getInstance() {
        if (m_ourInstance == null) {
            m_instanceLock.lock();
            try {
                if (m_ourInstance == null) {
                    m_ourInstance = new LoggerImplementation();
                }
            } finally {
                m_instanceLock.unlock();
            }
        }
        return m_ourInstance;
    }

    private LoggerImplementation() {
        m_loggingExecutor = Executors.newSingleThreadExecutor();
        m_loggingInterfaces = new ArrayList<>();
    }

    public void registerLoggingInterface(LoggingInterface loggingInterface) {
        m_loggingExecutor.submit(() -> {
            if (!m_loggingInterfaces.contains(loggingInterface)) {
                m_loggingInterfaces.add(loggingInterface);
            }
        });
    }

    @Override
    public void info(String s) {
        m_loggingExecutor.submit(() -> {
            for (LoggingInterface loggingInterface : m_loggingInterfaces) {
                loggingInterface.info(s);
            }
        });
    }

    @Override
    public void warning(String s) {
        m_loggingExecutor.submit(() -> {
            for (LoggingInterface loggingInterface : m_loggingInterfaces) {
                loggingInterface.warning(s);
            }
        });
    }

    @Override
    public void error(String s) {
        m_loggingExecutor.submit(() -> {
            for (LoggingInterface loggingInterface : m_loggingInterfaces) {
                loggingInterface.error(s);
            }
        });
    }
}

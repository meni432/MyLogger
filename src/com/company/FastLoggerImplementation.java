package com.company;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FastLoggerImplementation implements Logger{
    private static Lock m_instanceLock = new ReentrantLock();
    private static FastLoggerImplementation m_ourInstance;

    private final ExecutorService m_loggingExecutor;
    private final List<LoggingInterface> m_loggingInterfaces;
    private final Map<LoggingInterface, ExecutorService> m_loggingInterfaceExecutorServiceMap;

    public static FastLoggerImplementation getInstance() {
        if (m_ourInstance == null) {
            m_instanceLock.lock();
            try {
                if (m_ourInstance == null) {
                    m_ourInstance = new FastLoggerImplementation();
                }
            } finally {
                m_instanceLock.unlock();
            }
        }
        return m_ourInstance;
    }

    private FastLoggerImplementation() {
        m_loggingExecutor = Executors.newSingleThreadExecutor();
        m_loggingInterfaces = new ArrayList<>();
        m_loggingInterfaceExecutorServiceMap = new HashMap<>();
    }

    public void registerLoggingInterface(LoggingInterface loggingInterface) {
        m_loggingExecutor.submit(() -> {
            if (!m_loggingInterfaces.contains(loggingInterface)) {
                m_loggingInterfaces.add(loggingInterface);
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                m_loggingInterfaceExecutorServiceMap.put(loggingInterface, executorService);
            }
        });
    }

    @Override
    public void info(String s) {
        m_loggingExecutor.submit(() -> {
            for (LoggingInterface loggingInterface : m_loggingInterfaces) {
                ExecutorService loggingInterfaceExecutorService = m_loggingInterfaceExecutorServiceMap.get(loggingInterface);
                loggingInterfaceExecutorService.submit(() ->
                {
                   loggingInterface.info(s);
                });
            }
        });
    }

    @Override
    public void warning(String s) {
        m_loggingExecutor.submit(() -> {
            for (LoggingInterface loggingInterface : m_loggingInterfaces) {
                ExecutorService loggingInterfaceExecutorService = m_loggingInterfaceExecutorServiceMap.get(loggingInterface);
                loggingInterfaceExecutorService.submit(() ->
                {
                    loggingInterface.warning(s);
                });
            }
        });
    }

    @Override
    public void error(String s) {
        m_loggingExecutor.submit(() -> {
            for (LoggingInterface loggingInterface : m_loggingInterfaces) {
                ExecutorService loggingInterfaceExecutorService = m_loggingInterfaceExecutorServiceMap.get(loggingInterface);
                loggingInterfaceExecutorService.submit(() ->
                {
                    loggingInterface.error(s);
                });
            }
        });
    }
}

package com.company;

import java.io.IOException;

public class Main {

    static class ExampleWriter implements Runnable {

        private final Logger m_logger;
        private final String m_name;

        public ExampleWriter(Logger logger, String name) {
            m_name = name;
            m_logger = logger;
        }

        @Override
        public void run() {
            while(true) {
                double randomValue = Math.random();
                long time = System.currentTimeMillis();
                m_logger.info( m_name + " :: some info " + time);
                try {
                    Thread.sleep((long)(randomValue * 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Logger logger = FastLoggerImplementation.getInstance();
        logger.registerLoggingInterface(new PrintlnLoggingHandler());
        logger.registerLoggingInterface(new FileLoggingHandler("log-file.txt"));
        logger.registerLoggingInterface(new SlowPrintlnLoggingHandler());
        for (int i = 0; i < 10; i ++)
        {
            new Thread(new ExampleWriter(logger, "name " + i)).start();
        }
    }
}

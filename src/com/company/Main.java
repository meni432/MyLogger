package com.company;

import java.io.IOException;

public class Main {

    static class ExampleWriter implements Runnable {

        private final Logger m_logger = LoggerImplementation.getInstance();
        private final String m_name;

        public ExampleWriter(String name) {
            m_name = name;
        }

        @Override
        public void run() {
            while(true) {
                double randomValue = Math.random();
                long time = System.currentTimeMillis();
                m_logger.info( m_name + " :: some info " + time);
//                try {
//                    Thread.sleep((long)(randomValue * 100));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Logger logger = LoggerImplementation.getInstance();
        logger.registerLoggingInterface(new PrintlnLoggingHandler());
        logger.registerLoggingInterface(new FileLoggingHandler("log-file.txt"));
        for (int i = 0; i < 10; i ++)
        {
            new Thread(new ExampleWriter("name " + i)).start();
        }
    }
}

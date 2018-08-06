package com.company;

import java.util.concurrent.TimeUnit;

public class SlowPrintlnLoggingHandler implements LoggingInterface {
    @Override
    public void info(String s) {
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("[slow-INFO] " + s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void warning(String s) {
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("[slow-WARNING] " + s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void error(String s) {
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("[slow-ERROR] " + s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

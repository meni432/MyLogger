package com.company;

public class PrintlnLoggingHandler implements LoggingInterface {


    @Override
    public void info(String s) {
        System.out.println("[INFO] " + s);
    }

    @Override
    public void warning(String S) {
        System.out.println("[WARNING] " + S);
    }

    @Override
    public void error(String s) {
        System.err.println("ERROR] " + s);
    }
}

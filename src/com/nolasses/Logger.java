package com.nolasses;


//simple logger that routes all logs and console 'info' prints through this class
public class Logger {
    private boolean coreAppRunning = true;

    // to print errors
    public void error(Errors.elist code, String source, String log){
        System.err.println("Error " + code + " at " + source + ": " + log);
    }

    // general log
    public void log(String source, String log){
        System.out.println(source + ": " + log);
    }

    // app state, this is set to false when a fatal error breaks the app
    public void coreApp(boolean state) {
        coreAppRunning = state;
    }
}

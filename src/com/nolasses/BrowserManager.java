package com.nolasses;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

// BrowserManager to handle links when the timer rings.
public class BrowserManager {
    Logger logger = new Logger();
    public ArrayList<String> history = new ArrayList<String>();

    //openLink opens the link and logs the link URL and prints a system Log
    public void openLink(String link, String source) throws URISyntaxException, IOException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(link));
            StoreLog(link, Long.toString(System.currentTimeMillis()));
            logger.log(this.getClass().getSimpleName(), "BrowserManager opened a link: " + link);
        }
    }
    //simple method that appends the URL to the history arraylist
    public void StoreLog(String URL, String time){
        history.add(URL + " UTC:" + time);
    }

    //dumps the history arraylist onto the console *debugging tool*
    public void dumpLog(){
        logger.log(this.getClass().getSimpleName(), " BrowserManager's log was dumped (SOpln)");
        for (String item : history) {
            System.out.println(item);
        }
    }

    //returns the log, if any UI elements need the log
    public ArrayList getLog(){
        logger.log(this.getClass().getSimpleName(), "BrowserManager.getLog() was called");
        return history;
    }
}

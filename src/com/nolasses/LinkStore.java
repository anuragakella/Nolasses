package com.nolasses;

import java.util.HashMap;
import java.util.Map;


public class LinkStore {
    // this class manages links and stores them in a dictionary
    // acts as an interface / API to add / remove / manage links.

    // creating a logger object to log any errors / messages
    Logger logger = new Logger();

    Map<String, String> links = new HashMap<String, String>();
    public boolean mapInUse = false;
    int links_length;
    // add a link to the dictionary
    public int addLink(String time, String link){ // add a link, returns 1 if successful, else prints a stack trace and returns 0;
        try {
            mapInUse = true;
            links.put(time, link);
            logger.log(this.getClass().getSimpleName(), "added link: " + link);
            mapInUse = false;
            return 1;
        } catch (Exception e){
            logger.coreApp(false);
            logger.error(Errors.elist.UNKNOWN, this.getClass().getSimpleName(), "an exception occurred");
            e.printStackTrace();
            return 0;
        }
    }
    public int removeLink(String time){ // removes a link, returns 1 if successful, else prints a stack trace and returns 0
        try {
            mapInUse = true;
            logger.log(this.getClass().getSimpleName(), "removed link: " + links.get(time));
            links.remove(time);
            mapInUse = false;
            return 1;
        } catch (Exception e){
            logger.coreApp(false); // sets coreApprunning to false.
            logger.error(Errors.elist.UNKNOWN, this.getClass().getSimpleName(), "an exception occurred");
            e.printStackTrace();
            return 0;
        }

    }

    // method to dump the whole dictionary (for debugging)
    public void dumpLinks(){
        mapInUse = true;
        for (String key : links.keySet()) {
            System.out.println("--- LIST ---");
            System.out.println(key + ": " + links.get(key));
            System.out.println("--- **** ---");
        }
        mapInUse = false;
    }

    public Map<String, String> getLinks(){
        return links;
    }

    public void clearLinks() {
        this.links.clear();
    }
}

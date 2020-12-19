package com.nolasses;


import javax.swing.text.Style;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

//class that handles the timing and opening of links
//this class takes links from the link store and runs a timer and passes the link to the browsermanager when the timer is up.
public class LinkTracker implements Runnable {
    Map<String, String> links, oldLinks;
    BrowserManager browserManager = new BrowserManager();
    public NolassesManager nolassesManager;
    boolean nolassesSet = false;

    public LinkTracker(Map<String, String> links){
        this.links = links;
        this.oldLinks = links;
    }

    public void updateLinks(Map<String, String> links){
        this.links = links;
        for (String key : links.keySet()){
            if(oldLinks.containsKey(key))
                continue;
            else
                logger.log(this.getClass().getSimpleName(), "new link was added: " + links.get(key));
        }
    }

    private String getFormattedTime(){
        int second = LocalTime.now().getSecond();
        int minute = LocalTime.now().getMinute();
        int hour = LocalTime.now().getHour();
        return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);
    }

    Logger logger = new Logger();
    public void timeLoop(){
        while(true){
            String currentTimeStr = getFormattedTime();
            if(nolassesSet){
                this.nolassesManager.updateTime(currentTimeStr);
            }
            matchLinks(currentTimeStr, this.links, this.browserManager);
            try {
                Thread.sleep(1000);
            } catch (Exception e){
                logger.coreApp(false);
                logger.error(Errors.elist.UNKNOWN, this.getClass().getSimpleName(), " an exception occurred: " + e);
                e.printStackTrace();
            }
        }
    }

    public void matchLinks(String time, Map<String, String> links, BrowserManager browserManager){
        if(links.containsKey(time)){
            try {
                browserManager.openLink(links.get(time), this.getClass().getSimpleName());
                logger.log(this.getClass().getSimpleName(), "BrowserManager was called");
                if(nolassesSet){
                    this.nolassesManager.expireLink(time, links.get(time));
                }
            } catch (Exception e){
                logger.coreApp(false);
                logger.error(Errors.elist.UNKNOWN, this.getClass().getSimpleName(), " an exception occurred: " + e);
                e.printStackTrace();
            }
        }
    }

    public void setNolassesManager(NolassesManager nolassesManager){
        nolassesSet = true;
        this.nolassesManager = nolassesManager;
    }

    @Override
    public void run() {
        timeLoop();
    }
}

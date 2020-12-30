package com.nolasses;

public class NolassesBridge {
    NolassesManager nolassesManager;
    LinkTracker linkTracker;
    LinkStore linkStore;
    //create objects and start the UI
    public  NolassesBridge(){
        linkStore = new LinkStore();

        //pass a linkstore to the linktracker
        linkTracker = new LinkTracker(linkStore.getLinks());
        nolassesManager = new NolassesManager(linkTracker, linkStore);

        //create a new thread for the linktracker
        Thread nolassesThread = new Thread(nolassesManager.linkTracker);
        nolassesThread.start();

        //launch UI
        nolassesManager.launchWindow();
        nolassesManager.linkTracker.updateLinks(nolassesManager.linkStore.links);

        //point nolassesmanager's linktracker to its parent - nolasses manager
        nolassesManager.linkTracker.setNolassesManager(nolassesManager);
    }
}

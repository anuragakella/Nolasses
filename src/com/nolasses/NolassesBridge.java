package com.nolasses;

public class NolassesBridge {
    NolassesManager nolassesManager;
    LinkTracker linkTracker;
    LinkStore linkStore;
    public  NolassesBridge(){
        linkStore = new LinkStore();
        linkTracker = new LinkTracker(linkStore.getLinks());
        nolassesManager = new NolassesManager(linkTracker, linkStore);
        Thread nolassesThread = new Thread(nolassesManager.linkTracker);
        nolassesThread.start();
        nolassesManager.launchWindow();
        nolassesManager.linkTracker.updateLinks(nolassesManager.linkStore.links);
        nolassesManager.linkTracker.setNolassesManager(nolassesManager);
    }
}

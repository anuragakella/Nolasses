package com.nolasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Map;

// Class that manages the UI
public class NolassesManager {
    JFrame mainFrame = new JFrame();
    JPanel mainPanel = new JPanel();
    Logger logger = new Logger();
    LinkTracker linkTracker;
    LinkStore linkStore;
    private JLabel timeLabel;
    JLabel getStartedLabel;
    ArrayList<LinkLabel> linksList = new ArrayList<>();
    private static boolean isGetstarted = false;
    static int mainY = 60;

    // initialize Jframes and set look and feel
    // also connect linktracker and linkstore objects to this class
    public NolassesManager(LinkTracker linkTracker, LinkStore linkStore){
        mainFrame.setTitle("Nolasses");
        mainFrame.setSize(800, 500);
        mainFrame.setLayout(null);
        mainFrame.setResizable(false);
        this.linkTracker = linkTracker;
        this.linkStore = linkStore;
        mainPanel.setSize(800, 500);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // draws a link from the parsed link onto the Jframe
    // mouse event handles a double click to delete the link
    private void drawLink(String link, String time){
        linkStore.addLink(time, link);
        linkTracker.updateLinks(linkStore.getLinks());
        JLabel temp = new JLabel();
        temp.setText(link + " " + time);

        temp.setFont(new Font("Consolas", Font.PLAIN, 20));
        temp.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    mainPanel.remove(e.getComponent());
                    for (LinkLabel linkLabel : linksList) {
                        if(linkLabel.label == e.getComponent()) {
                            linksList.remove(linkLabel);
                            linkStore.removeLink(linkLabel.time);
                            break;
                        }
                    }
                    paintLinks();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        });
        LinkLabel linkLabel = new LinkLabel(temp, time, link);
        this.linksList.add(linkLabel);
        this.paintLinks();
    }

    // this menthod is called after a link expires, adds a strike through to the link label's text
    // double click to remove still works
    public void expireLink(String time, String link){
        Font font = new Font("Consolas", Font.PLAIN, 20);
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        Font newFont = new Font(attributes);
        System.out.println(link);
        for (LinkLabel linkLabel : linksList){
            if((linkLabel.link.compareTo(link)) == 0){
                if(linkLabel.time.compareTo(time) == 0){
                    linkLabel.label.setFont(newFont);
                    paintLinks();
                    break;
                }
            }
        }
    }


    // paints links, or repaints them when called, uses the links arraylist to paint labels
    private void paintLinks() {
        this.mainY = 60;
        mainPanel.setVisible(false);
        if (linksList.size() == 0) {
            getStarted();
        }
        for (LinkLabel linkLabel : linksList){
            linkLabel.label.setBounds(10, this.mainY, 500, 30);
            this.mainY += 30;
            mainPanel.add(linkLabel.label);
        }
        mainPanel.setVisible(true);
    }


    // these 3 methods add or remove the getStarted label
    private void getStarted(){
        if(!isGetstarted) {
            this.isGetstarted = true;
            getStartedLabel = new JLabel("paste a link above to get started");
            getStartedLabel.setFont(new Font("Consolas", Font.PLAIN, 20));
            getStartedLabel.setBounds(10, this.mainY, 800, 30);
            mainPanel.add(getStartedLabel);
        }
    }

    private void updateGetStarted(String text){
        getStartedLabel = new JLabel(text);
        getStartedLabel.setFont(new Font("Consolas", Font.PLAIN, 20));
        getStartedLabel.setBounds(10, this.mainY, 800, 30);
        mainPanel.add(getStartedLabel);
    }

    private void removeGetStarted(){
        if(isGetstarted) {
            this.isGetstarted = false;
            mainPanel.setVisible(false);
            mainPanel.remove(getStartedLabel);
            mainPanel.setVisible(true);
        }
    }
    
    // link text is: link + " " + time
    // this method looks for the space, splits the string and passes it onto drawLink which creates a Jlabel for the link
    private void parseLinkText(String text) {
        int split = text.indexOf(' ');
        String link = text.substring(0, split);
        String time = text.substring(split + 1, text.length());
        drawLink(link, time);
    }

    // API / abstract method to update the timer label everysecond
    public void updateTime(String time){
        this.timeLabel.setText(time);
    }

    // main functional method that launches the window and sets the Jframe and panel elements
    public void launchWindow(){

        // the add a new link section
        // plus button to add link, text field to type
        // x button to cleat
        // and a reset everything button to restart the app
        JButton addButton = new JButton("+");
        addButton.setFont(new Font("Sans", Font.PLAIN, 15));
        addButton.setBounds(703, 9, 52, 24);
        mainPanel.add(addButton);


        JTextField linkField = new JTextField();
        linkField.setFont(new Font("Sans", Font.PLAIN, 10));
        linkField.setBounds(9, 10, 693, 22);
        mainPanel.add(linkField);

        JButton clearButton = new JButton("x");
        clearButton.setBounds(755, 9, 24, 24);
        clearButton.setMargin(new Insets(0,0,0,0));
        mainPanel.add(clearButton);

        JButton resetButton = new JButton("reset everything");
        resetButton.setBounds(653, 430, 120, 20);
        resetButton.setMargin(new Insets(1,1,1,1));
        resetButton.setFont(new Font("Sans", Font.PLAIN, 9));
        mainPanel.add(resetButton);

        // a hint that shows the input format for the link
        JLabel linkHintLabel = new JLabel("Paste a link here, then a space then type a time (HH:MM:SS). Press + to add, x to clear | double click on a link to remove it.");
        linkHintLabel.setBounds(10, 30, 600, 22);
        linkHintLabel.setFont(new Font("Sans", Font.PLAIN, 9));
        mainPanel.add(linkHintLabel);

        // timer label that is updated every second trough link tracker
        timeLabel = new JLabel("11:02:44");
        timeLabel.setFont(new Font("Consolas", Font.PLAIN, 100));
        timeLabel.setBounds(10, 375, 500, 100);
        mainPanel.add(timeLabel);

        // event listeners
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                linkField.setText("");
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeGetStarted();
                parseLinkText(linkField.getText());
                linkField.setText("");
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                linkStore.clearLinks();
                for(LinkLabel l : linksList){
                    mainPanel.remove(l.label);
                }
                linksList = new ArrayList<>();
                paintLinks();
            }
        });


        // starting the UI
        getStarted();

        mainFrame.add(mainPanel);
        mainPanel.setLayout(null);

        mainFrame.setVisible(true);
        mainPanel.setVisible(true);
    }



}

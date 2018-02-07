package com.example.mohamed.musicplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by CompuCity on 2/7/2018.
 */

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> Features = new ArrayList<String>();
        Features.add("smart playlist");
        Features.add("ringtone cutter");
        Features.add("pi power share");
        Features.add("equalizer");
        Features.add("sleep timer");

        List<String> Customization = new ArrayList<String>();
        Customization.add("Settings");
        Customization.add("Store");

        List<String> Help = new ArrayList<String>();
        Help.add("Introduction");
        Help.add("Send Feedback");

        List<String> About = new ArrayList<String>();
        About.add("Invite Friends");
        About.add("Follow Us");
        About.add("Info");
        About.add("Rate this App");


        expandableListDetail.put("Features", Features);
        expandableListDetail.put("Customization", Customization);
        expandableListDetail.put("Help", Help);
        expandableListDetail.put("viewPagerAdapter", About);

        return expandableListDetail;
    }
}

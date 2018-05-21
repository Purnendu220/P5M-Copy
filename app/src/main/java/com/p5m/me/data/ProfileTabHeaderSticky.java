package com.p5m.me.data;

import com.brandongogetap.stickyheaders.exposed.StickyHeader;

public class ProfileTabHeaderSticky implements StickyHeader {

    private int tabCount;

    private String[] tabs;

    private int selectedTab;

    public ProfileTabHeaderSticky(String[] tabs) {
        this.tabs = tabs;
    }

    public ProfileTabHeaderSticky(String[] tabs, int selectedTab) {
        this.tabs = tabs;
        this.selectedTab = selectedTab;
    }

    public ProfileTabHeaderSticky() {
    }

    public int getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(int selectedTab) {
        this.selectedTab = selectedTab;
    }

    public int getTabCount() {
        return tabCount;
    }
}

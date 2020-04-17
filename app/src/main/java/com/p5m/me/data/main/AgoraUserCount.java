package com.p5m.me.data.main;

public class AgoraUserCount {

    private boolean channel_exist;
    private int mode;
    private int[] broadcasters;
    private int[] audience;
    private int audience_total;


    public boolean isChannel_exist() {
        return channel_exist;
    }

    public void setChannel_exist(boolean channel_exist) {
        this.channel_exist = channel_exist;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int[] getBroadcasters() {
        return broadcasters;
    }

    public void setBroadcasters(int[] broadcasters) {
        this.broadcasters = broadcasters;
    }

    public int[] getAudience() {
        return audience;
    }

    public void setAudience(int[] audience) {
        this.audience = audience;
    }

    public int getAudience_total() {
        return audience_total;
    }

    public void setAudience_total(int audience_total) {
        this.audience_total = audience_total;
    }
}

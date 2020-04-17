package com.p5m.me.data.main;

public class AgoraUserStatus {

    private long join;
    private boolean in_channel;
    private int role;

    public long getJoin() {
        return join;
    }

    public void setJoin(long join) {
        this.join = join;
    }

    public boolean isIn_channel() {
        return in_channel;
    }

    public void setIn_channel(boolean in_channel) {
        this.in_channel = in_channel;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}

package com.p5m.me.data.request;

public class DeviceUpdate implements java.io.Serializable {
    private static final long serialVersionUID = 1831731815770097684L;

    private String deviceType = "android";
    private String appVersion;
    private int userId;
    private String deviceToken;

    public DeviceUpdate(String appVersion, int userId, String deviceToken) {
        this.appVersion = appVersion;
        this.userId = userId;
        this.deviceToken = deviceToken;
    }
}

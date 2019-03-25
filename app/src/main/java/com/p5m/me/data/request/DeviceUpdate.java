package com.p5m.me.data.request;

public class DeviceUpdate implements java.io.Serializable {
    private static final long serialVersionUID = 1831731815770097684L;

    private String deviceType = "android";
    private String appVersion;
    private int userId;
    private String deviceToken;
    private String androidId;
    private String osVersion;


    public DeviceUpdate(String appVersion, int userId, String deviceToken, String androidId, String osVersion) {
        this.appVersion = appVersion;
        this.userId = userId;
        this.deviceToken = deviceToken;
        this.androidId = androidId;
        this.osVersion = osVersion;
    }
}

package bean;

public class RenHeUserInfo {
    private String deviceId;
    private boolean isAllowed;

    public RenHeUserInfo() {
    }

    public RenHeUserInfo(String deviceId, boolean isAllowed) {
        this.deviceId = deviceId;
        this.isAllowed = isAllowed;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isAllowed() {
        return isAllowed;
    }

    public void setAllowed(boolean allowed) {
        isAllowed = allowed;
    }
}


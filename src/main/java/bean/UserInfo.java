package bean;

/**
 *  数据库中用户信息数据类
 */
public class UserInfo {
    private String account;
    private String password;
    private String genkey;
    private String deviceId;
//    private String matchcode;
    private String name;
    // 1 is male, 0 is female;
    private boolean sex;
    private String birthday;
    private String loveAuth;
    private String partnerAccount;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGenkey() {
        return genkey;
    }

    public void setGenkey(String genkey) {
        this.genkey = genkey;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLoveAuth() {
        return loveAuth;
    }

    public void setLoveAuth(String loveAuth) {
        this.loveAuth = loveAuth;
    }

    public String getPartnerAccount() {
        return partnerAccount;
    }

    public void setPartnerAccount(String partnerAccount) {
        this.partnerAccount = partnerAccount;
    }
}

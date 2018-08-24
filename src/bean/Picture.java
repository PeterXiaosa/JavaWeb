package bean;

import java.util.Date;

public class Picture {
    private String url;
    private String updateTime;

    public Picture(String url, String updateTime){
        this.url = url;
        this.updateTime = updateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}

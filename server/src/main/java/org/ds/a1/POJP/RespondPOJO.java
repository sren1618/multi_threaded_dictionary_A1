package org.ds.a1.POJP;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: RespondJSON
 * Package: org.ds.a1
 * @Author Shiqiang Ren
 * @Create 27/3/2023 6:45 pm
 * @Version 1.0
 */
public class RespondPOJO {
    private int status;
    private String info;
    private Map<String, String> data = new HashMap<>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

}

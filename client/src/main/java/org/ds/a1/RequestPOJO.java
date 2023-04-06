package org.ds.a1;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: RequestPOJO
 * Package: org.ds.a1
 * @Author Shiqiang Ren
 * @StudentID 1348968
 * @Create 27/3/2023 6:31 pm
 * @Version 1.0
 */
public class RequestPOJO {

    private String requestType;
    private Map<String, String> data = new HashMap<>();

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}

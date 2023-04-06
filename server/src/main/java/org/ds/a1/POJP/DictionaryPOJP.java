package org.ds.a1.POJP;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: Dictionary
 * Package: org.ds.a1
 * @Author Shiqiang Ren
 * @Create 24/3/2023 12:40 pm
 * @Version 1.0
 */
public class DictionaryPOJP {

    private String title;
    private String version;
    private String author;
    private ConcurrentHashMap<String, String> records = new ConcurrentHashMap<>();






    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ConcurrentHashMap<String, String> getRecords() {
        return records;
    }

    public void setRecords(ConcurrentHashMap<String, String> records) {
        this.records = records;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

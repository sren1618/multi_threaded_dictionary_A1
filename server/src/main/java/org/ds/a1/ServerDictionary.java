package org.ds.a1;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.ds.a1.POJP.DictionaryPOJP;
import org.ds.a1.POJP.RespondPOJO;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: ServerLoadData
 * Package: org.ds.a1
 * @Author Shiqiang Ren
 * @StudentID 1348968
 * @Create 23/3/2023 11:46 am
 * @Version 1.0
 */
public class ServerDictionary {
    static String dictionaryFilename;
    static DictionaryPOJP dictionaryPOJP = null;

    //load dictionary file to memory
    public static boolean readJsonFile(String filename) throws IOException{
        dictionaryFilename = filename;
        String jsonStr = "";
        //File jsonFile = new File("./server/"+dictionaryFilename);
        File jsonFile = new File("./"+dictionaryFilename);
        FileReader fileReader = new FileReader(jsonFile);
        Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
        int ch = 0;
        StringBuffer sb = new StringBuffer();
        while ((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }
        fileReader.close();
        reader.close();
        jsonStr = sb.toString();
        dictionaryPOJP = JSON.parseObject(jsonStr, DictionaryPOJP.class);
        return dictionaryPOJP != null;
    }

    //write memory dictionary data to disk
    public static void writeJsonFile() {
        String jsonStr = "";
            File jsonFile = new File("./"+ dictionaryFilename);
            if(!jsonFile.exists()){
                jsonFile.delete();
            }
        Writer writer = null;
        String dictionaryPOJPToJson = JSON.toJSONString(dictionaryPOJP);
        try {
            writer = new OutputStreamWriter(new FileOutputStream(jsonFile),"utf-8");
            writer.write(dictionaryPOJPToJson);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static JSONObject query(JSONObject request) {
        String word = (String) JSONObject.from(request.get("data")).get("word");
        ConcurrentHashMap<String, String> records = dictionaryPOJP.getRecords();
        RespondPOJO respondPOJO = new RespondPOJO();
        if (records.containsKey(word)) {
            respondPOJO.setStatus(1);
            respondPOJO.setInfo("query successfully");
            respondPOJO.getData().put("word",word);
            respondPOJO.getData().put("meanings",records.get(word));
        } else {
            respondPOJO.setStatus(0);
            respondPOJO.setInfo("The word does not exist!");
        }
        return JSONObject.from(respondPOJO);
    }

    public static JSONObject add(JSONObject request) {
        String word = (String) JSONObject.from(request.get("data")).get("word");
        String meanings = (String) JSONObject.from(request.get("data")).get("meanings");
        ConcurrentHashMap<String, String> records = dictionaryPOJP.getRecords();
        RespondPOJO respondPOJO = new RespondPOJO();
        if (records.containsKey(word)) {
            respondPOJO.setStatus(0);
            respondPOJO.setInfo("The word already exit!");
        } else {
            respondPOJO.setStatus(1);
            records.put(word, meanings);
            respondPOJO.setInfo("The word added successfully!");
            writeJsonFile();
        }
        return JSONObject.from(respondPOJO);
    }

    public static JSONObject delete(JSONObject request) {
        String word = (String) JSONObject.from(request.get("data")).get("word");
        ConcurrentHashMap<String, String> records = dictionaryPOJP.getRecords();
        RespondPOJO respondPOJO = new RespondPOJO();
        if (records.containsKey(word)) {
            records.remove(word);
            respondPOJO.setStatus(1);
            respondPOJO.setInfo("The word deleted successfully!");
            writeJsonFile();
        } else {
            respondPOJO.setStatus(0);
            respondPOJO.setInfo("The word does not exit!");
        }
        return JSONObject.from(respondPOJO);
    }

    public static JSONObject update(JSONObject request) {
        String word = (String) JSONObject.from(request.get("data")).get("word");
        String meanings = (String) JSONObject.from(request.get("data")).get("meanings");
        ConcurrentHashMap<String, String> records = dictionaryPOJP.getRecords();
        RespondPOJO respondPOJO = new RespondPOJO();
        if (records.containsKey(word)) {
            records.put(word,meanings );
            respondPOJO.setStatus(1);
            respondPOJO.getData().put("word",word);
            respondPOJO.getData().put("meanings",records.get(word));
            respondPOJO.setInfo("The word already Updated!");
            writeJsonFile();
        } else {
            respondPOJO.setStatus(0);
            respondPOJO.setInfo("The word does not exit!");
        }
        return JSONObject.from(respondPOJO);
    }
}






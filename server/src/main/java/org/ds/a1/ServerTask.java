package org.ds.a1;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.*;
import java.net.Socket;

/**
 * ClassName: ServerTask
 * Package: org.ds.a1
 * @Author Shiqiang Ren
 * @Create 22/3/2023 10:55 pm
 * @Version 1.0
 */
public class ServerTask implements Runnable{
    final Socket socket;
    public ServerTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while(true){
                String jsonString = reader.readLine();
                JSONObject request = JSON.parseObject(jsonString);
                    if (request != null) {
                        JSONObject respond = null;
                        String requestType = (String) request.get("requestType");
                        if (requestType.equals("QUERY")) {
                            respond = ServerDictionary.query(request);
                        } else if (requestType.equals("ADD")) {
                            respond = ServerDictionary.add(request);
                        } else if (requestType.equals("DELETE")) {
                            respond = ServerDictionary.delete(request);
                        } else if (requestType.equals("UPDATE")) {
                            respond = ServerDictionary.update(request);
                        } else if (requestType.equals("CLOSE")) {
                            System.out.println("User disconnected from IP ---" + socket.getInetAddress().getHostAddress());
                            return;
                        } else {
                            System.out.println("Request type can not be identified!");
                        }
                        String message = JSON.toJSONString(respond);
                        writer.write(message);
                        writer.newLine();
                        writer.flush();
                    }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

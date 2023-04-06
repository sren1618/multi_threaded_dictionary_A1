package org.ds.a1;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

/**
 * ClassName: ClientConnection
 * Package: org.ds.a1
 * @Author Shiqiang Ren
 * @StudentID 1348968
 * @Create 22/3/2023 6:02 pm
 * @Version 1.0
 */
public class ClientConnection extends Thread{

    //LinkedBlockingQueue<JSONObject> request = new LinkedBlockingQueue<>();
    LinkedList <JSONObject> requests = new LinkedList<>();
    private  JSONObject result;
    private  Boolean status = true;
    Socket socket;

    public ClientConnection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        if(socket!= null){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                while (!isInterrupted()) {
                    if (status && !requests.isEmpty()) {
                        synchronized (this) {
                            // send data
                            String jsonString = JSON.toJSONString(requests.poll());
                            writer.write(jsonString);
                            writer.newLine();
                            writer.flush();
                            System.out.println("Sent message to server: " + jsonString);
                            // receive data
                            String response = reader.readLine();
                            System.out.println("Received response from server: " + response);
                            this.result = JSON.parseObject(response);
                            this.notify();
                        }
                    }
                    if (!status) {
                        synchronized (this) {
                            try {
                                RequestPOJO requestPOJO = new RequestPOJO();
                                requestPOJO.setRequestType("CLOSE");
                                String jsonString = JSON.toJSONString(requestPOJO);
                                writer.write(jsonString);
                                writer.newLine();
                                writer.flush();
                                break;
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                }
            } catch (UnknownHostException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                if (e.getMessage().equals("Connection reset")) {
                    System.out.println("Server has shut down!");
                    System.out.println("Programme will exit!");
                    System.exit(0);
                } else {
                    System.err.println("IO error occurred: " + e.getMessage());
                }
            } finally {
                try {
                    if (socket != null)
                        socket.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    }

    public  JSONObject getResult() {
        if(result != null){
            return result;
        }else{
            JSONObject json = new JSONObject();
            json.put("status", 2);
            return json;
        }
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}

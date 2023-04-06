package org.ds.a1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ClassName: ServerConnection
 * Package: org.ds.a1
 * @Author Shiqiang Ren
 * @StudentID 1348968
 * @Create 22/3/2023 8:24 pm
 * @Version 1.0
 */
public class ServerConnection extends Thread{
    ServerSocket serverSocket;
    MyThreadsPool threadsPool;

    public ServerConnection(ServerSocket serverSocket, MyThreadsPool threadsPool) {
        this.serverSocket = serverSocket;
        this.threadsPool = threadsPool;
    }
    @Override
    public void run() {
        Socket socket;
        try {
            if (serverSocket != null) {
                while (true) {
                    socket = serverSocket.accept();
                    System.out.println("User connected from IP ---" + socket.getInetAddress().getHostAddress());
                    threadsPool.execute(new ServerTask(socket));
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (threadsPool != null) {
                threadsPool.closePool();
            }
        }
    }
}

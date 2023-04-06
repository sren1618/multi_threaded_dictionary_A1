package org.ds.a1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

/**
 * ClassName: ServerUi
 * Package: org.ds.a1
 * @Author Shiqiang Ren
 * @Create 4/4/2023 10:27 am
 * @Version 1.0
 */
public class ServerUi {
    private JTextField port;
    private JTextField fileName;
    private JTextField initWorkerNumber;
    private JTextField maxWorkerNumber;
    private JButton runServer;
    private JPanel panelMain;
    private JTextField workerWaitingTime;
    private ServerConnection serverConnection;
    private ServerSocket serverSocket;
    private MyThreadsPool threadsPool;

    public ServerUi() {
        runServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int portNumber;
                String dictionaryFilename;
                int initPoolSize;
                int maxPoolSize;
                int workerAliveTime;
                try {
                    portNumber = Integer.parseInt(port.getText());
                    dictionaryFilename = fileName.getText();
                    initPoolSize = Integer.parseInt(initWorkerNumber.getText());
                    maxPoolSize = Integer.parseInt(maxWorkerNumber.getText());
                    workerAliveTime = Integer.parseInt(workerWaitingTime.getText());
                    if(maxPoolSize < initPoolSize
                            || workerAliveTime == 0
                            || initPoolSize == 0) {
                        JOptionPane.showMessageDialog(panelMain, "The pool size is not appropriate!");
                    }else if (maxPoolSize <= 3){
                        JOptionPane.showMessageDialog(panelMain, "The max pool size at least is 5!");
                    }else if(ServerDictionary.readJsonFile(dictionaryFilename)){
                        serverSocket = new ServerSocket(portNumber);
                        if (ServerDictionary.dictionaryPOJP != null) {
                            threadsPool = new MyThreadsPool(initPoolSize, maxPoolSize, workerAliveTime*1000);
                        }
                        serverConnection = new ServerConnection(serverSocket,threadsPool);
                        serverConnection.start();
                        runServer.setText("server is running!");
                        runServer.setEnabled(false);
                        port.setEnabled(false);
                        fileName.setEnabled(false);
                        initWorkerNumber.setEnabled(false);
                        maxWorkerNumber.setEnabled(false);
                        workerWaitingTime.setEnabled(false);
                        JOptionPane.showMessageDialog(panelMain,"Server runs successfully!" );
                        System.out.println("Server runs successfully!");
                    }else{
                        throw new FileNotFoundException();
                    }
                } catch(NumberFormatException err) {
                    JOptionPane.showMessageDialog(panelMain,"Please check the inputs again please!" );
                } catch (BindException err) {
                    JOptionPane.showMessageDialog(panelMain,"Port number already be used!" );
                } catch ( FileNotFoundException err) {
                    JOptionPane.showMessageDialog(panelMain,"Dictionary File is not exist!\nor\nThe File name is not appropriate!\nCorrect example: dictionary.json "  );
                } catch (IOException ex) {
                    System.out.println(e);
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setContentPane(new ServerUi().panelMain);
        frame.setVisible(true);
        frame.pack();
        frame.setBounds(600,300,500,300);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to stop the server and exit?", "Stop Server?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    System.out.println("The server has shut down!");
                    System.exit(0);
                }
            }
        });
    }
}

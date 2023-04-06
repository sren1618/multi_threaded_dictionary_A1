package org.ds.a1;

import com.alibaba.fastjson2.JSONObject;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;

/**
 * ClassName: clientUi
 * Package: org.ds.a1
 * @Author Shiqiang Ren
 * @StudentID 1348968
 * @Create 22/3/2023 6:01 pm
 * @Version 1.0
 */
public class ClientUi {
    private JPanel panelMain;
    private JButton queryButton;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTextArea meaningsTextArea;
    private JTextField wordTextField;
    private JPanel panelButtons;
    private JPanel panelContent;
    private JLabel title;
    private JLabel word;
    private JLabel meanings;
    private JLabel info;
    private static ClientConnection cc = null;

    private final String regex = "^\\s*$";
    static String serverIPAddress;
    static String port;

    public ClientUi() {

        checkServerAndCreateConnectionThread();

        wordTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                wordTextField.setText("");
                meaningsTextArea.setText("");
            }
        });
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordTextField.getText();
                if (word.matches(regex)) {
                    JOptionPane.showMessageDialog(panelMain, "Please re-enter please!\n(Cannot be empty)");
                }else {
                    RequestPOJO requestPOJO = new RequestPOJO();
                    requestPOJO.setRequestType("QUERY");
                    requestPOJO.getData().put("word", word);
                    synchronized (cc) {
                        try {
                            cc.requests.add(JSONObject.from(requestPOJO));
                            cc.wait(); // waiting for the result of connection thread
                            JSONObject respond = JSONObject.from(cc.getResult());
                            if ((Integer) respond.get("status") == 1) {
                                meaningsTextArea.setText((String) JSONObject.from(respond.get("data")).get("meanings"));
                            } else if ((Integer) respond.get("status") == 0) {
                                JOptionPane.showMessageDialog(panelMain,respond.get("info") );
                            } else {
                                JOptionPane.showMessageDialog(panelMain,"Server responds error!\nProgramme will exit!\nPlease try again later!");
                                System.exit(0);
                            }
                        } catch (InterruptedException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordTextField.getText();
                String meanings = meaningsTextArea.getText();
                if (word.matches(regex) |meanings.matches(regex)) {
                    JOptionPane.showMessageDialog(panelMain, "Please re-enter please!\n(Cannot be empty)");
                }else {
                    RequestPOJO requestPOJO = new RequestPOJO();
                    requestPOJO.setRequestType("ADD");
                    requestPOJO.getData().put("word", word);
                    requestPOJO.getData().put("meanings", meanings);
                    synchronized (cc) {
                        try {
                            cc.requests.add(JSONObject.from(requestPOJO));
                            cc.wait(); // waiting for the result of connection thread
                            JSONObject respond = JSONObject.from(cc.getResult());
                            if ((Integer) respond.get("status") == 1) {
                                JOptionPane.showMessageDialog(panelMain,respond.get("info") );
                                wordTextField.setText("");
                                meaningsTextArea.setText("");
                            } else if ((Integer) respond.get("status") == 0) {
                                JOptionPane.showMessageDialog(panelMain,respond.get("info") );
                                wordTextField.setText("");
                                meaningsTextArea.setText("");
                            } else {
                                JOptionPane.showMessageDialog(panelMain,"Server responds error!\nProgramme will exit!\nPlease try again later!");
                                System.exit(0);
                            }
                        } catch (InterruptedException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordTextField.getText();
                if (word.matches(regex)) {
                    JOptionPane.showMessageDialog(panelMain, "Please re-enter please!\n(Cannot be empty)");
                }else {
                    RequestPOJO requestPOJO = new RequestPOJO();
                    requestPOJO.setRequestType("DELETE");
                    requestPOJO.getData().put("word", word);
                    synchronized (cc) {
                        try {
                            cc.requests.add(JSONObject.from(requestPOJO));
                            cc.wait(); // waiting for the result of connection thread
                            JSONObject respond = JSONObject.from(cc.getResult());
                            if ((Integer) respond.get("status") == 1) {
                                JOptionPane.showMessageDialog(panelMain,respond.get("info") );
                                wordTextField.setText("");
                                meaningsTextArea.setText("");
                            } else if ((Integer) respond.get("status") == 0) {
                                JOptionPane.showMessageDialog(panelMain,respond.get("info") );
                            } else {
                                JOptionPane.showMessageDialog(panelMain,"Server responds error!\nProgramme will exit!\nPlease try again later!");
                                System.exit(0);
                            }
                        } catch (InterruptedException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordTextField.getText();
                String meanings = meaningsTextArea.getText();
                if (word.matches(regex) |meanings.matches(regex)) {
                    JOptionPane.showMessageDialog(panelMain, "Please re-enter please!\n(Cannot be empty)");
                }else {
                    RequestPOJO requestPOJO = new RequestPOJO();
                    requestPOJO.setRequestType("UPDATE");
                    requestPOJO.getData().put("word", word);
                    requestPOJO.getData().put("meanings", meanings);
                    synchronized (cc) {
                        try {
                            cc.requests.add(JSONObject.from(requestPOJO));
                            cc.wait(); // waiting for the result of connection thread
                            JSONObject respond = JSONObject.from(cc.getResult());
                            if ((Integer) respond.get("status") == 1) {
                                meaningsTextArea.setText((String) JSONObject.from(respond.get("data")).get("meanings"));
                                JOptionPane.showMessageDialog(panelMain,respond.get("info") );
                            } else if ((Integer) respond.get("status") == 0) {
                                JOptionPane.showMessageDialog(panelMain,respond.get("info") );
                            } else {
                                JOptionPane.showMessageDialog(panelMain,"Server responds error!\nProgramme will exit!\nPlease try again later!");
                                System.exit(0);
                            }
                        } catch (InterruptedException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
            }
        });
    }
    public static void main(String[] args) {
        try{
            serverIPAddress = args[0];
            port = args[1];
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("The number of parameters is incorrect, please re-enter!");
        }
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setContentPane(new ClientUi().panelMain);
        frame.setVisible(true);
        frame.pack();
        frame.setBounds(600,300,500,300);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    synchronized (cc) {
                        cc.setStatus(false);
                        try {
                            cc.wait();
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                        System.exit(0);
                    }
                }
            }
        });
    }

    public  void checkServerAndCreateConnectionThread(){
        try {
            int timeout = 2000;
            int portNumber = Integer.parseInt(port);
            InetAddress ipAddress = InetAddress.getByName(serverIPAddress);
            SocketAddress address = new InetSocketAddress(ipAddress, portNumber);
            Socket socket = new Socket();
            socket.connect(address, timeout);
            cc= new ClientConnection(socket);
            cc.start();
            System.out.println("Connect Server Successfully!");
            JOptionPane.showMessageDialog(panelMain,"Welcome!" );
        } catch(NumberFormatException err) {
            JOptionPane.showMessageDialog(panelMain,"The port number is not correct!" );
            System.exit(0);
        } catch(UnknownHostException|NoRouteToHostException|SocketTimeoutException err) {
            JOptionPane.showMessageDialog(panelMain,"The IP address is not correct!" );
            System.exit(0);
        }catch (ConnectException e) {
            JOptionPane.showMessageDialog(panelMain,"The server is not on!\nor\nCheck the port number!");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}

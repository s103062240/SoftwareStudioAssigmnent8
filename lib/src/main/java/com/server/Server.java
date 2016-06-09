package com.server;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

class Server extends JFrame {

    private JLabel label;

    private int port;

    public static void main(String[] args) {
        new Server().doServer(args);
    }

    public void doServer(String[] args) {
        if (args.length != 1) {
            port = 8991;
        }
        else {
            port = Integer.parseInt(args[0]);
        }
        this.setTitle("Server");
        this.setSize(450, 175);
        this.setLayout(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            this.label = new JLabel("Initialize with ip " + InetAddress.getLocalHost().getHostAddress() + " port " + port);
            this.label.setBounds(20, 20, 410, 40);
            this.label.setFont(new Font(this.label.getFont().getName(), Font.PLAIN, 18));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (this.label != null) {
            this.add(this.label);
        }
        this.setVisible(true);
        startAccept();
    }

    private void startAccept() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        while (!serverSocket.isClosed()) {
            Socket client = null;
            try {
                client = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String message = reader.readLine();
                label.setText(message);
                SwingUtilities.invokeLater(() -> label.updateUI());
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

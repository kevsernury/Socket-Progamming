/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tamsdndeneme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private PrintWriter out;
    private BufferedReader in;

    private Socket client;

    private int clientPort;
    private String msg;

    public Server() throws IOException {
        ServerSocket listener = new ServerSocket(4444);

        System.out.println("[SERVER] Waiting for connection...");

        try {

            client = listener.accept();
            System.out.println("[SERER] Connected..");

            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while (client.isConnected()) {
                String request = in.readLine();
                

                if (request != null) {
                    System.out.println("[CLIENT] " + request);
                    parseRequest(request);
                    out.println(String.valueOf(clientPort) + String.valueOf(4444)+":|" + msg);
                }

            }

        } catch (Exception e) {
            System.out.println("Closing!");
            client.close();
            listener.close();
            out.close();
            in.close();
        }

    }

    private void parseRequest(String request) {
        clientPort = parseInt(request.substring(4, 8));
        int index = 0;
        for (int i = 9; i < request.length(); i++) {
            char one = request.charAt(i - 1);
            char two = request.charAt(i);
            if (one == ':' && two == '|') {
                index = i + 1;
            }
        }

        msg = request.substring(index, request.length());

    }
}

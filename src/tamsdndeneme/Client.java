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
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader in;
    private BufferedReader keyboard;
    private PrintWriter out;

    private int serverPort;
    private int clientPort;

    public Client(int port, int clientPort, int serverPort) throws IOException {
        this.serverPort = serverPort;
        this.clientPort = clientPort;

        socket = new Socket("127.0.0.1", port);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        keyboard = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("> ");
        String msg = keyboard.readLine();

        String request = String.valueOf(serverPort) + String.valueOf(clientPort) + ":|" + msg;
        out.println(request);
        try {
            while (socket.isConnected()) {

                String response = in.readLine();
                if (response != null) {
                    System.out.println("[SERVER] " + response);
                }

            }
        } catch (Exception e) {
            System.out.println("Closing..");
            socket.close();
            in.close();
            out.close();
            keyboard.close();
        }
    }

}

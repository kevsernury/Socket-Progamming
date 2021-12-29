
package tamsdndeneme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ForHandleServer implements Runnable {

    private PrintWriter out;
    private BufferedReader in;
    private int clientPort;
    private String msg;
    private Socket client;

    public ForHandleServer(Socket socket) {
        client = socket;
    }

    @Override
    public void run() {
        try {

            System.out.println("[SERER] Connected..");

            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while (!client.isClosed()) {
                String request = in.readLine();

                if (request != null) {
                    System.out.println("[CLIENT] " + request);
                    parseRequest(request);
                    out.println(String.valueOf(clientPort) + String.valueOf(4444) + ":|" + msg);
                    client.close();///
                }

            }

        } catch (Exception e) {
            System.out.println("Closing!");
            try {
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(ForHandleServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.close();
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(ForHandleServer.class.getName()).log(Level.SEVERE, null, ex);
            }
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

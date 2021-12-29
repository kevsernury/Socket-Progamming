
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

        try {

            while (true) {
                socket = new Socket("127.0.0.1", port);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                keyboard = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("> ");
                String msg = keyboard.readLine();
                int flag = 0;
                while (socket.isConnected() && flag == 0) {
                    String request = String.valueOf(serverPort) + String.valueOf(clientPort) + ":|" + msg;
                    out.println(request);
                    String response = in.readLine();
                    if (response != null) {
                        System.out.println("[SERVER] " + response);
                        socket.close();
                        flag = 1;
                    }

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

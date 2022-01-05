package tamsdndeneme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import javax.management.StringValueExp;

public class Client100 {

    private Socket socket;

    private BufferedReader in;
    private PrintWriter out;

    private int serverPort;
    private int clientPort;

    private String msg;

    private ArrayList<String> packageToSend = new ArrayList<>();

    public Client100(int clientPort, int serverPort) throws IOException {
        this.serverPort = serverPort;
        this.clientPort = clientPort;
        genereatePackage(100);

        try {

            for (int i = 0; i < packageToSend.size(); i++) {
                int port;
                Random r = new Random();
                int rand = r.nextInt(2);

                if (rand == 0) {
                    port = 1001;
                } else {
                    port = 1002;
                }

                msg = packageToSend.get(i);

                socket = new Socket("127.0.0.1", port);

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                int flag = 0;
                while (!socket.isClosed() && flag == 0) {
                    String request = String.valueOf(serverPort) + String.valueOf(clientPort) + ":|" + msg;
                    System.out.println(String.valueOf(i + 1) + ". " + request);
                    out.println(request);
                    String response = in.readLine();
                    if (response != null) {
                        if (response.equals("Package Dropped!")) {
                            System.out.println("[ROUTER] " + response);
                            System.out.println("Resend package");
                            i = i - 1;
                        } else {
                            System.out.println("[SERVER] " + response);
                        }
                        socket.close();
                        flag = 1;
                    }

                }
            }
        } catch (IOException e) {
            System.out.println("Closing..");
            socket.close();
            in.close();
            out.close();
        }
    }

    private void genereatePackage(int size) {
        //Genertae number between 33-125 
        Random r = new Random();

        for (int i = 0; i < size; i++) {
            String str = "";
            for (int j = 0; j < 20; j++) {
                int ch;
                ch = r.nextInt(126 - 65) + 65;
                str += (char) ch;
            }
            packageToSend.add(str);
        }
    }

}

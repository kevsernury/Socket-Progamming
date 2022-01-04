package tamsdndeneme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ForHandleController implements Runnable {

    private Socket router;

    private int serverPort;
    private int clientPort;
    private int routerPort;
    private int priRouter;
    private int nextRouter = 0;
    private String msg;
    private PrintWriter out;
    private BufferedReader in;

    public ForHandleController(Socket socket) {
        this.router = socket;
    }

    @Override
    public void run() {
        try {

            takeConnection(router);

        } catch (Exception e) {
            System.out.println("4 Closing!");
            try {
                router.close();
            } catch (IOException ex) {
                Logger.getLogger(ForHandleController.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.close();
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(ForHandleController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void parseRequest(String request) {
        serverPort = parseInt(request.substring(0, 4));
        clientPort = parseInt(request.substring(4, 8));
        int index = 0;
        for (int i = 9; i < request.length(); i++) {
            char one = request.charAt(i - 1);
            char two = request.charAt(i);
            if (one == ':' && two == '|') {
                index = i + 1;
                break;

            }
        }
        msg = request.substring(index, request.length());
        if (index >= 18) {
            priRouter = parseInt(request.substring(8, 12));
            routerPort = parseInt(request.substring(12, 16));
        } else if (index >= 14) {
            routerPort = parseInt(request.substring(8, 12));
        }
        System.out.println("Server: " + serverPort);
        System.out.println("Client: " + clientPort);
        System.out.println("This Router: " + routerPort);
        System.out.println("Previous Router: " + priRouter);

    }

    private void returnNextRouter() {

        Random random = new Random();
        int r = random.nextInt(2);
        int r2 = random.nextInt(6); //Drop etme ihtimali

        switch (routerPort) {
            case 1001:
                if (r2 == 1) {
                    nextRouter = 1;
                } else {
                    nextRouter = 1004;
                }
                break;
            case 1002:
                if (r2 == 1) {
                    nextRouter = 1;
                } else if (clientPort == 5555) {
                    nextRouter = 1005;
                } else {
                    nextRouter = 1003;
                }
                break;
            case 1003:

                if (r == 1) {
                    nextRouter = 1004;
                } else if (priRouter == 1002) {
                    nextRouter = 1;
                } else {
                    nextRouter = 1005;
                }
                break;
            case 1004:
                if (r2 == 1 && clientPort == 6666) {
                    nextRouter = 1;
                } else {
                    nextRouter = 1007;
                }
                break;
            case 1005:
                if (r2 == 1) {
                    nextRouter = 1;
                } else if (clientPort == 6666) {
                    nextRouter = 1008;
                } else {
                    nextRouter = 1006;
                }
                break;
            case 1006:
                if (r2 == 1) {
                    nextRouter = 1;
                } else if (r == 2) {
                    nextRouter = 1007;
                } else {
                    nextRouter = 1008;
                }
                break;
            case 1007:
                if (r2 == 1) {
                    nextRouter = 1;
                } else {
                    nextRouter = serverPort;
                }
                break;
            case 1008:
                if (r2 == 1) {
                    nextRouter = 1;
                } else {
                    nextRouter = serverPort;
                }
                break;

        }
    }

    private void takeConnection(Socket router) throws IOException {

        System.out.println("[CONTROLLER] Connected..");

        out = new PrintWriter(router.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(router.getInputStream()));

        while (router.isConnected() && nextRouter == 0) {
            String request = in.readLine();

            if (request != null) {
                System.out.println("[CLIENT] " + request);
                parseRequest(request);

                returnNextRouter();

            }
        }
        out.println(String.valueOf(nextRouter));
        nextRouter = 0;
        System.out.println("1 Closing!");
        router.close();
    }

}

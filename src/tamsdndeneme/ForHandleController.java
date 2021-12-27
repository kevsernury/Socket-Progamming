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
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kevsernur
 */
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
        System.out.println("Parse girildi");
        serverPort = parseInt(request.substring(0, 4));
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
        if (index > 15) {
            priRouter = parseInt(request.substring(8, 12));
            routerPort = parseInt(request.substring(12, 16));
        } else if (index > 11) {
            routerPort = parseInt(request.substring(8, 12));
        }
        System.out.println("Parse çıkıldı");

    }

    private void returnNextRouter() {
        switch (routerPort) {
            case 1001:
                nextRouter = 1002;
                break;
            case 1002:
                nextRouter = 1003;
                break;
            case 1003:
                nextRouter = serverPort;
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

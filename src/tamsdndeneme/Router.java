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

public class Router {

    private PrintWriter out;
    private BufferedReader in;

    private Socket nextRouterCon;
    private Socket controller;
    private Socket client;
    private String request;
    private String response;

    public Router() {
    }

    private int clientPort;
    private int serverPort;
    private int priRouter = 0;
    private int thisRouter;
    private int nextRouter = 0;
    private String msg;

    public Router(int routerPort) throws IOException {
        thisRouter = routerPort;
        ServerSocket listener = new ServerSocket(routerPort);

        System.out.println("[ROUTER] Waiting for connection...");

        try {
            

            client = listener.accept();
            System.out.println("[ROUTER] Connected..");

            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while (client.isConnected() && request == null) {
                request = in.readLine();

            }

            if (request != null) {
                parseRequest(request);

                try {
                    controller = new Socket("127.0.0.1", 9999);
                    out = new PrintWriter(controller.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(controller.getInputStream()));
                    int zeroflag = 0;
                    while (controller.isConnected() && zeroflag == 0) {
                        if (priRouter == 0) {
                            out.println(String.valueOf(serverPort) + String.valueOf(clientPort) + String.valueOf(thisRouter) + ":|" + msg);
                        } else {
                            out.println(String.valueOf(serverPort) + String.valueOf(clientPort) + String.valueOf(priRouter) + String.valueOf(thisRouter) + ":|" + msg);
                        }

                        while (nextRouter == 0) {
                            nextRouter = parseInt(in.readLine());
                            int flag = 1;
                            if (nextRouter != 0 && flag == 1) {
                                controller.close();
                                System.out.println(controller.isConnected());
                                flag = 0;
                                zeroflag = 1;
                                System.out.println("next router " + String.valueOf(nextRouter));
                            }
                        }
                        

                    }

                    System.out.println("3. try");
                    nextRouterCon = new Socket("127.0.0.1", nextRouter);
                    out = new PrintWriter(nextRouterCon.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(nextRouterCon.getInputStream()));
                    
                    if (nextRouterCon.isConnected()) {
                        System.out.println("1 " + msg);
                        out.println(String.valueOf(serverPort) + String.valueOf(clientPort) + String.valueOf(thisRouter) + ":|" + msg);
                        response = in.readLine();
                        
                        if (response != null) {
                            System.out.println("1 " + msg);
                            out = new PrintWriter(client.getOutputStream(), true);
                            out.println(response);
                        } else {
                            System.out.println("2 Closing!");
                            client.close();
                            listener.close();
                            out.close();
                            in.close();
                        }

                    } else {
                        System.out.println("3 Closing!");
                        client.close();
                        listener.close();
                        out.close();
                        in.close();
                    }

                } catch (IOException e) {
                    System.out.println("4 Closing!");
                    client.close();
                    listener.close();
                    out.close();
                    in.close();
                }

            }
        } catch (IOException e) {
            System.out.println("5 Closing!");
            client.close();
            listener.close();
            out.close();
            in.close();
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
            }
        }
        msg = request.substring(index, request.length());
        if (index > 15) {
            priRouter = parseInt(request.substring(8, 12));
        } 

    }

   
}

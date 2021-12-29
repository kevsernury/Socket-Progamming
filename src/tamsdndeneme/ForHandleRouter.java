
package tamsdndeneme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ForHandleRouter implements Runnable {

    private Socket nextRouterCon;
    private Socket controller;
    private Socket client;
    private ServerSocket listener;

    private String request;
    private String response;
    private int clientPort;
    private int serverPort;
    private int priRouter = 0;
    private int thisRouter;
    private int nextRouter = 0;
    private String msg;

    private PrintWriter out;
    private BufferedReader in;

    public ForHandleRouter(Socket socket, ServerSocket serverSocket, int router) {
        client = socket;
        listener = serverSocket;
        thisRouter = router;

    }

    @Override
    public void run() {
        try {

            out = new PrintWriter(client.getOutputStream(), true);

            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while (!client.isClosed() && request == null) {
                request = in.readLine();

            }

            if (request != null) {
                parseRequest(request);

                try {
                    controller = new Socket("127.0.0.1", 9999);
                    out = new PrintWriter(controller.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(controller.getInputStream()));
                    int zeroflag = 0;
                    while (!controller.isClosed() && zeroflag == 0) {
                        if (priRouter == 0) {
                            out.println(String.valueOf(serverPort) + String.valueOf(clientPort) + String.valueOf(thisRouter) + ":|" + msg);
                        } else {
                            out.println(String.valueOf(serverPort) + String.valueOf(clientPort) + String.valueOf(priRouter) + String.valueOf(thisRouter) + ":|" + msg);
                        }

                        while (nextRouter == 0) {
                            nextRouter = parseInt(in.readLine());
                            int flag = 1;

                            if (nextRouter == 1) {
                                System.out.println("Package Dropped!");
                                out = new PrintWriter(client.getOutputStream(), true);
                                out.println("Package Dropped!");
                                client.close();///
                            } else if (nextRouter != 0 && flag == 1) {
                                controller.close();
                                flag = 0;
                                zeroflag = 1;
                                System.err.println("This router "+ thisRouter);
                                System.out.println("next router " + String.valueOf(nextRouter));
                            }
                        }

                    }

                    if (nextRouter != 1) {
                        nextRouterCon = new Socket("127.0.0.1", nextRouter);
                        out = new PrintWriter(nextRouterCon.getOutputStream(), true);
                        in = new BufferedReader(new InputStreamReader(nextRouterCon.getInputStream()));

                        if (!nextRouterCon.isClosed()) {
                            System.out.println("The message " + msg);
                            out.println(String.valueOf(serverPort) + String.valueOf(clientPort) + String.valueOf(thisRouter) + ":|" + msg);
                            response = in.readLine();

                            if (response != null) {
                                
                                out = new PrintWriter(client.getOutputStream(), true);
                                out.println(response);
                                client.close();///
                                nextRouterCon.close();///
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
                    }else {
                            System.out.println("5 Closing!");
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

        } catch (IOException ex) {
            Logger.getLogger(ForHandleRouter.class.getName()).log(Level.SEVERE, null, ex);
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
        if (index >= 14) {
            priRouter = parseInt(request.substring(8, 12));
        }
        System.out.println("İfin içi " +"index: "+ index+" router: " +priRouter);
        
       

    }

}

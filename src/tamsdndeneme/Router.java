/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tamsdndeneme;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Router {

    private ArrayList<ForHandleRouter> handleThese = new ArrayList<>();
    private ExecutorService pool = Executors.newFixedThreadPool(4);
    private Socket client;

    private int thisRouter;

    public Router(int routerPort) throws IOException {
        thisRouter = routerPort;
        ServerSocket listener = new ServerSocket(routerPort);

        System.out.println("[ROUTER] Waiting for connection...");
        while (true) {
            try {
                client = listener.accept();
                System.out.println("[ROUTER] Connected..");

                ForHandleRouter handle = new ForHandleRouter(client, listener, thisRouter);
                handleThese.add(handle);

                pool.execute(handle);

            } catch (IOException e) {
                System.out.println("5 Closing!");
                client.close();
                listener.close();
            }
        }

    }

}


package tamsdndeneme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Server {

    private Socket client;
    private ArrayList<ForHandleServer> handleThese = new ArrayList<>();
    private ExecutorService pool = Executors.newFixedThreadPool(250);

    public Server() throws IOException {
        ServerSocket listener = new ServerSocket(4444);
        
        while (true) {            
            System.out.println("[SERVER] Waiting for connection...");
            
            client = listener.accept();
            
            ForHandleServer handle = new ForHandleServer(client);
            handleThese.add(handle);
            
            pool.execute(handle);
        }
        

    }

}

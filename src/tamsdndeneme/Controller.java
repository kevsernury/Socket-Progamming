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
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller {
    
    private ArrayList<ForHandleController> handleThese = new ArrayList<>();
    private ExecutorService pool = Executors.newFixedThreadPool(4);
    
    private Socket router;
    
    public Controller() throws IOException {
        ServerSocket listener = new ServerSocket(9999);
        
        while (true) {            
            System.out.println("[CONTROLLER] Waiting for connection...");
            router = listener.accept();
            
            ForHandleController handle = new ForHandleController(router);
            handleThese.add(handle);
            
            pool.execute(handle);
        }
        
    }
    
}

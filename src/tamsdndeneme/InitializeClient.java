/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tamsdndeneme;

import java.io.IOException;
import java.util.Random;

public class InitializeClient {

    public static void main(String[] args) throws IOException {

        //1. serverPort, 2. clientPort
        int port;
        Random r = new Random();
        int rand = r.nextInt(2);

        if (rand == 0) {
            port = 1001;
        } else {
            port = 1002;
        }

        Client client = new Client(port, 5555, 4444);
    }
}

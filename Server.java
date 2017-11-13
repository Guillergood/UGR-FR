
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alejandro de la Plata Ramos
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ServerSocket server;
        int port = 2222;
        int maxClients = 10;
        int totalClients = 0;
        Procesador[] threads = new Procesador[maxClients];
        
        // Open the Server
        try {
            server = new ServerSocket(port);
            // Server is running always.
            do {
                //Accept the user connection and add to the clients array.
                Socket client = server.accept();
                int i;
                boolean asignado = false;
                for (i = 0; i < maxClients && !asignado; i++) {
                    if (threads[i] == null) {
                        threads[i] = new Procesador(client, threads);
                        System.out.print("Un cliente lanzado ...");
                        threads[i].start();
                        asignado = true;
                    }
                } 
                if (!asignado) {
                    System.out.print("Chat lleno, por favor, inténtelo más tarde");
                    client.close();
                }
                
            } while (true);

        } catch (IOException e) {
            System.err.println("Error: no se puede abrir el puerto indicado.");
        }
    
    }
    
}

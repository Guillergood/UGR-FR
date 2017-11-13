
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
        int port = 80;
        ArrayList <Socket> clients = new ArrayList<>();
        ArrayList <PrintWriter> clients_writer = new ArrayList<>();
        
        // Open the Server
        try {
            server = new ServerSocket(port);
            // Server is running always.
            do {
                //Accept the user connection and add to the clients array.
                Socket client = server.accept();
                clients.add(client);
                clients_writer.add(new PrintWriter(client.getOutputStream(), true));
            } while (true);

        } catch (IOException e) {
            System.err.println("Error: no se puede abrir el puerto indicado.");
        }
    
    }
    
}

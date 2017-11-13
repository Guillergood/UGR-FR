import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alejandro de la Plata Ramos
 */

 public class Procesador extends Thread {
    Socket clientSocket;
    private int maxClients;
    Procesador[] threads;
    BufferedReader in = null;
    PrintWriter out = null;

    public Procesador(Socket clientSocket, Procesador[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClients = threads.length;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.err.println(clientSocket.toString() + "Couldn't get I/O for the connection to server.");
            System.exit(1);
        }

        while (true) {
            try {
                String mensaje = in.readLine();
                String[] words = mensaje.split("\\s+", 3);
                if (words[1].startsWith("@")) {
                    for (int i = 0; i < maxClients; i++) {

                    }
                } else { 
                    // Mensaje publico
                    synchronized(this) {
                        for (int i = 0; i < maxClients; i++) {
                            if (threads[i] != null) {
                                mensaje = "<"+words[0]+">" + words[1] +" "+ words[2];
                                threads[i].out.println(mensaje);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error al obtener los flujos de entrada/salida.");
            }
        }

    }
 }
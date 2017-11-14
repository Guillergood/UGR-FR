import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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
    private final Procesador[] threads;
    private static ArrayList <String> names = new ArrayList<>();
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
                String mensaje;
                while ((mensaje = in.readLine().trim()) != null) {
                    //Adaptamos el texto
                    String[] words = mensaje.split("\\s+", 2);
                    //Recogemos los nombres de todos los usuarios conectados
                    //y los almacenamos
                    if (words[0].trim().equals("/NewUser")) {
                        synchronized(this) {
                            names.add(words[1].trim());
                            this.out.println("Nuevo usuario: " + words[1].trim());
                            for (int i = 0; i < maxClients; i++) {
                                if (threads[i] != null && threads[i] != this) {
                                    mensaje = words[1] + " se ha unido al chat.\n";
                                    threads[i].out.println(mensaje);
                                }
                            }
                            //Listamos los usuarios:
                            this.out.println("Los usuarios conectados actualmente son:\n");
                            for (String n : names) {
                                this.out.println("Usuario: " + n + "\n");
                            }

                        }
                    } else if (words[1].trim().startsWith("@")) {
                        words = words[1].split("\\s+", 2);
                        mensaje = words[1].trim();
                        if (names.contains(words[0].trim())) {
                            //QUE MUESTRE QUIEN HA SIDO!!!!
                            threads[names.indexOf(words[0].trim())].out.println(mensaje);
                        } else {
                            this.out.println("No hay ningún usuario con ese nombre: " + words[0].trim());
                        }
                    } else { 
                        // Mensaje publico
                        synchronized(this) {
                            String name = words[0].trim();
                            mensaje = words[1].trim();
                            for (int i = 0; i < maxClients; i++) {
                                if (threads[i] != null && threads[i] != this) {
                                    mensaje = "<"+name+">" +" "+ mensaje;
                                    threads[i].out.println(mensaje);
                                }
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
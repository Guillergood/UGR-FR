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
            System.err.println("Couldn't get I/O for the connection to server.");
            System.exit(1);
        }

        while (!this.isInterrupted()) {
            try {
                String mensaje;
                while ((mensaje = in.readLine().trim()) != null) {
                    //Adaptamos el texto para trabajar con el.
                    String[] words = mensaje.split("\\s+", 2);
                    if (words[0].trim().equals("/NewUser")) {
                         NewUser(words, mensaje);
                    } else if (words[1].trim().equals("/List")){
                        UserList();
                    } else if (words[1].trim().equals("/Quit")){
                        QuitUser(words[0].trim());
                        this.interrupt();
                    } else if (words[1].trim().startsWith("@")) {
                        PrivateMessage(words, mensaje);
                    } else { 
                        PublicMessage(words, mensaje);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error al obtener los flujos de entrada/salida.");
            }
        }
        //Terminamos de eliminar el cliente que se ha desconectado...
        synchronized(this) {
            
        }
    }

    //Informa de que se ha conectado un nuevo usuario y añade 
    //su nombre a la lista de usuarios
    private void NewUser(String[] words, String mensaje) {
        synchronized(this) {
            names.add(words[1].trim());
            this.out.println("Te has unido al chat correctamente.");
            for (int i = 0; i < maxClients; i++) {
                if (threads[i] != null && threads[i] != this) {
                    mensaje = words[1].trim() + " se ha unido al chat.";
                    threads[i].out.println(mensaje);
                }
            }
        }
    }

    //Muestra los usuarios actualmente conectados al chat
    private void UserList() {
        //Listamos los usuarios:
        this.out.println("Los usuarios conectados actualmente son:");
        for (String n : names) {
            this.out.println("Usuario: " + n);
        }
    }

    //Un usuario abandona el chat
    private void QuitUser(String name) {
        synchronized(this) {
            for (int i = 0; i < maxClients; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                    names.remove(name);
                } else if (threads[i] != null){
                    threads[i].out.println(name + " ha abandonando la sala.");
                }
            }
            try {
                this.in.close();
                this.out.close();
                this.clientSocket.close();
                this.interrupt();
            } catch (Exception e) {
                System.err.print("Error: " + e.getMessage());
            }
        }
    }

    //Envía un mensaje privado al usuario especificado
    private void PrivateMessage(String[] words, String mensaje) {
        synchronized(this) {
            String emisor = words[0].trim();
            words = words[1].split("\\s+", 2);
            if (words.length == 2) {
                mensaje = words[1].trim();
                if (names.contains(words[0].trim())) {
                    mensaje = "<MP-" + emisor + "> " + mensaje;
                    threads[names.indexOf(words[0].trim())].out.println(mensaje);
                } else {
                    this.out.println("No hay ningún usuario con ese nombre: " + words[0].trim());
                }
            }
        }
    }

    //Envía un mensaje público a todos los usuarios conectados.
    private void PublicMessage(String[] words, String mensaje) {
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
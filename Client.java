
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alejandro de la Plata Ramos
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Socket cliente;
        DataInputStream in;
        DataOutputStream out;
        String nombre;
        Scanner consola;
        String host = "localhost";
        int port = 2222;
        
        try{
            //Logueo:
            do {
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Introduzca su nombre de usuario: ");
                nombre = stdIn.readLine();
                stdIn.close();
            } while (nombre.isEmpty());
            nombre = "@" + nombre;

            cliente = new Socket(host, port);
            in = new DataInputStream(cliente.getInputStream());
            out = new DataOutputStream(cliente.getOutputStream());
            consola = new Scanner(System.in);
            
            //Lanzamos los procesos para leer y escribir mensajes.
            Thread hiloLectura = new ProcesoLeer(host, port);
            Thread hiloEscritura = new ProcesoEscribir(nombre, host, port);
            
            hiloLectura.start();    
            hiloEscritura.start();
        } catch (IOException e) {
            System.err.println("Error: no se pudo conectar con el host.");
        }
    }   
}

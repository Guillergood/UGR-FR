
import java.io.DataInputStream;
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
public class Client implements Runnable {
    private final Socket cliente;
    private final DataInputStream in;
    private final DataOutputStream out;
    private boolean mode; //True == chat grupo  False == chat individual
    private String target;
    public String nombre;
    private Scanner consola;
    private String host = "localhost";
    private int port = 80;
    
    public Client () throws IOException {
        this.cliente = new Socket(host, port);
        this.in = new DataInputStream(cliente.getInputStream());
        this.out = new DataOutputStream(cliente.getOutputStream());
        consola = new Scanner(System.in);
    }
    
    @Override
    public void run() {
        //Logueo:
        do {
            System.out.print("Introduzca su nombre de usuario: ");
            nombre = consola.next();
        } while (nombre.isEmpty());
        
        //Lanzamos los procesos para leer y escribir mensajes.
        Thread hiloLectura = new ProcesoLeer(nombre, host, port);
        Thread hiloEscritura = new ProcesoEscribir(nombre, host, port);
        
        hiloLectura.start();    
        hiloEscritura.start();
    }
}


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author superkorlas
 */
public class ProcesoLeer extends Thread 
{
    private Socket socket;
    
    public ProcesoLeer(Socket socket)
    {
        this.socket = socket;
    }
    
    public void run()
    {    
        try
        {            
            //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);                   
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) 
            {
                //out.println(inputLine);
                System.out.println(inputLine); //Añadida por mi, para mostrar en pantalla.
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen the port or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
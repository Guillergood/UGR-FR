import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alejandro de la Plata Ramos
 */
public class ProcesoEscribir extends Thread 
{
    private Socket echoSocket;
    private final String clientName;

    public ProcesoEscribir(Socket echoSocket, String clientName)
    {
        this.echoSocket = echoSocket;
        this.clientName = clientName;
    }
    
    @Override
    public void run()
    {
        try (
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            //Indicamos que nos hemos unido al chat.
            out.println("/NewUser" + " " + clientName);
            String userInput;
            while ((userInput = stdIn.readLine()) != null) 
            {
                //Gestionar mensaje.
                if (!userInput.trim().isEmpty()) {
                    out.println(clientName + " " + userInput);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host ");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to host");
            System.out.println(e.getMessage());
            System.exit(1);
        } 
    }
}

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
    private final String clientName;
    private final String hostName;
    private final int portNumber;

    public ProcesoEscribir(String clientName, String hostName, int portNumber)
    {
        this.clientName = clientName;
        this.hostName = hostName;
        this.portNumber = portNumber;
        
    }
    
    @Override
    public void run()
    {
        try (
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) 
            {
                //Gestionar mensjaje.
                out.println(clientName + " " + userInput);
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
    }
}

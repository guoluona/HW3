/******************************************************************************
 *
 *  CS 6421 - Discovery Server
 *  implement a discoverty server store adresses information
 *  Compilation:  javac DiscovServer.java
 *  Execution:    java DiscovServer
 *
 *  % java DiscovServer portnum
 ******************************************************************************/

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Double;

public class ConvServer_b_g {
    
    static String client= "i'm client!";
    static String proxy= "i'm Proxy!";
    static String conv= "i'm Conv!";

    public static void process (Socket clientSocket) throws IOException {
        // open up IO streams
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        /* Write a welcome message to the client */
        out.println("Welcome to the Bananas (b) to Grams of Potassium (g) conversion server!");

        /* read and print the client's request */
        // readLine() blocks until the server receives a new line from client
        String userInput;
        if ((userInput = in.readLine()) == null) {
            System.out.println("Error reading message");
            out.close();
            in.close();
            clientSocket.close();
        }

        System.out.println("Received message: " + userInput);
         
    
        //String[] arg = userInput.split(" ");
        if (userInput.equals(client)){
            //--TODO:
        }else if(userInput.equals(proxy)){
            //--TODO:
        }else if(userInput.equals(conv)){
            //--TODO:
        }
        // close IO streams, then socket
        out.close();
        in.close();
        clientSocket.close();
    }

    public static void main(String[] args) throws Exception {

        //check if argument length is invalid
        if(args.length != 1) {
            System.err.println("Usage: java ConvServer port");
        }
        // create socket
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Started server on port " + port);

        // wait for connections, and process
        try {
            while(true) {
                // a "blocking" call which waits until a connection is requested
                Socket clientSocket = serverSocket.accept();
                System.err.println("\nAccepted connection from client");
                process(clientSocket);
            }

        }catch (IOException e) {
            System.err.println("Connection Error");
        }
        System.exit(0);
    }
}

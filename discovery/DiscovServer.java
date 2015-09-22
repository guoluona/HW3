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
import java.util.HashMap;

public class DiscovServer {
    
    static String client= "i'm client!";
    static String proxy= "i'm Proxy!";
    static String conv= "i'm Conv!";
    
    static HashMap<String, String> discovTable  = new HashMap<String, String>();

    public static String[] callServer(String msg, String server) {

        Socket sock = null;
        String[] userInput = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            String[] serverArg = server.split(" ");
            sock = new Socket(serverArg[0], Integer.parseInt(serverArg[1]));    //get the socket and connet to the server
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));   //get reader
            out = new PrintWriter(sock.getOutputStream(), true);    //get printer
            out.println(msg + "\n");   //send messages
            userInput = new String[2];
            
            if ((userInput[0] = in.readLine()) == null) {  //get reply
                System.out.println("Error reading message");
                out.close();
                in.close();
                sock.close();
            }

            userInput[1] = in.readLine();
            System.out.println("server info: " + userInput[0] + "; return result: " + userInput[1]);
            out.close();
            in.close();
            sock.close();
        } catch(Exception e) {
            out.println("ERROR:" + e);
        }

        //System.out.println(msg + " sended.");
        return userInput;
    }
    
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
        String[] arg = userInput.split(":");
        try{
            if(arg[0].equals(proxy)){
                // proxy server
                if(arg[1].equals("a")){ // when proxy server start, add its host, port info to discovTable
                    discovTable.put("proxy",arg[2]);
                }else if(arg[1].equals("r")){ // when proxy server die, remove its info in discovTable
                    discovTable.remove("proxy");
                }else if(arg[1].equals("lkup")){ // proxy server ask discverty server for info about host, port info for convertion server
                     callServer("discov:r_lkup:" + discovTable.get(arg[2]), discovTable.get("proxy"));
                }
            }else if(arg[0].equals(conv)){
                // convertion server
                String[] msg = arg[2].split(" ");
                if(arg[1].equals("a")){ // when convertion server start, add their host, port info to discovTable
                    discovTable.put(msg[0] + " " + msg[1], arg[3]);
                    discovTable.put(msg[1] + " " + msg[0], arg[3]);
                    callServer("discov:updt_t_a:" + arg[2], discovTable.get("proxy"));
                }else if(arg[1].equals("r")){   // when convertion server die, remove their info in discovTable
                    discovTable.remove(msg[0] + " " + msg[1]);
                    discovTable.remove(msg[1] + " " + msg[0]);
                    callServer("discov:updt_t_r:" + arg[2], discovTable.get("proxy"));
                }
            }else{
                // client:
                String[] msg = arg[0].split(" ");
                if(discovTable.containsKey(msg[0] + " " + msg[1])){
                    callServer(arg[0], discovTable.get(msg[0] + " " + msg[1]));
                }else{
                    out.println("can't accomplish that convertion");
                }
            }
        }catch(Exception e){
            out.println("Error:" + e);
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

// CS 6421 - Simple Message Board Client in Java
// Yi Zhou
// Compile with: javac MsgClient
// Run with:     java MsgClient "192.168.1.1" "Joe" "Hello World"

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MsgClient {
    public static void main(String[] args) {
        int portnum = 5555;

        if(args.length <= 2){
            System.out.println("ERROR, please input line arguements as host, username, message");
            System.out.println("e.g.: java MsgClient \"192.168.1.1\" \"Joe\" \"Hello World\"");
            System.exit(-1); 
        }
        
        Socket sock = null;
        try {
            sock = new Socket(args[0], portnum);    //get the socket and connet to the server
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);    //get printer
            out.println(args[1]+"\n"+args[2]);   //send messages
            out.close();
            sock.close();
        } catch(java.net.ConnectException e) {
            System.out.println("ERROR: connection refused.\nMore info:"+e);
            System.exit(-1); 
        } catch(UnknownHostException e) {
            System.out.println("ERROR: connect failed.\nMore info:"+e);
            System.exit(-1); 
        } catch(IOException e) {
            System.out.println("ERROR:"+e);
            System.exit(-1); 
        } catch(Exception e) {
            System.out.println("ERROR:"+e);
            System.exit(-1); 
        }

        System.out.println("Done.");
    }
}

// CS 6421 - Simple Message Board Client in Java
// Yi Zhou
// Compile with: javac MsgClient
// Run with:     java MsgClient

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MsgClient {
    public static void main(String[] args) {
        String host = "finger92.koding.io";
        int portnum = 5555;

        try {
            Socket sock = new Socket(host, portnum);
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
            out.println(args[args.length-2]);
            out.println(args[args.length-1]);
            
        } catch(Exception e) {
            System.out.println("Socket connection failed, Exception:"+e);
        }
       

    }
}

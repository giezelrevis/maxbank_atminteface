/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atminterface;
import java.io.*;
import java.net.*;
/**
 * 8175 port prod
 * @author KrabyYap
 */
public class ATMInterface {
   // 17300   8175
    static private int port=17300, maxConnections=100;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        int i=0;
        try{
            InetAddress addr = InetAddress.getByName("0.0.0.0");
            ServerSocket listener = new ServerSocket(port,50,addr);
            System.out.println("Address:" + listener);
            Socket server;
            System.out.println("Server started...");
            while((i++ < maxConnections) || (maxConnections == 0)){
                server = listener.accept();
                ATMThread conn_c= new ATMThread(server);
                Thread t = new Thread(conn_c);
                t.start();
                System.out.println("Client connected...");
            }
        } catch (IOException ioe) {
            System.out.println("IOException on socket listen: " + ioe);
            ioe.printStackTrace();
        }
    }   
}

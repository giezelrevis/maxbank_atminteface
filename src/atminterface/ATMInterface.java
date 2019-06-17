/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atminterface;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.sql.Timestamp;					  
						  
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
			writeToLog("Address:" + listener);								  
            Socket server;
            System.out.println("Server started...");
			writeToLog("Server started...");								
            while((i++ < maxConnections) || (maxConnections == 0)){
                server = listener.accept();
                ATMThread conn_c= new ATMThread(server);
                Thread t = new Thread(conn_c);
                t.start();
                System.out.println("Client connected...");
				writeToLog("Client connected...");								  
            }
        } catch (IOException ioe) {
            System.out.println("IOException on socket listen: " + ioe);
            ioe.printStackTrace();
        }
    }   
	 public static void writeToLog(String content){
	try (FileWriter writer = new FileWriter("ATMInterface.log", true);
            BufferedWriter bw = new BufferedWriter(writer)) {
            Date date= new Date();
            long time = date.getTime();
            Timestamp ts = new Timestamp(time);
            
            bw.newLine();
            bw.write("Log Entry:"+ts);
            bw.newLine();
            bw.write(content);
	} catch (IOException e) {
            System.err.format("IOException: %s%n", e);
	}    
    }
}											  
																   

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atmclient;
import java.io.*;
import java.net.*;
import java.util.*;
/**
 *
 * @author KrabyYap
 */
public class ATMClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String hostName = "localhost";
        int portNumber = 17300;  //17300   8175;
        String fromServer = null,fromUser;
    try{
        Socket kkSocket = new Socket(hostName, portNumber);
        // PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
        PrintStream out = new PrintStream(kkSocket.getOutputStream());
        BufferedReader in = new BufferedReader(
        new InputStreamReader(kkSocket.getInputStream()));
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        int msgLength = 0;
        boolean isValidMsgLength = true;
        
        do{
            System.out.print("Client: ");
            fromUser = stdIn.readLine();
            if (fromUser != null) {
                //System.out.println("Client: " + fromUser);
                //out.println(fromUser);  
                //byte[] c = fromUser.getBytes();
                //byte[] c = {0, 39, 48, 56, 48, 48, 130, 32, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 48, 54, 49, 56, 48, 56, 51, 51, 49, 57, 55, 56, 52, 56, 53, 48, 51, 48, 49};
                byte[] c = hexStringToByteArray(fromUser);
                System.out.println("c: " + c);
                for (int i = 0; i < c.length; i++) {
                    System.out.println("bytes: " + c[i]);
                }
                System.out.println("c.length: " + c.length);
                //out.print(c);
                out.write(c, 0, c.length);
                //out.flush();
            }
            //fromServer = in.readLine();
            for(int count = 1; count >= 0; count--){
                //int c = in.read();
                //line = line + (char)c;
                int c = in.read();
                //StringBuilder sb = new StringBuilder();
                //sb.append(String.format("%02X ", c));
                //System.out.println(sb.toString());
                line = line + String.valueOf(c);
                //line = line+(char)c;
                if(count == 0){
                    try{
                        msgLength = Integer.parseInt(line);
                        System.out.println("msgLength :"+msgLength);
                        
                        //System.out.println("msgLength ito :"+msgLength);
                    }catch(NumberFormatException e){
                        isValidMsgLength=false;
                        System.out.println("isValidMsgLength=false;");
                        break;
                    }
                }

            }
            if(isValidMsgLength){

                for(int count = 0; count < msgLength; count++) {
                    int c = in.read();
                    StringBuilder sbn = new StringBuilder();
                    sbn.append(String.format("%02X", c));
                    
                    System.out.println(sbn.toString());
                    
                    if (count > 3 && count < 20){
                        StringBuilder bm = new StringBuilder();
                        bm.append(String.format("%02X", c)); 
                        System.out.println("bitmap hex:"+sbn.toString().trim());
                        line = line + sbn.toString().trim(); 
                    } else {
                        line = line+(char)c;
                    }
                }
                
                System.out.println("line["+line+"]"); 

            }
            //System.out.println("Server: " + fromServer);
            
        }while(!line.equals("exit"));
    }catch(IOException e){
        System.out.println(e);
    }
        // TODO code application logic here
    }
    
    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
         System.out.println("b : "+ b.length); 
        for (int i = 0; i < b.length; i++) {
            System.out.println("iii : "+ i); 
            int index = i * 2;
            System.out.println(s.substring(index, index + 2));
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;   
        }
        return b;
        
    }
    public String hexToBinary(String hex1) {
        
        long    number1, number2;
        String  binary, binary1, binary2;
        /*
        number1 = new java.math.BigInteger(hex1, 16).longValue();
        binary1 = Long.toBinaryString(number1);
        String bin1 = String.format("%64s", binary1).replace(" ", "0");
        
        String bin2 = "";
        if (hex2 != ""){    
            number2 = new java.math.BigInteger(hex2, 16).longValue();
            binary2 = Long.toBinaryString(number2);
            bin2 = String.format("%64s", binary2).replace(" ", "0");            
        }
        */
        binary = "";
        for (int i = 0; i < hex1.length(); i++){
            String x = hex1.substring(i,i+1);
            number1 = new java.math.BigInteger(x, 16).longValue();
            binary1 = Long.toBinaryString(number1);
            String bin1 = String.format("%4s", binary1).replace(" ", "0");
            binary = binary + bin1;
        }
        //binary = new StringBuilder().append(bin1).append(bin2).toString();
        
        return  binary;
    }
    
}

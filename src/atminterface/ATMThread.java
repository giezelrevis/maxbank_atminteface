/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atminterface;
import java.io.*;
//import java.math.BigInteger;
import java.net.*;
//import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
//import java.util.Map;
import java.util.Date;
import java.util.StringTokenizer;
/**
 *
 * @author KrabyYap
 */
public class ATMThread implements Runnable{
    private Socket server;
    private String line,input;
    ATMDatabase atmDatabase;
    ATMParser atmParser;
    private ConcurrentHashMap response;
    
    ATMThread(Socket server) {
      this.server=server;
      atmDatabase = new ATMDatabase();
      atmParser = new ATMParser();
    }
    
    public void run () {
      input = "";
      try {
        // Get input from the client
        DataInputStream in = new DataInputStream(server.getInputStream());
        PrintStream out = new PrintStream(server.getOutputStream());
        //out.println("connected");
        do{
            line = "";
            String cc = "";
            String line2 = "";
            int msgLength = 0;
            int nLength = 0;
            InetAddress addy = server.getInetAddress();
            String sourceIp = addy.getHostAddress();
            String grailsOutput = "";
            String msgCode = "";
            boolean isValidMsgLength = true;
            Date msgTimestamp;
            String label;
            String val = null;
            int msgLenOrig = 0;
                    
            for(int count = 1; count >= 0; count--){
                //int c = in.read();
                //line = line + (char)c;
                int c = in.read();
                System.out.println("c:"+c);
                //StringBuilder sb = new StringBuilder();
                //sb.append(String.format("%02X ", c));
                //System.out.println(sb.toString());
                if (count == 1 && (int)c == 1){
                    c = 256;
                }
                System.out.println("Integer.parseInt(String.valueOf(c)) : "+Integer.parseInt(String.valueOf(c)));        
                msgLenOrig = msgLenOrig + Integer.parseInt(String.valueOf(c));
                line = line + String.valueOf(c);
                
                //line = line+(char)c;
                if(count == 0){
                    try{

                        //msgLength = Integer.parseInt(line);
                        msgLength = msgLenOrig ;//+ 2;
                        System.out.println("msgLength :"+msgLength);
                        
                        //System.out.println("msgLength ito :"+msgLength);
                    }catch(NumberFormatException e){
                        isValidMsgLength=false;
                        System.out.println("isValidMsgLength=false;");
                        break;
                    }
                }

            }
            if(msgLength > 256){
                line = "0" + String.valueOf(msgLength);
            }
            if(isValidMsgLength){

                for(int count = 0; count < msgLength; count++) {
                    int c = in.read();
                    StringBuilder sbn = new StringBuilder();
                    sbn.append(String.format("%02X", c));
                    System.out.println("xxx :"+sbn.toString());
                    
                    if (count > 3 && count < 20){
                        StringBuilder bm = new StringBuilder();
                        bm.append(String.format("%02X", c));                      
                        line = line + sbn.toString().trim(); 
                    } else {
                        line = line+(char)c;
                    }
                }
                // add '0' to line - 4 byte msg length
                if (msgLength < 100){ 
                    line = "0"+line;
                }

                msgTimestamp = new Date();
                System.out.println("REQT " + msgTimestamp.toString() + " | IP " + sourceIp + "| Port " + server.getPort() + " | " + msgLength + " | " + line);
                System.out.println(line);
                response = atmParser.processRequest(line);
                response.put("sourceIp", sourceIp);
                
                System.out.println("RESP " + response);
                grailsOutput = atmDatabase.sendAction(response);
                System.out.println("grailsOutput :"+grailsOutput);
                
																				  
				
               //StringTokenizer st = new StringTokenizer(grailsOutput, "{},:\""); 
                StringTokenizer st = new StringTokenizer(grailsOutput, "{}:\"");
                while(st.hasMoreTokens()) {
                    val = st.nextToken();
                }
                
                // start msg transformation
                System.out.println( "val " + val );
                int msgLen = val.length() - 20;
                byte[] b = new byte[msgLen + 2];
                int [] bb = new int[msgLen + 2];
                System.out.println("msgLen:"+msgLen);
                // message length
                int v = 0;
                //b[0] = (byte) v;
                //b[1] = (byte) msgLen; 
                
                /*bb[0] = v;
                bb[1] = msgLen; 
                b[0] = (byte) v;
                b[1] = (byte) msgLen;*/
                bb[0] = v;
                bb[1] = msgLen; 
                b[0] = (byte) v;
                b[1] = (byte) msgLen;
                if (msgLen >= 256){
                   v = 1;
                   b[0] = (byte) v;
                   v = msgLen - 256;
                   b[1] = (byte) v;        
                }
                int bitx = 6;
                int msgx = 22;
                int mtix = 1;
                //for (int i = 4; i < val.length(); i++){
                
                for (int i = 4; i < val.length(); i++){
                    // use x for increment on MTI
                    mtix++;
                    
                    /*
                    --------------------- message length already created above
                    if (i <= 3){
                        // message length
                        // convert to 2 bytes b[0] and b[1]
                        if (i==0){
                            b[i] = (byte) 0;
                        }
                        if (i >= 1 && i <= 3){
                            char c = val.charAt(i);
                            cc = cc  + c;
                        }
                        if (i == 3){
                            int ascii = Integer.parseInt(cc);
                            b[i] = (byte) ascii;
                            bb[i] = ascii;
                        }
                        //char c = val.charAt(i);
                        //int ascii = (int)c;
                        //b[i] = (byte) ascii;
                        //bb[i] = ascii;                        
                    }
                    */
                    if (i >= 4 && i <= 7 ) {
                        // mti
                        char c = val.charAt(i);
                        int ascii = (int)c;
                        b[mtix] = (byte) ascii;
                        bb[i] = ascii;
                    }
                    if (i == 8){
                        System.out.print("bitmap ");
                    }
                    if (i >= 8 && i <= 38){
                        // bitmap
                        int vx = Integer.parseInt(val.substring(i, i + 2), 16);
                        System.out.println("qqq:"+vx);
                        b[bitx] = (byte) vx;
                        bb[i] = vx;
                        System.out.println("bitx:" +bitx);
                        System.out.println("vx:"+vx);
                        // use 2 bits        
                        i++; 
                        bitx++;
                    }
                    if (i >= 40){
                        // regular message
                        char cx = val.charAt(i);
                        int asciix = (int)cx;
                        b[msgx] = (byte) asciix;
                        bb[msgx] = asciix;
                        System.out.println("msg i:"+i);
                        System.out.println("msg msgx:"+msgx);                        
                        System.out.println("msg ascixx:"+asciix);       
                        System.out.println("msg cx:"+cx);
                        msgx++;
                    }
                }
                //byte[] b = val.getBytes();        
                //out.printf(val);
                for (int i=0; i < b.length; i++){
                    System.out.print(b[i] + " ");
                    //out.write(bb[i]);
                }
                out.write(b, 0, b.length);        
                
            }
            //else
            //    System.out.println("yy " + line);
        }while(!line.equals("exit"));
           
            //Print all values stored in ConcurrentHashMap instance
            /*Iterator iterator = response.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next().toString();
                String value = (String)response.get(key);
                System.out.println(key + " " + value);
            }*/
        // Now close the connection
       // server.close();
      } catch (IOException ioe) {
        System.out.println("IOException on socket listen: " + ioe+ " Connection was disconnected");
        //ioe.printStackTrace();
      }
    }

}

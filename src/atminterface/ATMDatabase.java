/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atminterface;

/**
 *
 * @author KrabyYap
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
public class ATMDatabase {
    private HttpURLConnection  myURLConnection;
    
    public void ATMDatabase(){
    
    }
    public String sendAction(ConcurrentHashMap inputParams){
        URL url = null;
        System.out.println("request :"+inputParams);
        String action = (String)inputParams.get("request");
        System.out.println("action : "+action);
        try {
            if("Echo".equals(action)){
                System.out.println("AAA");
               //url = new URL("http://192.168.202.8:8028/icbs/ATMInterfaceListener/echoTest"); // PROD
                url = new URL("http://192.168.206.8:8080/icbs/ATMInterfaceListener/echoTest"); // TEST
             // url = new URL("http://192.168.11.8:8080/icbs/ATMInterfaceListener/echoTest"); // DR
            // url = new URL("http://localhost:8040/icbs/ATMInterfaceListener/echoTest");
		//	url = new URL("http://192.168.0.23:8080/icbs/ATMInterfaceListener/echoTest");
               // String response = (String)inputParams.get("echoResponse");
               // return response; 
            }
            if("Transaction".equals(action)){
                System.out.println("pumasok..");
           //   url = new URL("http://192.168.202.8:8028/icbs/ATMInterfaceListener/createTransaction"); // PROD
               url = new URL("http://192.168.206.8:8080/icbs/ATMInterfaceListener/createTransaction"); // TEST
           // url = new URL("http://192.168.11.8:8080/icbs/ATMInterfaceListener/createTransaction"); // DR
            // url = new URL("http://localhost:8040/icbs/ATMInterfaceListener/createTransaction");
		//    url = new URL("http://192.168.0.23:8080/icbs/ATMInterfaceListener/createTransaction");
            }
            
            Map<String,Object> params = new LinkedHashMap<>();
            Iterator iterator = inputParams.keySet().iterator();
            
            while (iterator.hasNext()) {
                String key = iterator.next().toString();
                String value = (String)inputParams.get(key);
                params.put(key, value);
            }
            StringBuilder postData = new StringBuilder();
            
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            
            conn.setDoOutput(true);
            System.out.println("3");
            conn.getOutputStream().write(postDataBytes);
            System.out.println("1");
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            //Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            System.out.println("2");
            String output="";
            
            for (int c; (c = in.read()) >= 0; output = output+(char)c);
            System.out.println("output :" + "<<"+output+">>");
            //System.out.println ("from server grails"+output);
            in.close();
            //System.out.println(output);
            output = output.replaceAll("\\n", "");
            output = output.replaceAll("\\r", ""); 
            output = output.replace("\\u0000", ""); 
            //output = "<<"+output+">>";
            return output;
        } 
        catch (MalformedURLException e) { 
            //System.out.println(e);
            // new URL() failed
            
            // ...
        } 
        catch (IOException e) {
            System.out.println(e);
            // openConnection() failed
            // ...
            System.out.println("Failed");
        }  
        return "35505";
    }
}

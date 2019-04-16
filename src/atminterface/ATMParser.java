package atminterface;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ATMParser {
    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }
    public String hexToBinary(String hex1, String hex2) {
        
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
    
    public String returnToClient(ConcurrentHashMap outputParams,String output){
        
        return "";
    }
    
    public ConcurrentHashMap processRequest(String s){
        
        ConcurrentHashMap<String, String> ret = new ConcurrentHashMap<String, String>();
        System.out.println("S :"+s);
        char[]  sa = s.toCharArray();
        char[]  reqLength = new char[4];
        char[]  reqCode = new char[4];
        char[]  reqHexBitmap1 = new char[32];
        char[]  reqHexBitmap2 = new char[16];
        char[]  reqMsg = new char[sa.length - 3];
        String  sReqLength = "", sReqCode = "", sReqHexBitmap1 = "", sReqHexBitmap2 = "", sReqBinBitmap = "", sReqMsg = "", sReqMsgLength = "";
        
        System.arraycopy(sa, 0, reqLength, 0, 4);
        System.arraycopy(sa, 4, reqCode, 0, 4);
        System.arraycopy(sa, 8, reqHexBitmap1, 0, 32);
        //System.arraycopy(sa, 24, reqHexBitmap2, 0, 16);
        System.arraycopy(sa, 40, reqMsg, 0, sa.length - 40);
        
        //Converting above arrays to strings
        sReqLength = new String(reqLength);
        sReqLength = sReqLength.trim();
        sReqCode = new String(reqCode);
        sReqHexBitmap1 = new String(reqHexBitmap1);
        System.out.println("sReqHexBitmap1 :"+sReqHexBitmap1);
        //sReqHexBitmap2 = new String(reqHexBitmap2);
        sReqMsg = new String(reqMsg);
        System.out.println("sReqMsg :"+sReqMsg);
        sReqMsgLength = Integer.toString(reqMsg.length);
        
        if(sReqCode.equals("0800")){
            ret.put("request", "Echo");
            //String cRespBitMap = "82200000000200000400000000000000";
            String cRespBitMap =   "82200000020000000400000000000000";
            //ret.put("echoResponse", new StringBuilder().append(sReqLength).append("0810").append(sReqHexBitmap1).append(sReqHexBitmap2).append(sReqMsg).toString());
            String sEchoResp = sReqMsg.substring(0,16) + "00" + sReqMsg.substring(16,19);
            ret.put("echoResponse", new StringBuilder().append("0041").append("0810").append(cRespBitMap).append(sEchoResp).toString());
            sReqBinBitmap = hexToBinary(sReqHexBitmap1, sReqHexBitmap2);
            System.out.println("sEchoResp :"+sEchoResp);
            System.out.println("ReqBitmap :"+sReqBinBitmap);
            System.out.println("sReqLength :"+sReqLength);            
            return ret;
        }
        if(sReqCode.equals("0200") || sReqCode.equals("0420")){
            ret.put("request", "Transaction");
        }
        
        sReqBinBitmap = hexToBinary(sReqHexBitmap1, sReqHexBitmap2);
        System.out.println("ReqBitmap :"+sReqBinBitmap);
        System.out.println("sReqLength :"+sReqLength);
        
        ret.put("msgLength", sReqLength);
        ret.put("reqHexBitmap1", sReqHexBitmap1);
        ret.put("reqHexBitmap2", sReqHexBitmap2);
        ret.put("reqBinBitmap", sReqBinBitmap);
        ret.put("reqMsg", sReqMsg);
        ret.put("reqMsgLength", sReqMsgLength);
        ret.put("msgContent", s);
        ret.put("reqCode", sReqCode);
        
        return ret;
    }
}
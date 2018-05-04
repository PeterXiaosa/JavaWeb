package util;

import net.sf.json.JSONObject;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseUtil {
    public static JSONObject getDataFromRequest(HttpServletRequest request) {
        JSONObject jsonObject;

        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(), "utf-8"));
            sb = new StringBuffer("");
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        String params = sb.toString();
        jsonObject = JSONObject.fromObject(params);
        return jsonObject;
    }

    public static boolean isPhone(String inputText) {
        Pattern p = Pattern.compile("^((14[0-9])|(13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }

//    public static String generateSignature(String account, String password, String deviceId, String genKey){
//
//    }
}

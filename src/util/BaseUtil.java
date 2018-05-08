package util;

import net.sf.json.JSONObject;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseUtil {
    private static final String TOKEN_SUFFIX = "Peter";
    private static final String TOKEN_SOURCE = "0123456789abcdefghijklmnopqrstuvwxyz";
    public static final int lengthOfNonce = 7;
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static JSONObject getDataFromRequest(HttpServletRequest request) {
        return getJsonObject(request);
    }

    public static JSONObject getJsonObject(HttpServletRequest request) {
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

    public static String createAccessToken(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

        StringBuilder builder = new StringBuilder();
        builder.append(TOKEN_SUFFIX);

        int length = 7;
        String source = TOKEN_SOURCE;
        int maxPos = source.length() - 1;
        Random random = new Random();

        StringBuilder builderSHA = new StringBuilder();
        builderSHA.append(df.format(new Date()));
        for (int i = 0; i < length; i++)
        {
            int index = random.nextInt(maxPos);
            builderSHA.append(source.substring(0, index));
        }
        builder.append(MD5Util.md5Password(builderSHA.toString()));

        return builder.toString();
    }

    public static String generateSignature(String genkey, String timeStamp, String nonce){
        String[] strs = {genkey, timeStamp, nonce};
        String[] sortedStrs = sortByDictory(strs);
        String tempString = "";

        if (sortedStrs[0].equals(genkey)){
            tempString = genkey;
        }
        else if (sortedStrs[0].equals(nonce)){
            tempString = nonce;
        }
        else if (sortedStrs[0].equals(timeStamp)){
            tempString = timeStamp;
        }

        if (sortedStrs[1].equals(genkey)){
            tempString =  tempString +genkey;
        }
        else if (sortedStrs[1].equals(nonce)){
            tempString = tempString +nonce;
        }
        else if (sortedStrs[1].equals(timeStamp)){
            tempString = tempString + timeStamp;
        }

        if (sortedStrs[2].equals(genkey)){
            tempString = tempString +genkey;
        }
        else if (sortedStrs[2].equals(nonce)){
            tempString = tempString +nonce;
        }
        else if (sortedStrs[2].equals(timeStamp)){
            tempString = tempString +timeStamp;
        }
        return encode(tempString);
    }

    private static String[] sortByDictory(String[] strs){
        for (int i = 0; i < strs.length - 1; i++) {
            boolean change = false; // 用作冒泡排序的标记，如果一趟排序存在交换，则change设为true，说明还需要下一趟排序
            for (int j = 0; j < strs.length - i - 1; j++) {
                if (bigger(strs[j], strs[j + 1])) {
                    // swap(s[j], s[j + 1]);
                    String tmp = strs[j];
                    strs[j] = strs[j + 1];
                    strs[j + 1] = tmp;
                    change = true;
                }
            }
            if (!change) {
                break; // 当change为false的时候，说明不需要再冒泡了
            }
        }
        return strs;
    }

    private static boolean bigger(String s1, String s2) {
        int length1 = s1.length();
        int length2 = s2.length();
        int i = 0;
        while (i < length1 && i < length2) {
            if (s1.charAt(i) > s2.charAt(i)) {
                return true;
            } else if (s1.charAt(i) < s2.charAt(i)) {
                return false;
            } else {
                i++;
            }
        }
        if (i == length1) {
            return false;
        } else {
            return true;
        }
    }

    private static String encode(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static boolean compareDate(Date start, Date end){
        if (start.compareTo(end) > 0) {
            System.out.println("Date1 is after Date2");
            return true;
        } else if (start.compareTo(end) < 0) {
            return false;
        } else if (start.compareTo(end) == 0) {
            return true;
        }
        return true;
    }
}

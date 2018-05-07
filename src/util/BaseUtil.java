package util;

import net.sf.json.JSONObject;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
//    public static String generateSignature(String account, String password, String deviceId, String genKey){
//
//    }

    public  static int getTableCountInDB(String tableName) throws Exception {
        Connection conn = DBConnectionUtil.getConnection();

        // 获取用户数量
        int count = 0;
        String sql = "SELECT COUNT(*) FROM ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, tableName);
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()){
            count= resultSet.getInt(1);
        }

        DBConnectionUtil.close(resultSet, pstmt, conn);
        return count;
    }
}

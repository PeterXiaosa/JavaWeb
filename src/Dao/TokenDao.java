package Dao;

import util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TokenDao {
    private static int TOKEN_TIME_OUT_HOUR = 2;
    private static int TOKEN_TIME_OUT_MINUTE = 1;

    // 将Access Token插入数据库
    public static int addTokenToDB(String account, String accesstoken) throws Exception{
        String startTime, endTime;
        Connection conn = DBConnectionUtil.getConnection();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        startTime = df.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, TOKEN_TIME_OUT_HOUR);
        date = calendar.getTime();
        endTime = df.format(date);

        String sql = "INSERT INTO `mydbsystem`.`TokenTest` (`id`, `account`, `starttime`, `endtime`, `accesstoken`) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setInt(1, DBConnectionUtil.getUserCountInDB("TokenTest") + 1);
        psmt.setString(2, account);
        psmt.setString(3, startTime);
        psmt.setString(4, endTime);
        psmt.setString(5, accesstoken);
        int result = psmt.executeUpdate();

        DBConnectionUtil.close(psmt, conn);

        return result;
    }
}

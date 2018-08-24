package Dao;

import bean.Token;
import bean.UserInfo;
import util.BaseUtil;
import util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TokenDao {
    private static final int ACCESS_TOKEN_TIME_OUT_HOUR = 2;
    private static final int ACCESS_TOKEN_TIME_OUT_MINUTE = 1;

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void InsertAccessTokenToDB(String account, String accessToken) throws Exception{
        Connection conn = DBConnectionUtil.getConnection();

        Date startTime = new Date();

        String startTimeStr = df.format(startTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.HOUR, ACCESS_TOKEN_TIME_OUT_HOUR);
        Date endTime = calendar.getTime();
        String endTimeStr = df.format(endTime);

        String sql = "INSERT INTO `playappserver`.`Token` (`id`, `account`, `accesstoken`, `starttime`, `endtime`) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setInt(1, DBConnectionUtil.getTableCountInDB("Token") + 1);
        psmt.setString(2, account);
        psmt.setString(3, accessToken);
        psmt.setString(4, startTimeStr);
        psmt.setString(5, endTimeStr);
        int result = psmt.executeUpdate();

        DBConnectionUtil.close(psmt, conn);
//        return result;
    }

    public static boolean isAccountHasAccessToken(UserInfo userInfo) throws Exception{
        Connection conn = DBConnectionUtil.getConnection();
        String sql = "SELECT accesstoken FROM Token WHERE account = ?";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setString(1, userInfo.getAccount());
        ResultSet resultSet = psmt.executeQuery();

        if (resultSet.next()){
            DBConnectionUtil.close(resultSet, psmt, conn);
            return true;
        }else {
            DBConnectionUtil.close(resultSet, psmt, conn);
            return false;
        }
    }

    public static String getAccessTokenByAccount(UserInfo userInfo) throws Exception{
        String accessToken = "";
        Connection conn = DBConnectionUtil.getConnection();

        String sql = "SELECT accesstoken FROM Token WHERE account = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userInfo.getAccount());
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            accessToken = resultSet.getString("accesstoken");
        }

        DBConnectionUtil.close(resultSet, pstmt, conn);
        return accessToken;
    }

    public static boolean isAccessTokenTimeOut(String account) throws Exception{
        Connection conn = DBConnectionUtil.getConnection();
        String sql = "SELECT starttime, endtime FROM playappserver.Token WHERE account = ?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, account);
        ResultSet resultSet = pstm.executeQuery();


        if (resultSet.next()){
            String endTime = resultSet.getString("endtime");
            Date now = new Date();
            Date end = df.parse(endTime);
            DBConnectionUtil.close(resultSet, pstm, conn);
            return BaseUtil.compareDate(now, end);
        }
        DBConnectionUtil.close(resultSet, pstm, conn);
        return false;
    }

    public static void updateAccessTokenInDB(String account, String accessToken) throws Exception{
        Connection conn = DBConnectionUtil.getConnection();
        Date startTime = new Date();

        String startTimeStr = df.format(startTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.HOUR, ACCESS_TOKEN_TIME_OUT_HOUR);
        Date endTime = calendar.getTime();
        String endTimeStr = df.format(endTime);

        String sql = "UPDATE playappserver.Token SET accesstoken = ?, starttime = ? , endtime = ? WHERE account = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, accessToken);
        pstmt.setString(2, startTimeStr);
        pstmt.setString(3, endTimeStr);
        pstmt.setString(4, account);
        int result = pstmt.executeUpdate();
        DBConnectionUtil.close(pstmt, conn);
//        return result;
    }
}

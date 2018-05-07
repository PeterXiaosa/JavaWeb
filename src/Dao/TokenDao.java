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

    public static int InsertAccessTokenToDB(String account, String accessToken) throws Exception{
        Connection conn = DBConnectionUtil.getConnection();

        Token token = new Token();
        token.setAccessToken(accessToken);
        Date startTime = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.format(startTime);
        token.setStartTime(startTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.HOUR, ACCESS_TOKEN_TIME_OUT_HOUR);
        Date endTime = calendar.getTime();

        String sql = "INSERT INTO `mydbsystem`.`UserInfoTest` (`id`, `account`, `accesstoken`, `starttime`, `endtime`) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setInt(1, BaseUtil.getTableCountInDB("TokenTest") + 1);
        psmt.setString(2, account);
        psmt.setString(3, accessToken);
        //TODO 先将时间存为null
        psmt.setDate(4, null);
        psmt.setDate(5, null);
        int result = psmt.executeUpdate();

        DBConnectionUtil.close(psmt, conn);

        return result;
    }

    public static boolean isAccountHasAccessToken(UserInfo userInfo) throws Exception{
        Connection conn = DBConnectionUtil.getConnection();
        String sql = "SELECT accesstoken FROM TokenTest WHERE account = ?";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setString(1, userInfo.getAccount());
        ResultSet resultSet = psmt.executeQuery();
        DBConnectionUtil.close(resultSet, psmt, conn);
        if (resultSet.next()){
            return true;
        }else {
            return false;
        }
    }

    public static String getAccessTokenByAccount(UserInfo userInfo) throws Exception{
        String accessToken = "";
        Connection conn = DBConnectionUtil.getConnection();

        String sql = "SELECT accesstoken FROM TokenTest WHERE account = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userInfo.getAccount());
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            accessToken = resultSet.getString("accesstoken");
        }

        DBConnectionUtil.close(resultSet, pstmt, conn);
        return accessToken;
    }
}

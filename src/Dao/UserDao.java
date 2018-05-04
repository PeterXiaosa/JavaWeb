package Dao;

import bean.UserInfo;
import util.DBConnectionUtil;

import java.sql.*;

public class UserDao {
    private static DBConnectionUtil dbUtil = new DBConnectionUtil();

    public static int addUser(UserInfo userInfo) throws Exception{
        Connection conn = DBConnectionUtil.getConnection();

        String sql = "INSERT INTO `mydbsystem`.`UserInfoTest` (`id`, `account`, `password`, `genkey`) VALUES (?, ?, ?, ?)";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setInt(1, getUserCountInDB() + 1);
        psmt.setString(2, userInfo.getAccount());
        psmt.setString(3, userInfo.getPassword());
        psmt.setString(4, userInfo.getGenkey());
        int result = psmt.executeUpdate();
        return result;
    }

    public static boolean checkUser(UserInfo userInfo) throws Exception{
        String account = userInfo.getAccount();

        Connection conn = DBConnectionUtil.getConnection();
        String sql = "SELECT * FROM UserInfoTest WHERE account = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, account);
        ResultSet result = pstmt.executeQuery();
        if (result.next()){
            return true;
        }else {
            return false;
        }
    }

    public  static int getUserCountInDB() throws Exception {
        Connection conn = DBConnectionUtil.getConnection();

        // 获取用户数量
        int count = 0;
        String sql = "SELECT COUNT(*) FROM UserInfoTest";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()){
            count= resultSet.getInt(1);
        }
        return count;
    }

    public static String getGenKeyFromDB(UserInfo userInfo) throws Exception{
        String genkey = "";
        Connection conn = DBConnectionUtil.getConnection();

        String sql = "SELECT genkey FROM UserInfoTest WHERE account = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userInfo.getAccount());
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            genkey = resultSet.getString("genkey");
        }
        return genkey;
    }

    public static boolean isUserLegal(UserInfo userInfo) {
        if (userInfo != null && userInfo.getAccount() != null && ! "".equals(userInfo.getAccount())
                && userInfo.getPassword() != null && ! "".equals(userInfo.getPassword())
                && userInfo.getGenkey() != null && ! "".equals(userInfo.getGenkey())
                && userInfo.getDeviceId() != null && ! "".equals(userInfo.getDeviceId())){
            return true;
        }else {
            return false;
        }
    }
}

package Dao;

import bean.UserInfo;
import util.DBConnectionUtil;

import java.sql.*;

public class UserDao {

    public static int addUser(UserInfo userInfo) throws Exception{
        Connection conn = DBConnectionUtil.getConnection();

        String sql = "INSERT INTO `mydbsystem`.`UserInfoTest` (`id`, `account`, `password`, `genkey`) VALUES (?, ?, ?, ?)";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setInt(1, DBConnectionUtil.getUserCountInDB("UserInfoTest") + 1);
        psmt.setString(2, userInfo.getAccount());
        psmt.setString(3, userInfo.getPassword());
        psmt.setString(4, userInfo.getGenkey());
        int result = psmt.executeUpdate();

        DBConnectionUtil.close(psmt, conn);

        return result;
    }

    public static boolean checkUser(UserInfo userInfo) throws Exception{
        String account = userInfo.getAccount();

        Connection conn = DBConnectionUtil.getConnection();
        String sql = "SELECT * FROM UserInfoTest WHERE account = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, account);
        ResultSet result = pstmt.executeQuery();

        DBConnectionUtil.close(result, pstmt, conn);
        if (result.next()){
            return true;
        }else {
            return false;
        }
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
        DBConnectionUtil.close(resultSet, pstmt, conn);
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

    public static boolean checkPasswordIsRight(UserInfo userInfo) throws Exception{
        String password = "";
        Connection conn = DBConnectionUtil.getConnection();

        String sql = "SELECT password FROM UserInfoTest WHERE account = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userInfo.getAccount());
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            password = resultSet.getString("password");
        }

        DBConnectionUtil.close(resultSet, pstmt, conn);
        if (password.equals(userInfo.getPassword())){
            return true;
        }else {
            return false;
        }
    }

    public static int updaeUserInfoAfterLogin(UserInfo userInfo) throws Exception{
        Connection conn = DBConnectionUtil.getConnection();
        String sql = "UPDATE UserInfoTest SET deviceid = ?, genkey = ? WHERE account = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userInfo.getDeviceId());
        pstmt.setString(2, userInfo.getGenkey());
        pstmt.setString(3, userInfo.getAccount());
        int result = pstmt.executeUpdate();
        DBConnectionUtil.close(pstmt, conn);
        return result;
    }


}

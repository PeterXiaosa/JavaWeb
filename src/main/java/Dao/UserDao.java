package Dao;

import bean.UserInfo;
import util.BaseUtil;
import util.DBConnectionUtil;

import java.sql.*;

public class UserDao {

    public static int addUser(UserInfo userInfo) throws Exception{
        Connection conn = DBConnectionUtil.getConnection();

        String sql = "INSERT INTO `playappserver`.`userinfo` (`id`, `account`, `password`, `genkey`, `deviceid`) VALUES (?, ?, ?, ?,?)";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setInt(1, DBConnectionUtil.getTableCountInDB("userinfo") + 1);
        psmt.setString(2, userInfo.getAccount());
        psmt.setString(3, userInfo.getPassword());
        psmt.setString(4, userInfo.getGenkey());
        psmt.setString(5, userInfo.getDeviceId());
        int result = psmt.executeUpdate();

        DBConnectionUtil.close(psmt, conn);

        return result;
    }

    public static boolean checkUser(UserInfo userInfo) throws Exception{
        String account = userInfo.getAccount();

        Connection conn = DBConnectionUtil.getConnection();
        String sql = "SELECT * FROM userinfo WHERE account = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, account);
        ResultSet result = pstmt.executeQuery();
        if (result.next()){
            DBConnectionUtil.close(result, pstmt, conn);
            return true;
        }else {
            DBConnectionUtil.close(result, pstmt, conn);
            return false;
        }
    }

    public static String getGenKeyFromDB(String account) throws Exception{
        String genkey = "";
        Connection conn = DBConnectionUtil.getConnection();

        String sql = "SELECT genkey FROM userinfo WHERE account = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, account);
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

        String sql = "SELECT password FROM userinfo WHERE account = ?";
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
        String sql = "UPDATE userinfo SET deviceid = ?, genkey = ? , password = ? WHERE account = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userInfo.getDeviceId());
        pstmt.setString(2, userInfo.getGenkey());
        pstmt.setString(3, userInfo.getPassword());
        pstmt.setString(4, userInfo.getAccount());
        int result = pstmt.executeUpdate();
        DBConnectionUtil.close(pstmt, conn);
        return result;
    }

    public static int getUseCountFromDB(String deviceid) throws Exception{
        int count = 0;
        Connection conn = DBConnectionUtil.getConnection();

        String sql = "SELECT usecount FROM AppCalculate WHERE deviceid = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, deviceid);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            count = resultSet.getInt("usecount");
        }
        DBConnectionUtil.close(resultSet, pstmt, conn);
        return count;
    }

    public static boolean isHasUseApp(String deviceid) throws Exception{
        Connection conn = DBConnectionUtil.getConnection();
        String sql = "SELECT * FROM AppCalculate WHERE deviceid = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, deviceid);
        ResultSet result = pstmt.executeQuery();

        if (result.next()){
            DBConnectionUtil.close(result, pstmt, conn);
            return true;
        }else {
            DBConnectionUtil.close(result, pstmt, conn);
            return false;
        }
    }

    public static int updaeUseAppCount(String deviceid) throws Exception{
        Connection conn = DBConnectionUtil.getConnection();
        String sql = "UPDATE AppCalculate SET usecount = ? WHERE deviceid = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, getUseCountFromDB(deviceid) + 1);
        pstmt.setString(2, deviceid);
        int result = pstmt.executeUpdate();
        DBConnectionUtil.close(pstmt, conn);
        return result;
    }

    public  static int getTableCountInApp(String tableName) throws Exception {
        Connection conn = DBConnectionUtil.getConnection();
        // 获取用户数量
        int count = 0;
//        String sql = "SELECT COUNT(*) FROM ?";
        String sql = "SELECT count(1) FROM AppCalculate";
        PreparedStatement pst = conn.prepareStatement(sql);
//        pst.setString(1, tableName);
        ResultSet resultSet = pst.executeQuery();
        while (resultSet.next()){
            count= resultSet.getInt(1);
        }

        DBConnectionUtil.close(resultSet, pst, conn);
        return count;
    }
}

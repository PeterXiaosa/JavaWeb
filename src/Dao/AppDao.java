package Dao;

import util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppDao {


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
        String sql = "UPDATE playappserver.AppCalculate SET usecount = ? WHERE deviceid = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, getUseCountFromDB(deviceid) + 1);
        pstmt.setString(2, deviceid);
        int result = pstmt.executeUpdate();
        DBConnectionUtil.close(pstmt, conn);
        return result;
    }

    public static int getUseCountFromDB(String deviceid) throws Exception{
        int count = 0;
        Connection conn = DBConnectionUtil.getConnection();

        String sql = "SELECT usecount FROM playappserver.AppCalculate WHERE deviceid = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, deviceid);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            count = resultSet.getInt("usecount");
        }
        DBConnectionUtil.close(resultSet, pstmt, conn);
        return count;
    }

    public static void addNewUser(String deviceId) throws Exception {
        Connection conn = DBConnectionUtil.getConnection();
        int acount = DBConnectionUtil.getTableCountInDB("AppCalculate");
        String sql = "INSERT INTO `playappserver`.`AppCalculate` ( `id`, `deviceid`, `usecount`, `isallowed`) VALUES (?, ?, ?, ?)";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setInt(1, acount + 1);
        psmt.setString(2, deviceId);
        psmt.setInt(3, UserDao.getUseCountFromDB(deviceId) + 1);
        psmt.setBoolean(4, true);
        int result = psmt.executeUpdate();
        DBConnectionUtil.close(psmt, conn);
    }

    public static boolean isAllowedUseApp(String deviceid) throws Exception {
        boolean result = true;
        Connection conn = DBConnectionUtil.getConnection();
        String sql = "SELECT isallowed FROM AppCalculate WHERE deviceid = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, deviceid);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            result = resultSet.getBoolean("isallowed");
        }
        DBConnectionUtil.close(resultSet, pstmt, conn);
        return result;
    }
}

package Dao;

import bean.UserInfo;
import util.BaseUtil;
import util.DBConnectionUtil;

import java.sql.*;

public class UserDao {

    public static int addUser(UserInfo userInfo) throws Exception{
        Connection conn = DBConnectionUtil.getConnection();

        String sql = "INSERT INTO `playappserver`.`userinfo` (`id`, `account`, `password`, `genkey`, `deviceid`) VALUES (?, ?, ?, ?, ?)";
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

    public static boolean checkUser(String account) throws Exception{
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

    public static boolean isUserHasMatched(String account) {
        Connection conn = null;
        String partnerAccount = null, loveAuth = null;

        try {
            conn = DBConnectionUtil.getConnection();
            String sql = "SELECT partneraccount, love_auth FROM userinfo WHERE account = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, account);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()){
                partnerAccount = resultSet.getString("partneraccount");
                loveAuth = resultSet.getString("love_auth");
            }

            DBConnectionUtil.close(resultSet, pstmt, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (partnerAccount != null && loveAuth != null){
            return true;
        }else {
            return false;
        }
    }

    public static String getLoveAuthByAccount(String account) {
        Connection conn = null;
        String  loveAuth = null;

        try {
            conn = DBConnectionUtil.getConnection();
            String sql = "SELECT love_auth FROM userinfo WHERE account = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, account);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()){
                loveAuth = resultSet.getString("love_auth");
            }

            DBConnectionUtil.close(resultSet, pstmt, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loveAuth;
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

    public static UserInfo getUserInfoByDeviceId(String deviceId) throws Exception {
        Connection conn = DBConnectionUtil.getConnection();
        UserInfo userInfo = new UserInfo();

        String sql = "SELECT genkey FROM userinfo WHERE deviceid = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, deviceId);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            String account = resultSet.getString("account");
            String password = resultSet.getString("password");
            String genkey = resultSet.getString("genkey");
            String matchcode = resultSet.getString("matchcode");
            String name = resultSet.getString("name");
            Date birthday = resultSet.getDate("birthday");
            boolean sex = resultSet.getBoolean("sex");
            userInfo.setAccount(account);
            userInfo.setPassword(password);
            userInfo.setGenkey(genkey);
            userInfo.setDeviceId(deviceId);
//            userInfo.setMatchcode(matchcode);
            userInfo.setName(name);
            userInfo.setBirthday(birthday.toString());
            userInfo.setSex(sex);
        }
        DBConnectionUtil.close(resultSet, pstmt, conn);
        return userInfo;
    }

    public static UserInfo getUserInfoByAccount(String account) throws Exception {
        Connection conn = DBConnectionUtil.getConnection();
        UserInfo userInfo = new UserInfo();

        String sql = "SELECT * FROM userinfo WHERE account = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, account);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            String deviceId = resultSet.getString("deviceid");
            String password = resultSet.getString("password");
            String genkey = resultSet.getString("genkey");
            String name = resultSet.getString("name");
            Date birthday = resultSet.getDate("birthday");
            boolean sex = resultSet.getBoolean("sex");
            String loveAuth = resultSet.getString("love_auth");
            String partnerAccount = resultSet.getString("partneraccount");
            userInfo.setAccount(account);
            userInfo.setPassword(password);
            userInfo.setGenkey(genkey);
            userInfo.setDeviceId(deviceId);
            userInfo.setName(name);
            userInfo.setBirthday(birthday != null ? birthday.toString() : null);
            userInfo.setSex(sex);
            userInfo.setLoveAuth(loveAuth);
            userInfo.setPartnerAccouont(partnerAccount);
        }
        DBConnectionUtil.close(resultSet, pstmt, conn);
        return userInfo;
    }

    public static int updaeUserInfo(UserInfo userInfo) throws Exception{
        Connection conn = DBConnectionUtil.getConnection();
        String sql = "UPDATE userinfo SET name = ?, sex = ? , birthday = ? WHERE account = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userInfo.getName());
        pstmt.setBoolean(2, userInfo.isSex());
        pstmt.setString(3, userInfo.getBirthday());
        pstmt.setString(4, userInfo.getAccount());
        int result = pstmt.executeUpdate();
        DBConnectionUtil.close(pstmt, conn);
        return result;
    }

    /**
     * 匹配成功之后更新用户的另一半的id到数据库，起绑定作用
     * @param selfAccount  自己账户
     * @param partnerAccount 伙伴账户
     * @param loveAuth 双方验证字符，相当于共同id，公共记录通过loveAuth当做id作用。
     * @return 数据库操作结果
     * @throws Exception
     */
    public static int updateUserPartnerid(String selfAccount, String partnerAccount, String loveAuth) throws Exception {
        Connection conn = DBConnectionUtil.getConnection();
        String sql = "UPDATE userinfo SET partneraccount = ? , love_auth = ? WHERE account = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, partnerAccount);
        pstmt.setString(2, loveAuth);
        pstmt.setString(3, selfAccount);
        int result = pstmt.executeUpdate();
        DBConnectionUtil.close(pstmt, conn);
        return result;
    }
}

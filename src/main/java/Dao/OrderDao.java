package Dao;

import bean.SOrderInsert;
import net.sf.json.JSONArray;
import util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class OrderDao {

    public static void recordOrder(SOrderInsert orderInsert, String appVersion, String systemVersion, String deviceId, String deviceType, String eventTime, String page, String action) throws Exception {
        Connection conn = DBConnectionUtil.getConnection();

        String sql = "INSERT INTO `playappserver`.`OrderInfo` (`id`, `company_no`, `user_no`, `address_no`, `app_version`, `android_version`, " +
                "`device_id`, `event_time`, `page`, `action`, `contract_no`, `device_type`) " +
                "VALUES (?, ?, ?, ?,?,?,?,?,?,?,?,?)";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setInt(1, DBConnectionUtil.getTableCountInDB("OrderInfo") + 1);
        psmt.setString(2, orderInsert.getCompanyNo());
        psmt.setString(3, orderInsert.getUserNo());
        psmt.setString(4, orderInsert.getAddressNo());
        psmt.setString(5, appVersion);
        psmt.setString(6, systemVersion);
        psmt.setString(7, deviceId);
        psmt.setString(8, eventTime);
        psmt.setString(9, page);
        psmt.setString(10, action);
        psmt.setString(11, orderInsert.getContractNo());
        psmt.setString(12, deviceType);
        int result = psmt.executeUpdate();

        DBConnectionUtil.close(psmt, conn);
    }
}

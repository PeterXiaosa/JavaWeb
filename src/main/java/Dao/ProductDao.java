package Dao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDao {

    public static JSONArray getProductByType(int type) throws Exception {
        String name, img_url;
        double price;
        JSONArray jsonArray = new JSONArray();

        Connection conn = DBConnectionUtil.getConnection();
        String sql = "SELECT * FROM ProductTable WHERE TYPE = ?";
        PreparedStatement psmt = conn.prepareStatement(sql);
//        psmt.setString(1, userInfo.getAccount());
        psmt.setInt(1, type);
        ResultSet resultSet = psmt.executeQuery();
        while (resultSet.next()){
            name = resultSet.getString("name");
            img_url = resultSet.getString("image_url");
            price = resultSet.getDouble("price");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("image_url", img_url);
            jsonObject.put("price", price);
            jsonArray.add(jsonObject);
        }
        DBConnectionUtil.close(resultSet, psmt, conn);
        return jsonArray;
    }

    public static JSONArray getProductType() throws Exception {
        JSONArray jsonArray = new JSONArray();
        Connection conn = DBConnectionUtil.getConnection();
        String sql = "SELECT * FROM ProductTypeTable";
        PreparedStatement psmt = conn.prepareStatement(sql);
        ResultSet resultSet = psmt.executeQuery();
        while (resultSet.next()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", resultSet.getInt("type"));
            jsonObject.put("type_name", resultSet.getString("type_name"));
            jsonObject.put("image_url", resultSet.getString("image_url"));
            jsonArray.add(jsonObject);
        }
        DBConnectionUtil.close(resultSet, psmt, conn);
        return jsonArray;
    }
}

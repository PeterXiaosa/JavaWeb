package servlet;

import Dao.UserDao;
import bean.UserInfo;
import com.google.gson.Gson;
import net.sf.json.JSONObject;
import util.BaseUtil;
import util.DBConnectionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/calapp")
public class AppCalculateServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject responseJson = new JSONObject();
        try {
            // 获取Client端数据
            JSONObject requestJson = BaseUtil.getDataFromRequest(request);
            String deviceId = requestJson.getString("deviceid");

            if (UserDao.isHasUseApp(deviceId)){
                UserDao.updaeUseAppCount(deviceId);
            }else {
                Connection conn = DBConnectionUtil.getConnection();
                int acount = UserDao.getTableCountInApp("AppCalculate");
                String sql = "INSERT INTO `mydbsystem`.`AppCalculate` ( `id`, `deviceid`, `usecount`) VALUES (?, ?, ?)";
                PreparedStatement psmt = conn.prepareStatement(sql);
                psmt.setInt(1, acount + 1);
                psmt.setString(2, deviceId);
                psmt.setInt(3, UserDao.getUseCountFromDB(deviceId) + 1);
                int result = psmt.executeUpdate();
                DBConnectionUtil.close(psmt, conn);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            PrintWriter printWriter = response.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }
    }
}

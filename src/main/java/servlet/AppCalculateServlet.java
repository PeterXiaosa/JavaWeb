package servlet;

import Dao.AppDao;
import Dao.UserDao;
import bean.UserInfo;
import com.google.gson.Gson;
import net.sf.json.JSONObject;
import sun.rmi.runtime.Log;
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
import java.util.logging.Logger;

@WebServlet("/calapp")
public class AppCalculateServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject responseJson = new JSONObject();
        try {
            // 获取Client端数据
            JSONObject requestJson = BaseUtil.getDataFromRequest(request);
            String deviceId = requestJson.getString("deviceid");

            if (AppDao.isHasUseApp(deviceId)){
                AppDao.updaeUseAppCount(deviceId);
                System.out.println("updaeUseAppCount");
            }else {
                AppDao.addNewUser(deviceId);
                System.out.println("addNewUser");
            }

        } catch (Exception e){
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

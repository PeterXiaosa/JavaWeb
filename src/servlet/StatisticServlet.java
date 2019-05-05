package servlet;

import Dao.AppDao;
import bean.RenHeUserInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.BaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/getuserpermission")
public class StatisticServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject responseJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            // 获取Client端数据
            JSONObject requestJson = BaseUtil.getDataFromRequest(request);

            List<RenHeUserInfo> result = AppDao.getUserInfoList();

            for (RenHeUserInfo userInfo : result){
                JSONObject object = new JSONObject();
                object.put("deviceId", userInfo.getDeviceId());
                object.put("isAllowed", userInfo.isAllowed());
                jsonArray.add(object);
            }

            responseJson.put("status", 0);
            responseJson.put("msg", "获取数据成功");
            responseJson.put("data", jsonArray);

        } catch (Exception e){
            e.printStackTrace();
            responseJson.put("status", 1);
            responseJson.put("msg", "获取数据失败");
            responseJson.put("data", jsonArray);
        }
        finally {
            PrintWriter printWriter = response.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }
    }
}

package servlet.renheAPI;

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

@WebServlet("/setuserpermission")
public class AppPermissionControlServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject responseJson = new JSONObject();
        try {
            // 获取Client端数据
            JSONObject requestJson = BaseUtil.getDataFromRequest(request);
            String deviceId = requestJson.getString("deviceid");
            boolean isAllowed = requestJson.getBoolean("isallowed");

            AppDao.modifyUserPermission(deviceId, isAllowed);

            responseJson.put("status", 0);
            responseJson.put("msg", "修改权限成功");

        } catch (Exception e){
            e.printStackTrace();
            responseJson.put("status", 1);
            responseJson.put("msg", "修改权限失败");
        }
        finally {
            PrintWriter printWriter = response.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }
    }
}

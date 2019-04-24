package servlet;

import Dao.AppDao;
import net.sf.json.JSONObject;
import util.BaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/allowapp")
public class AppPermissionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject responseJson = new JSONObject();
        try {
            // 获取Client端数据
            JSONObject requestJson = BaseUtil.getDataFromRequest(request);
            String deviceId = requestJson.getString("deviceid");
            boolean isAllowed = true;

            if (AppDao.isHasUseApp(deviceId)){
//                AppDao.updaeUseAppCount(deviceId);
                isAllowed = AppDao.isAllowedUseApp(deviceId);
            }else {
                AppDao.addNewUser(deviceId);
            }

            responseJson.put("status", 0);
            responseJson.put("msg", "获取权限成功");
            responseJson.put("isallowed", isAllowed);

        } catch (Exception e){
            e.printStackTrace();
            responseJson.put("status", 1);
            responseJson.put("msg", "获取权限失败");
            responseJson.put("isallowed", false);
        }
        finally {
            PrintWriter printWriter = response.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }
    }
}

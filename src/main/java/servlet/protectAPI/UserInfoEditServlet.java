package servlet.protectAPI;

import Dao.UserDao;
import bean.UserInfo;
import com.google.gson.Gson;
import net.sf.json.JSONObject;
import util.BaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/certificate/modifyuserinfo")
public class UserInfoEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject responseJson = new JSONObject();

        try {
            // 获取Client端数据
            JSONObject requestJson = BaseUtil.getDataFromRequest(request);
            Gson gson = new Gson();
            UserInfo userInfo = gson.fromJson(requestJson.toString(), UserInfo.class);

            int result = UserDao.updaeUserInfo(userInfo);
            if (result > 0) {
                responseJson.put("status", 0);
                responseJson.put("msg", "用户信息更新成功");
                responseJson.put("data", new JSONObject());
            }else {
                responseJson.put("status", 30012);
                responseJson.put("msg", "用户信息缺少，更新失败");
                responseJson.put("data", new JSONObject());
            }

        }catch (Exception e) {
            responseJson.put("status", 30011);
            responseJson.put("msg", "用户信息更新失败");
            responseJson.put("data", new JSONObject());
        }finally {
            PrintWriter printWriter = resp.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }
    }
}

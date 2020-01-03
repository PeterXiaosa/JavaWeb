package servlet.protectAPI;

import Dao.UserDao;
import bean.UserInfo;
import com.alibaba.fastjson.JSON;
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

/**
 *  查询用户信息
 */

@WebServlet("/certificate/queryuserinfo")
public class UserInfoQueryServlet  extends HttpServlet {

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
            UserInfo user = gson.fromJson(requestJson.toString(), UserInfo.class);
            String account = user.getAccount();
            UserInfo userInfo = UserDao.getUserInfoByAccount(account);

            String userStr = JSON.toJSONString(userInfo);
            responseJson.put("status", 0);
            responseJson.put("msg", "用户信息查询成功");
            responseJson.put("data", userStr);
        }catch (Exception e) {
            responseJson.put("status", 40001);
            responseJson.put("msg", "用户信息查询失败");
            responseJson.put("data", new JSONObject());
        } finally {
            PrintWriter printWriter = resp.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }
    }
}

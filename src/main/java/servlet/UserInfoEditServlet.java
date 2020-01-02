package servlet;

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

            String account = userInfo.getAccount();
            boolean isMale = userInfo.isSex();
//            String birthday = userInfo.get
        }catch (Exception e) {

        }
    }
}

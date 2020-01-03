package servlet.certificate;

import Dao.TokenDao;
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

@WebServlet("/certificate/login")
public class LoginServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject responseJson = new JSONObject();
        try{
            // 获取Client端数据
            JSONObject requestJson = BaseUtil.getDataFromRequest(request);

            Gson gson = new Gson();
            UserInfo userInfo = gson.fromJson(requestJson.toString(), UserInfo.class);

            do {
                if (! UserDao.checkPasswordIsRight(userInfo)){
                    responseJson.put("status", 10011);
                    responseJson.put("msg", "密码错误");
                    responseJson.put("data", new JSONObject());
                    break;
                }

                String accessToken = "";
                // 登录成功在UserInfo表中更新用户信息。
                UserDao.updaeUserInfoAfterLogin(userInfo);
                JSONObject jsonObject = new JSONObject();
                if (! TokenDao.isAccountHasAccessToken(userInfo)){
                    //创建AccessToken之后将其存入数据库中
                    accessToken = BaseUtil.createAccessToken();
                    TokenDao.InsertAccessTokenToDB(userInfo.getAccount(), accessToken);

                    responseJson.put("status", 0);
                    responseJson.put("msg", "账号密码正确");
                    responseJson.put("data", jsonObject);
                    break;
                }

                if (TokenDao.isAccessTokenTimeOut(userInfo.getAccount())){
                    responseJson.put("status", 10012);
                    responseJson.put("msg", "accesstoken过期");
                    responseJson.put("data", new JSONObject());
                    break;
                }

                accessToken = TokenDao.getAccessTokenByAccount(userInfo);
                UserInfo user = UserDao.getUserInfoByAccount(userInfo.getAccount());
                responseJson.put("status", 0);
                responseJson.put("msg", "账号密码正确");
                jsonObject.put("accesstoken", accessToken);
                jsonObject.put("account", user.getAccount());
                jsonObject.put("deviceId", user.getDeviceId());
                jsonObject.put("name", user.getName());
                jsonObject.put("birthday", user.getBirthday());
                jsonObject.put("sex", user.isSex());
                responseJson.put("data", jsonObject);
            }while (false);

        }catch (Exception e){
            responseJson = new JSONObject();
            responseJson.put("status", 1);
            responseJson.put("msg", "登录失败 : " + e.toString());
            responseJson.put("data", new JSONObject());
        }finally {
            PrintWriter printWriter = resp.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }
    }
}

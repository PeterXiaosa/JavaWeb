package servlet;

import Dao.TokenDao;
import Dao.UserDao;
import bean.UserInfo;
import com.google.gson.Gson;
import net.sf.json.JSONObject;
import util.BaseUtil;
import util.DBConnectionUtil;
import util.MD5Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/user/login")
public class LoginServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {

        Statement statement = null;
        JSONObject responseJson = new JSONObject();
        boolean isLeagleAccount = false;

        try{
            // 获取Client端数据
            JSONObject requestJson = BaseUtil.getDataFromRequest(request);

            Gson gson = new Gson();
            UserInfo userInfo = gson.fromJson(requestJson.toString(), UserInfo.class);

            //如果成功登陆则需要更新数据库中的deviceId和genkey数据
            if (UserDao.checkPasswordIsRight(userInfo)){
                String accessToken = BaseUtil.createAccessToken();
                UserDao.updaeUserInfoAfterLogin(userInfo);
                TokenDao.addTokenToDB(userInfo.getAccount(), accessToken);

                JSONObject jsonObject = new JSONObject();
                // 返回Token的同时应该向数据库中插入token信息
                jsonObject.put("accesstoken", accessToken);
                responseJson.put("status", 0);
                responseJson.put("msg", "账号密码正确");
                responseJson.put("content", jsonObject);
            }else {
                responseJson.put("status", 10011);
                responseJson.put("msg", "密码错误");
            }


        }catch (Exception e){
            responseJson = new JSONObject();
            responseJson.put("statuscode", 1);
            responseJson.put("content", "登录失败 : " + e.toString());
        }finally {
            PrintWriter printWriter = resp.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }
    }
}

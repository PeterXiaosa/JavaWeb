package servlet.protectAPI;

import Dao.UserDao;
import bean.UserInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.sf.json.JSONObject;
import util.BaseUtil;
import util.DBConnectionUtil;
import util.MD5Util;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/user/register")
public class RegistServlet extends HttpServlet{

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
            Gson gson = new Gson();
            UserInfo userInfo = gson.fromJson(requestJson.toString(), UserInfo.class);

            if (UserDao.isUserLegal(userInfo)) {
                if (!UserDao.checkUser(userInfo)) {
                    // 注册用户
                    UserDao.addUser(userInfo);
                    responseJson.put("status", 0);
                    responseJson.put("msg", "注册成功");
                } else {
                    // 此用户已存在
                    responseJson.put("status", 10010);
                    responseJson.put("msg", "该账号已存在");
                }
            }else {
                responseJson.put("status", 10001);
                responseJson.put("msg", "Post参数错误");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            responseJson.put("status", 20001);
            responseJson.put("msg", "数据库操作失败");
        }catch (Exception e){
            e.printStackTrace();
            responseJson.put("status", 20000);
            responseJson.put("msg", "未知错误");
        }
        finally {
            responseJson.put("data", new JSONObject());
            PrintWriter printWriter = response.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }
    }
}

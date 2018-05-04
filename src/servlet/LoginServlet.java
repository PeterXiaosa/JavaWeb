package servlet;

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
            Connection connection = DBConnectionUtil.getConnection();
            statement = connection.createStatement();
            // 获取Client端数据
            JSONObject requestJson = BaseUtil.getDataFromRequest(request);

            Gson gson = new Gson();
            UserInfo userInfo = gson.fromJson(requestJson.toString(), UserInfo.class);


            String sql = "SELECT password FROM UsersInfo WHERE phone = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setString(1, account);
            ResultSet resultSetif1 = preparedStatement.executeQuery();
//            while (resultSet1.next()){
//                if (md5Password.equals(resultSet1.getString("password"))){
//                    isLeagleAccount = true;
//                    break;
//                }
//            }
            if (isLeagleAccount){
                responseJson = new JSONObject();
                responseJson.put("statuscode", 0);
                responseJson.put("content", "登录成功");
                PrintWriter printWriter = resp.getWriter();
                printWriter.print(responseJson.toString());
                printWriter.flush();
                printWriter.close();
            }else {
                responseJson = new JSONObject();
                responseJson.put("statuscode", 1);
                responseJson.put("content", "登录失败");
                PrintWriter printWriter = resp.getWriter();
                printWriter.print(responseJson.toString());
                printWriter.flush();
                printWriter.close();
            }

        }catch (Exception e){
            responseJson = new JSONObject();
            responseJson.put("statuscode", 1);
            responseJson.put("content", "登录失败 : " + e.toString());
            PrintWriter printWriter = resp.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }
    }
}

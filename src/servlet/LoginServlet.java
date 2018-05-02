package servlet;

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

@WebServlet("/login")
public class LoginServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = DBConnectionUtil.getConnection();
        Statement statement = null;
        JSONObject responseJson = new JSONObject();
        boolean isLeagleAccount = false;

        try{
            statement = connection.createStatement();
            // 将传入的数据变成json
            JSONObject requestJson = BaseUtil.getDataFromRequest(req);

            String account = requestJson.getString("phone");
            String password = requestJson.getString("password");
            String md5Password = MD5Util.md5Password(password);

            String sql = "SELECT password FROM UsersInfo WHERE phone = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account);
            ResultSet resultSet1 = preparedStatement.executeQuery();
            while (resultSet1.next()){
                if (md5Password.equals(resultSet1.getString("password"))){
                    isLeagleAccount = true;
                    break;
                }
            }
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

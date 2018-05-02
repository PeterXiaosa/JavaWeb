package servlet;

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

@WebServlet("/register")
public class RegistServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = DBConnectionUtil.getConnection();
        Statement statement = null;
        JSONObject responseJson = new JSONObject();
        try {
            statement = connection.createStatement();
            // 将传入的数据变成json
            JSONObject requestJson = BaseUtil.getDataFromRequest(request);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM UsersInfo");
            int number= 0;
            String phone, requestPhone;
            requestPhone = (String) requestJson.get("phone");
            boolean isCanInsertData = true;

            while (resultSet.next()){
                number++;
                phone = resultSet.getString("phone");
                if (requestPhone.equals(phone)){
                    isCanInsertData = false;
                    break;
                }
            }
            if (isCanInsertData) {
                if (BaseUtil.isPhone(requestPhone)) {
                    String passwordToDatabase = MD5Util.md5Password((String) requestJson.get("password"));
                    String sqlAdd = String.format("INSERT INTO `mydbsystem`.`UsersInfo` (`userId`, `phone`, `password`) VALUES ('%d', '%s', '%s');",
                            number + 1, requestJson.get("phone"), passwordToDatabase);
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlAdd);
                    preparedStatement.executeUpdate();

                    responseJson.put("status", 0);
                    responseJson.put("msg", requestPhone);
                }else {
                    responseJson.put("status", 1);
                    responseJson.put("msg", "手机号不合法");
                }
            }else {
                responseJson.put("status", 1);
                responseJson.put("msg", "该手机号已注册");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            responseJson.put("status", 1);
            responseJson.put("msg", "数据插入数据库失败");
        }finally {
            PrintWriter printWriter = response.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }
    }
}

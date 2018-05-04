package servlet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.DBConnectionUtil;

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

import static util.BaseUtil.getJsonObject;

@WebServlet("/getAccount")
public class DBServlet extends HttpServlet{

    // Get data from database and output json.
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
//        String url = "jdbc:mysql://localhost:3306/playappserver";
        String url = "jdbc:mysql://47.100.210.98:3306/mydbsystem";
        String user = "root";
        String password = "root";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            connection = DBConnectionUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM UsersInfo;");

            JSONArray jsonArray = new JSONArray();

            while (resultSet.next()){
                JSONObject jsonObject = new JSONObject();
                int userId = resultSet.getInt("userId");
                String phone = resultSet.getString("phone");
                String name = resultSet.getString("password");
                jsonObject.put("userId", user);
                jsonObject.put("phone", phone);
                jsonObject.put("password", name);
                jsonArray.add(jsonObject);
            }

            resp.setContentType("application/json");
            PrintWriter printWriter = resp.getWriter();
            printWriter.print(jsonArray.toString());
            printWriter.flush();
            printWriter.close();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                resultSet = null;
            }
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                statement = null;
            }
            if (connection != null){
                try {
                    connection.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
                connection = null;
            }
        }
    }

    // Get data from request and write data to database.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        super.doPost(req, resp);
        JSONObject requestJson;
         requestJson = getDataFromRequest(request);
        String url = "jdbc:mysql://localhost:3306/playappserver";
        String user = "root";
        String password = "root";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM user;");
            PreparedStatement pstmt1=null;
            int number = 0;

            while (resultSet.next()){
                number++;
            }

            String sqlAdd = String.format("INSERT INTO `playappserver`.`user` (`id`, `account`, `password`) VALUES ('%d', '%s', '%s');", number+1, requestJson.get("account"), requestJson.get("password"));
            pstmt1 = connection.prepareStatement(sqlAdd);
            pstmt1.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                resultSet = null;
            }
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                statement = null;
            }
            if (connection != null){
                try {
                    connection.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
                connection = null;
            }
        }
    }

    private JSONObject getDataFromRequest(HttpServletRequest request) {
        return getJsonObject(request);
    }
}

package servlet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.BaseUtil;
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

@WebServlet("/contact")
public class ContactServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);

        Statement statement = null;
        JSONObject responseJson = new JSONObject();

        try {
            Connection connection = DBConnectionUtil.getConnection();
            statement = connection.createStatement();
            JSONObject requestJson = BaseUtil.getDataFromRequest(req);
            String userPhone = (String) requestJson.get("phone");


            String dbOperate = "SELECT userId FROM userinfo WHERE phone = '" + userPhone + "'";
            ResultSet resultSet = statement.executeQuery(dbOperate);
            int userId = -1;
//                int userId = resultSet.getInt("userId");
            while (resultSet.next()){
                userId = resultSet.getInt("userId");
            }

            int action = Integer.valueOf(String.valueOf(requestJson.get("action")));
            // action 为0是查询，action为1是插入
            if (action == 0) {
                ResultSet resultSet_contact = statement.executeQuery("SELECT * FROM Contacts WHERE userid = '" + userId +"'");
                JSONArray jsonArray = new JSONArray();
                while (resultSet_contact.next()){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", resultSet_contact.getString("name"));
                    jsonObject.put("phone", resultSet_contact.getString("phone"));
                    jsonArray.add(jsonObject);
                }

                PrintWriter printWriter = resp.getWriter();
                printWriter.print(jsonArray.toString());
                printWriter.flush();
                printWriter.close();
            }else if (action == 1){
//                int numberInContact = 0;
                String contactName = (String) requestJson.get("contactName");
                String contactPhone = (String) requestJson.get("contactPhone");
                boolean isPhoneExit = false;
                String sql = "SELECT phone FROM Contacts WHERE userid = ?";
                PreparedStatement preparedStatement_query = connection.prepareStatement(sql);
                preparedStatement_query.setInt(1, userId);
                ResultSet resultSet_query = preparedStatement_query.executeQuery();
                while (resultSet_query.next()){
                    if (contactPhone.equals(resultSet_query.getString("phone"))){
                        isPhoneExit = true;
                    }
                }
                if (isPhoneExit){
                    //插入的电话已经存在于数据库
                    responseJson = new JSONObject();
                    responseJson.put("status", 1);
                    responseJson.put("msg", "该电话已经被加入");
                    PrintWriter printWriter = resp.getWriter();
                    printWriter.print(responseJson.toString());
                    printWriter.flush();
                    printWriter.close();
                }else {
                    PreparedStatement psql = connection.prepareStatement("INSERT INTO Contacts (userid, name, phone)" + "values(?,?,?)");  //用preparedStatement预处理来执行sql语句
                    psql.setInt(1, userId);
                    psql.setString(2, contactName);
                    psql.setString(3, contactPhone);
                    psql.executeUpdate();
                    psql.close();
                    //插入成功
                    responseJson = new JSONObject();
                    responseJson.put("status", 0);
                    responseJson.put("msg", "数据库插入成功");
                    PrintWriter printWriter = resp.getWriter();
                    printWriter.print(responseJson.toString());
                    printWriter.flush();
                    printWriter.close();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            responseJson = new JSONObject();
            responseJson.put("status", 1);
            responseJson.put("msg", "数据库操作失败" + e.toString());
            PrintWriter printWriter = resp.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }catch (Exception e){
            e.printStackTrace();
            e.printStackTrace();
            responseJson = new JSONObject();
            responseJson.put("status", 1);
            responseJson.put("msg", "未知错误" + e.toString());
            PrintWriter printWriter = resp.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }
    }
}

package servlet.protectAPI;

import Dao.UserDao;
import data.ProtectSocketData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.BaseUtil;
import util.DBConnectionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Random;

@WebServlet("/protect/matchcode/generate")
public class MatchCodeGenerateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject responseJson = new JSONObject();
        responseJson = new JSONObject();
        try {
            JSONObject requestJson = BaseUtil.getDataFromRequest(req);
            String userAccount = requestJson.getString("account");
            if (UserDao.checkUser(userAccount)) {
                String matchCode;
                do {
                    matchCode = generateMatchCode(6);
                }while (! ProtectSocketData.getsInstance().saveMatchCode(matchCode));
                responseJson.put("status", 0);
                responseJson.put("content","匹配码已生成");
                responseJson.put("matchcode", matchCode);
            }else {
                responseJson.put("status", 1);
                responseJson.put("content", "该用户不存在");
            }

            PrintWriter printWriter = resp.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
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
            responseJson = new JSONObject();
            responseJson.put("status", 1);
            responseJson.put("msg", "未知错误" + e.toString());
            PrintWriter printWriter = resp.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }
    }

    private String generateMatchCode(int bitCount){
        String sources = "0123456789"; // 加上一些字母，就可以生成pc站的验证码了
        Random rand = new Random();
        StringBuffer flag = new StringBuffer();
        for (int j = 0; j < bitCount; j++) {
            flag.append(sources.charAt(rand.nextInt(9)) + "");
        }
        return flag.toString();
    }
}
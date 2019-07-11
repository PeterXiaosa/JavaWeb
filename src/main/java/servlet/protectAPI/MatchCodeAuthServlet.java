package servlet.protectAPI;

import data.ProtectSocketData;
import net.sf.json.JSONObject;
import util.BaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Statement;

@WebServlet("/protect/matchcode/auth")
public class MatchCodeAuthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject responseJson = new JSONObject();
        responseJson = new JSONObject();
        try {
            JSONObject requestJson = BaseUtil.getDataFromRequest(req);
            String matchcode = requestJson.getString("matchcode");
            if (ProtectSocketData.getsInstance().isExitMatchCode(matchcode)){
                // 匹配码合法
                responseJson.put("status", 0);
                responseJson.put("content","匹配码合法，允许接入");
            }else {
                responseJson.put("status", 1);
                responseJson.put("content", "匹配码非法");
            }
        }catch (Exception e){
            e.printStackTrace();
            responseJson.put("status", 1);
            responseJson.put("content", "匹配码非法");
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.print(responseJson.toString());
        printWriter.flush();
        printWriter.close();
    }

}

package servlet;

import bean.Picture;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/getAdv")
public class AdvertisementServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", "http://47.100.210.98:8080/photos/xiaosa.jpg");
        jsonObject.put("updatetime", "2018-5-24 10:22:20");
        jsonObject.put("starttime", "2018-5-24 15:22:20");
        jsonObject.put("endtime", "2018-5-24 22:22:20");
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = new PrintWriter(resp.getOutputStream());
        out.print(jsonObject.toString());
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}

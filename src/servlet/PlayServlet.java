package servlet;

import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/test")
public class PlayServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        JSONObject responseJson = new JSONObject();
        responseJson.put("name", "许景彦");
        responseJson.put("description", "小逗比");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(responseJson.toString());
        printWriter.flush();
        printWriter.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        JSONObject responseJson = new JSONObject();
        responseJson.put("name", "许景彦");
        responseJson.put("description", "小逗比");
        responseJson.put("addition", "用post也是个小逗比");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(responseJson.toString());
        printWriter.flush();
        printWriter.close();
    }
}

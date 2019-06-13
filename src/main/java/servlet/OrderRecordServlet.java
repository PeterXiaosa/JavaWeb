package servlet;

import Dao.OrderDao;
import bean.SOrderInsert;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.sf.json.JSONObject;
import util.BaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/order/record")
public class OrderRecordServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject responseJson = new JSONObject();
        try{
            // 获取Client端数据
            JSONObject requestJson = BaseUtil.getDataFromRequest(request);
            JSONObject dataObject = new JSONObject();
            dataObject = requestJson.getJSONObject("user_info");
            String appVersion = dataObject.getString("app_version");
            String systemVersion = dataObject.getString("system_version");
            String deviceId = dataObject.getString("device_id");
            String deviceType = dataObject.getString("device_type");
            String action = requestJson.getString("action");
            String page = requestJson.getString("page");
            String eventTime = requestJson.getString("event_time");

            Gson gson = new Gson();
            SOrderInsert orderInsert = gson.fromJson(requestJson.getJSONObject("order_data").toString(), SOrderInsert.class);
            OrderDao.recordOrder(orderInsert, appVersion, systemVersion, deviceId, deviceType, eventTime, page, action);
        }catch (Exception e){
            responseJson = new JSONObject();
            responseJson.put("statuscode", 1);
            responseJson.put("content", "订单插入失败 : " + e.toString());
        }finally {
            PrintWriter printWriter = resp.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }
    }
}

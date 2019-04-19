package servlet;

import Dao.ProductDao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.BaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/product/type")
public class ProductTypeServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject responseJson = new JSONObject();
        try {
            JSONArray jsonArray = ProductDao.getProductType();
            responseJson.put("status", 0);
            responseJson.put("msg", "查询成功");
            responseJson.put("content", jsonArray);

        } catch (SQLException e) {
            e.printStackTrace();
            responseJson.put("status", 20001);
            responseJson.put("msg", "数据库操作失败");
        }catch (Exception e){
            e.printStackTrace();
            responseJson.put("status", 20000);
            responseJson.put("msg", "未知错误");
        }
        finally {
            PrintWriter printWriter = response.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }

        super.doGet(request, response);
    }
}

package servlet.protectAPI;

import Dao.EvaluateDao;
import bean.Evaluate;
import bean.ProductInfo;
import com.google.gson.Gson;
import net.sf.json.JSONObject;
import util.BaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/protect/user/evaluate")
public class EvaluateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject responseJson = new JSONObject();
        JSONObject dataJson = new JSONObject();
        try {
            JSONObject requestJson = BaseUtil.getJsonObject(req);

            Gson gson = new Gson();
            Evaluate evaluate = gson.fromJson(requestJson.toString(), Evaluate.class);
            int result = EvaluateDao.addEvaluate(evaluate);
            responseJson.put("data", dataJson);
            responseJson.put("status", 0);
            responseJson.put("msg", "评论插入成功");
        }catch (Exception e){
            e.printStackTrace();
            responseJson.put("data", dataJson);
            responseJson.put("status", 1);
            responseJson.put("msg", "评论插入失败 : " + e.toString());
        } finally {
            PrintWriter printWriter = resp.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }

    }
}

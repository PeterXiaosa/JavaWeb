package servlet.certificate;

import Dao.TokenDao;
import Dao.UserDao;
import net.sf.json.JSONObject;
import util.BaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/certificate/regettoken")
public class ReGetTokenServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws IOException{
        JSONObject responseJson = new JSONObject();
        try{
            // 获取Client端数据
            JSONObject requestJson = BaseUtil.getDataFromRequest(request);
            String account = requestJson.getString("account");
            String timeStamp = requestJson.getString("timestamp");
            String nonce = requestJson.getString("nonce");
            String signatureClient = requestJson.getString("signature");

            String genkeyServer = UserDao.getGenKeyFromDB(account);
            String signatureServer = BaseUtil.generateSignature(genkeyServer, timeStamp, nonce);
            do {
                if (! signatureClient.equals(signatureServer)){
                    responseJson.put("status", 10008);
                    responseJson.put("msg", "签名失败");
                    break;
                }else {
                    //  签名成功刷新Accesstoken和时间
                    String accessToken = BaseUtil.createAccessToken();
                    TokenDao.updateAccessTokenInDB(account, accessToken);
                    JSONObject jsonObject = new JSONObject();

                    responseJson.put("status", 0);
                    responseJson.put("msg", "accessToken获取成功");
                    jsonObject.put("accesstoken", accessToken);
                    responseJson.put("content", jsonObject);
                }
            }while (false);

        }catch (Exception e){
            responseJson.put("statuscode", 1);
            responseJson.put("content", "accesstoken获取失败 : " + e.toString());
        }finally {
            PrintWriter printWriter = resp.getWriter();
            printWriter.print(responseJson.toString());
            printWriter.flush();
            printWriter.close();
        }
    }
}

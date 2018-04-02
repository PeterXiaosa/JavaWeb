package servlet;

import bean.NewTotal;
import bean.News;
import com.google.gson.Gson;
import net.sf.json.JSONObject;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/getJson")
public class JsonServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 创建多个新闻类，模拟从数据库获取数据，可将此处改为用JDBC从数据库读取数据
        News new1 = new News(110, "日本地震", "日本福田发生了7级地震", "2016-5-16 10:22:20",
                "http://world.huanqiu.com/exclusive/2016-05/8974294.html");
        News new2 = new News(111, "Apple库克第八次访华", "近日库克第八次访华，与滴滴高层会谈", "2016-5-16 10:22:20",
                "http://mobile.163.com/16/0523/09/BNO7SG2B001168BQ.html");
        News new3 = new News(113, "Google I/O大会开幕", "Google开发者大会即将举办，是否推出Android7.0?", "2016-5-16 10:22:20",
                "http://www.ithome.com/html/android/227647.htm");
        News new4 = new News(114, "格力营收下滑400亿", "格里营收下滑400亿，董明珠说我活得好的很", "2016-5-16 10:22:20",
                "http://news.mydrivers.com/1/484/484072.htm");
        News new5 = new News(115, "日本地震", "日本福田发生了7级地震", "2016-5-16 10:22:20", "www.baidu.com");
        News new6 = new News(116, "日本地震", "日本福田发生了7级地震", "2016-5-16 10:22:20", "www.baidu.com");
        News new7 = new News(117, "日本地震", "日本福田发生了7级地震", "2016-5-16 10:22:20", "www.baidu.com");
        News new8 = new News(118, "日本地震", "日本福田发生了7级地震", "2016-5-16 10:22:20", "www.baidu.com");
        News new9 = new News(119, "日本地震", "日本福田发生了7级地震", "2016-5-16 10:22:20", "www.baidu.com");
        News new10 = new News(120, "日本地震", "日本福田发生了7级地震", "2016-5-16 10:22:20", "www.baidu.com");
        News new11 = new News(121, "获取新数据！！！！！！", "日本福田发生了7级地震", "2016-5-16 10:22:20", "www.baidu.com");
        News new12 = new News(122, "获取新数据！！！！！！", "日本福田发生了7级地震", "2016-5-16 10:22:20", "www.baidu.com");
        News new13 = new News(123, "获取新数据！！！！！！", "日本福田发生了7级地震", "2016-5-16 10:22:20", "www.baidu.com");
        News new14 = new News(124, "获取新数据！！！！！！", "日本福田发生了7级地震", "2016-5-16 10:22:20", "www.baidu.com");
        News new15 = new News(125, "日本地震", "日本福田发生了7级地震", "2016-5-16 10:22:20", "www.baidu.com");
        News new16 = new News(126, "日本地震", "日本福田发生了7级地震", "2016-5-16 10:22:20", "www.baidu.com");
        News new17 = new News(127, "日本地震", "日本福田发生了7级地震", "2016-5-16 10:22:20", "www.baidu.com");
        News new18 = new News(128, "日本地震", "日本福田发生了7级地震", "2016-5-16 10:22:20", "www.baidu.com");
        News new19 = new News(129, "日本地震", "日本福田发生了7级地震", "2016-5-16 10:22:20", "www.baidu.com");
        News new20 = new News(130, "日本地震", "日本福田发生了7级地震", "2016-5-16 10:22:20", "www.baidu.com");

        String page = req.getParameter("page");
        // 将数据添加到数组
        List<News> newslist = new ArrayList<News>();
        if (page == null || page.equals("0")) {
            newslist.add(new1);
            newslist.add(new2);
            newslist.add(new3);
            newslist.add(new4);
            newslist.add(new5);
            newslist.add(new6);
            newslist.add(new7);
            newslist.add(new8);
            newslist.add(new9);
            newslist.add(new10);
        }
        else {
            newslist.add(new11);
            newslist.add(new12);
            newslist.add(new13);
            newslist.add(new14);
            newslist.add(new15);
            newslist.add(new16);
            newslist.add(new17);
            newslist.add(new18);
            newslist.add(new19);
            newslist.add(new20);
        }

        // 将数据封装到新闻总计类
        NewTotal nt = new NewTotal(newslist.size(), newslist);

        // 调用GSON jar工具包封装好的toJson方法，可直接生成JSON字符串
        Gson gson = new Gson();
        String json = gson.toJson(nt);

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

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("account");
                String a = resultSet.getString("password");
            }

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

        // 输出到界面
        System.out.println(json);
        resp.setContentType("application/json;charset=gb2312");
        PrintWriter out = new PrintWriter(resp.getOutputStream());
        out.print(json);
        out.flush();
        // 更多Json转换使用请看JsonTest类
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        this.doGet(req, resp);
        // getParameterMap获取url中的参数
        Map<String, String[]> qc = request.getParameterMap();
        String user = request.getParameter("name");
        String password = request.getParameter("password");
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(), "utf-8"));
        StringBuffer sb = new StringBuffer("");
        String temp;
        while ((temp = br.readLine()) != null) {
            sb.append(temp);
        }
        br.close();
        String params = sb.toString();

        JSONObject jsonObject = JSONObject.fromObject(params);

        response.setContentType("application/x-javascript; charset=utf-8");
        PrintWriter pw = response.getWriter();
        pw.write(jsonObject.toString());
        pw.flush();
        pw.close();
    }

}

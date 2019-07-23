package websocket;

import Dao.UserDao;
import bean.Partner;
import bean.User;
import bean.UserInfo;
import data.ProtectSocketData;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/mywebsocket/{matchcode}/{deviceid}")
public class WebSocketTest {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    // 若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<WebSocketTest> webSocketSet = new CopyOnWriteArraySet<WebSocketTest>();
    private static Map<String, Partner> webSocketPartnerMap = new ConcurrentHashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam(value="matchcode")String matchcode, @PathParam(value = "deviceid")String deviceid, Session session){
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("携带的参数的是 " + matchcode);
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());

        if (ProtectSocketData.getsInstance().isExitMatchCode(matchcode)){
            // 匹配码合法
            ProtectSocketData.getsInstance().saveSocketCode(matchcode);
            Partner partner = webSocketPartnerMap.get(matchcode);
            if (partner == null) {
                // 双人中第一个人首先连接
                partner = new Partner();
                partner.setMatchCode(matchcode);
                User user1 = new User();
                user1.setSession(session);
                user1.setDeviceId(deviceid);
                partner.setUser1(user1);
                webSocketPartnerMap.put(matchcode, partner);
            }else {
                // 双人中第二个人连接
                User user2 = partner.getUser2();
                if (user2 == null){
                    user2 = new User();
                    user2.setSession(session);
                    user2.setDeviceId(deviceid);
                    partner.setUser2(user2);
                    webSocketPartnerMap.put(matchcode, partner);

                    // 至此配对结束，需要将数据存库
                    UserInfo userInfo2 = new UserInfo();
                    userInfo2.setDeviceId(deviceid);
                    User user1 = partner.getUser1();
                    UserInfo userInfo1 = new UserInfo();
                    userInfo1.setDeviceId(user1.getDeviceId());
                    try {
                        UserDao.updateUserMatchcodeByDeviceId(matchcode, user1.getDeviceId());
                        UserDao.updateUserMatchcodeByDeviceId(matchcode, user2.getDeviceId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    User usermale = partner.getUser1();
                    User userfemale = partner.getUser2();
                    try {
                        usermale.getSession().getBasicRemote().sendText("startconnectyourpartner");
                        userfemale.getSession().getBasicRemote().sendText("startconnectyourpartner");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            // 匹配码不合法, 可客户端先检测匹配码是否合法，合法再进行连接。否则容易耗费资源

//            try {
//                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "匹配码不存在"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam(value="matchcode")String matchcode, @PathParam(value = "deviceid")String deviceid){
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(@PathParam(value = "matchcode")String matchCode, String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        //群发消息
//        for(WebSocketTest item: webSocketSet){
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//                continue;
//            }
//        }

        try {
            // 收到一端的信息，需要转发到另外的匹配端
            Partner partner = webSocketPartnerMap.get(matchCode);
            if (partner == null){
                return;
            }
            User user1 = partner.getUser1();
            if (session.getId().equals(user1.getSession().getId())) {
                User user2 = partner.getUser2();
                Session desSession =user2.getSession();
                desSession.getBasicRemote().sendText(message);
            } else {
                Session desSession =user1.getSession();
                desSession.getBasicRemote().sendText(message);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketTest.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketTest.onlineCount--;
    }
}

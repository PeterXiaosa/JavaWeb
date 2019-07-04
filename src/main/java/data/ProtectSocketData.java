package data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class ProtectSocketData {
    // 用来存储生成的随机配对码
    private List<String> matchCodeList = new ArrayList<>();
    // 建立Socket连接使用的配对码
    private List<String> socketMatchCodeList = new ArrayList<>();

    private ProtectSocketData(){}

    public static ProtectSocketData getsInstance() {
        return ProtectSocketDataHolder.sInstance;
    }

    private static class ProtectSocketDataHolder {
        static ProtectSocketData sInstance = new ProtectSocketData();
    }

    public synchronized boolean saveSocketCode(String socketMatchCode){
        if (socketMatchCodeList.contains(socketMatchCode)){
            return false;
        }else {
            return socketMatchCodeList.add(socketMatchCode);
        }
    }

    public synchronized boolean saveMatchCode(String matchCode){
        if (matchCodeList.contains(matchCode)){
            return false;
        }else {
            return matchCodeList.add(matchCode);
        }
    }

    public boolean isExitMatchCode(String matchCode){
        if (matchCodeList.contains(matchCode)){
            return true;
        }else {
            return false;
        }
    }
}

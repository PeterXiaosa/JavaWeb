package data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class ProtectSocketData {
    private List<String> matchCodeList = new ArrayList<>();

    private ProtectSocketData(){}

    public static ProtectSocketData getsInstance() {
        return ProtectSocketDataHolder.sInstance;
    }

    private static class ProtectSocketDataHolder {
        static ProtectSocketData sInstance = new ProtectSocketData();
    }

    public synchronized boolean saveMatchCode(String matchCode){
        if (matchCodeList.contains(matchCode)){
            return false;
        }else {
            return matchCodeList.add(matchCode);
        }
    }
}

package vip.ylove.sdk.util;

public final class StKeyUtil {

    private static final  ThreadLocal<String> keyMap = new ThreadLocal<>();

    public static String getKey(){
        return keyMap.get();
    }

    public static String puKey(String key){
         keyMap.set(key);
         return key;
    }
    public static void remove(){
         keyMap.remove();
    }
}

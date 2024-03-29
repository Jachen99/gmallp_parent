package space.jachen.gmall.activity.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统缓存类
 *
 * @author JaChen
 * @date 2023/3/24 19:53
 */
public class CacheHelper {

    /**
     * 缓存容器
     */
    private final static Map<String, String> cacheMap = new ConcurrentHashMap<>();

    /**
     * 加入缓存
     */
    public static void put(String key, String cacheStr) {
        cacheMap.put(key, cacheStr);
    }

    /**
     * 获取缓存
     */
    public static String get(String key) {
        return cacheMap.get(key);
    }

    /**
     * 清除缓存
     */
    public static void remove(String key) {
        cacheMap.remove(key);
    }

    /**
     * 清空缓存
     */
    public static synchronized void removeAll() {
        cacheMap.clear();
    }
}

package com.anwen.rpc.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import com.anwen.rpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 31/3/2024 下午9:20
 */
@Slf4j
public class SpiLoader {

    /**
     * 存储已加载的类
     */
    private static Map<String, Map<String, Class<?>>> loadmap = new ConcurrentHashMap<>();

    /**
     * 存储对象实例
     */
    private static Map<String, Object> instanceCache = new ConcurrentHashMap<>();

    /**
     * 系统SPi目录
     */
    private static final String RPC_SYS_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用户自定义spi路径
     */
    private static final String RPC_CUS_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * 扫描路径
     */
    private static final String[] SCAN_DIRS = new String[]{RPC_SYS_SPI_DIR, RPC_CUS_SPI_DIR};

    /**
     * 动态加载的类列表
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    /**
     * 加载所有类型
     */
    private static void loadall() {
        log.info("加载索引spi");
        for (Class<?> aclass : LOAD_CLASS_LIST) {
            //todo load
            load(aclass);
        }
    }

    /**
     * 获取某个接口实例
     * @param tclass
     * @param key
     * @return
     * @param <T>
     */
    public static <T> T getInstance(Class<?> tclass, String key) {
        String tclassName = tclass.getName();
        Map<String, Class<?>> classMap = loadmap.get(tclassName);
        if (classMap == null) {
            throw new RuntimeException(String.format("spi未加载%s", tclass));
        }
        if (!classMap.containsKey(key)) {
            throw new RuntimeException(String.format("spi的%s不存在key=%s的类型 ", tclass, key));
        }
        //获取要加载的类
        Class<?> aClass = classMap.get(key);
        //从实例缓存中加载指定类型的实例
        String className = aClass.getName();
        if (!instanceCache.containsKey(className)) {
            try {
                instanceCache.put(className,aClass.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(String.format("%s类加载失败",className));
            }
        }
        Object object = instanceCache.get(className);
        return (T) object;
    }

    /**
     * 加载某个类型
     * @param loadclass
     * @return
     */
    public static  Map<String, Class<?>> load(Class<?> loadclass) {
        log.info("加载类型为{}的spi", loadclass.getName());
        //扫描路径,用户的优先级大于系统
        HashMap<String,Class<?>> keymap = new HashMap<>();
        for (String scanDir : SCAN_DIRS) {
            List<URL> resource = ResourceUtil.getResources(scanDir + loadclass.getName());
            for (URL url : resource) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] arrey = line.split("=");
                        if (arrey.length > 1) {
                            String key = arrey[0];
                            String classname = arrey[1];
                            keymap.put(key, Class.forName(classname));
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        loadmap.put(loadclass.getName(),keymap);
        return keymap;
    }





}

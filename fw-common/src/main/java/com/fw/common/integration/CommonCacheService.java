package com.fw.common.integration;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author khanhtd
 * @Date 2022
 */
@Configuration
@EnableScheduling
@Log4j2
public class CommonCacheService {
    private List<String> lstMethod = new ArrayList<>();

    /**
     * init Method - lấy danh sách method: public + không phải thư viện
     */
    @PostConstruct
    public void init() {
        Method[] methods = CommonCacheService.class.getDeclaredMethods();
        for (Method method : methods) {
            if (!method.isBridge() && !method.isSynthetic() && Modifier.isPublic(method.getModifiers())) {
                lstMethod.add(method.getName());
            }
        }
    }

    private Map<String, String> streamMap = new HashMap<>();

    //Getter
    public Map<String, String> getStreamMap() {
        return loadRunnableByName(streamMap, this::loadStreams);
    }

    //load data
    private void loadStreams() {
        streamMap.clear();
        streamMap.put("", "");
    }

    @Scheduled(fixedRateString = "${cache.expireInSeconds:86400000}")
    private void autoLoad() {
        loadStreams();
    }

    private Map loadRunnableByName(Map<?, ?> map, Runnable function) {
        //Lấy tên method getter trước hàm loadRunnableByName này
        String currentMethod = Thread.currentThread().getStackTrace()[2].getMethodName();

        //Nếu map cache không có dữ liệu và method trước đó vẫn còn trong lst thì run function
        //remove method khỏi lst nếu map có dữ liệu
        try {
            if (!CollectionUtils.isEmpty(map)) {
                lstMethod.remove(currentMethod);
            }
            if (CollectionUtils.isEmpty(map) && lstMethod.contains(currentMethod)) {
                function.run();
            }
            return map;
        } catch (Exception e) {
            log.info(CommonCacheService.class.getName() + ". Method " + currentMethod + " fail with exception: " + e.getMessage());
            return map;
        }
    }
}

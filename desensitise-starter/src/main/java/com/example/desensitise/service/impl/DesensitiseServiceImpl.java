package com.example.desensitise.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.json.JSONUtil;
import com.example.desensitise.config.DesensitiseProperties;
import com.example.desensitise.constant.DesensitiseType;
import com.example.desensitise.model.DesensitiseConfig;
import com.example.desensitise.service.IDesensitiseConfigProvider;
import com.example.desensitise.service.IDesensitiseService;
import com.example.desensitise.util.ExpireTimeMap;
import com.example.desensitise.util.TrieTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DesensitiseServiceImpl implements IDesensitiseService {

    @Autowired
    private IDesensitiseConfigProvider desensitiseConfigProvider;

    @Autowired
    private DesensitiseProperties desensitiseProperties;

    public Object desensitise(List<String> keys, Object obj) {
        TrieTree<String> trieTree = getTrieTree(keys);

        if (trieTree != null && trieTree.getSize() > 0) {
            log.debug("脱敏开始：{}", JSONUtil.toJsonStr(obj));
            obj = desensitiseObj(obj, trieTree.getRoot());
            log.debug("脱敏结束：{}", JSONUtil.toJsonStr(obj));
        }
        return obj;
    }

    private TrieTree<String> getTrieTree(List<String> keys) {
        String mapKey = String.join("&", keys);
        TrieTree trieTree = ExpireTimeMap.get(mapKey, TrieTree.class);
        if (trieTree != null) {
            return trieTree;
        }
        List<DesensitiseConfig> configs = desensitiseConfigProvider.match(keys);
        if (!CollectionUtils.isEmpty(configs)) {
            trieTree = new TrieTree<String>();
            for (DesensitiseConfig config : configs) {
                trieTree.insertAndUpdate(config.getFieldPath(), config.getHandleType());
            }
            ExpireTimeMap.put(mapKey, trieTree, desensitiseProperties.getBufferTimes() * 1000, TimeUnit.MILLISECONDS);
            return trieTree;
        }

        ExpireTimeMap.put(mapKey, new TrieTree<String>(), desensitiseProperties.getBufferTimes() * 1000, TimeUnit.MILLISECONDS);
        return null;
    }

    private Object desensitiseObj(Object obj, TrieTree<String>.Node<String> trieNode) {
        // 深度遍历字段时，更新前缀树，并获取脱敏类型
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object val = field.get(obj);
                TrieTree<String>.Node<String> node = trieNode.getNode(field.getName());
                if (node == null || val == null) {
                    continue;
                }
                if (node.getData() == null) {
                    if (val instanceof Map) {
                        for (Object mapKey : ((Map) val).keySet()) {
                            // 递归
                            desensitiseObj(((Map) val).get(mapKey), node);
                        }
                    } else if (val instanceof Collection) {
                        Iterator iterator = ((Collection) val).iterator();
                        while (iterator.hasNext()) {
                            Object o = iterator.next();
                            // 递归
                            desensitiseObj(o, node);
                        }
                    } else {
                        // 递归
                        desensitiseObj(val, node);
                    }
                } else {
                    // 根据脱敏类型，更新字段值
                    Object fieldVal = handleCollection(val, node.getData());
                    field.set(obj, fieldVal);
                }

            }
        } catch (IllegalAccessException e) {
            log.error("脱敏失败数据：{} {}", JSONUtil.toJsonStr(obj), JSONUtil.toJsonStr(trieNode));
            log.error("脱敏失败报错：{}", e.getMessage());
        }

        return obj;
    }

    private Object handleCollection(Object val, String data) {
        if (val instanceof List) {
            List originList = (List) val;
            ArrayList newList = new ArrayList(originList.size());
            for (Object origin : originList) {
                newList.add(handleDetail(origin, data));
            }
            return newList;
        } else if (val instanceof Set) {
            Set originSet = (Set) val;
            Set newSet = new HashSet(originSet.size());
            for (Object origin : originSet) {
                newSet.add(handleDetail(origin, data));
            }
            return newSet;
        } else if (val instanceof Map) {
            Map originMap = (Map) val;
            Map newMap = new HashMap(originMap.size());
            for (Object key : originMap.keySet()) {
                newMap.put(key, handleDetail(originMap.get(key), data));
            }
            return newMap;
        } else {
            return handleDetail(val, data);
        }
    }

    private Object handleDetail(Object obj, String data) {
        log.debug("脱敏数据：{}", obj);
        DesensitizedUtil.DesensitizedType desensitizedType = DesensitiseType.desensitiseTypeMap.get(data);

        if (desensitizedType != null) {
            if (obj instanceof Character || obj instanceof String) {
                return DesensitizedUtil.desensitized(String.valueOf(obj), DesensitiseType.desensitiseTypeMap.get(data));
            }
        }

        if ("number".equals(data)) {
            if (obj instanceof Integer) {
                return 0;
            } else if (obj instanceof Long) {
                return 0L;
            } else if (obj instanceof Double) {
                return 0.0;
            } else if (obj instanceof Float) {
                return 0.0f;
            } else if (obj instanceof Short) {
                return 0;
            } else if (obj instanceof Byte) {
                return 0;
            }
        }

        log.warn("脱敏失败，未知脱敏类型：{} {}", obj, data);
        return obj;
    }
}

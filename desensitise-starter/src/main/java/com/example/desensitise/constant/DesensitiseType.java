package com.example.desensitise.constant;

import cn.hutool.core.util.DesensitizedUtil;

import java.util.HashMap;
import java.util.Map;

public class DesensitiseType {

    public static final Map<String, DesensitizedUtil.DesensitizedType> desensitiseTypeMap = new HashMap<String, DesensitizedUtil.DesensitizedType>();

    static {
        desensitiseTypeMap.put("用户id", DesensitizedUtil.DesensitizedType.USER_ID);
        desensitiseTypeMap.put("中文名", DesensitizedUtil.DesensitizedType.CHINESE_NAME);
        desensitiseTypeMap.put("身份证号", DesensitizedUtil.DesensitizedType.ID_CARD);
        desensitiseTypeMap.put("座机号", DesensitizedUtil.DesensitizedType.FIXED_PHONE);
        desensitiseTypeMap.put("手机号", DesensitizedUtil.DesensitizedType.MOBILE_PHONE);
        desensitiseTypeMap.put("地址", DesensitizedUtil.DesensitizedType.ADDRESS);
        desensitiseTypeMap.put("电子邮件", DesensitizedUtil.DesensitizedType.EMAIL);
        desensitiseTypeMap.put("密码", DesensitizedUtil.DesensitizedType.PASSWORD);
        desensitiseTypeMap.put("中国大陆车牌", DesensitizedUtil.DesensitizedType.CAR_LICENSE);
        desensitiseTypeMap.put("银行卡", DesensitizedUtil.DesensitizedType.BANK_CARD);
        desensitiseTypeMap.put("IPv4地址", DesensitizedUtil.DesensitizedType.IPV4);
        desensitiseTypeMap.put("IPv6地址", DesensitizedUtil.DesensitizedType.IPV6);
        desensitiseTypeMap.put("只显示第一个字符", DesensitizedUtil.DesensitizedType.FIRST_MASK);
        desensitiseTypeMap.put("清空为null", DesensitizedUtil.DesensitizedType.CLEAR_TO_NULL);
        desensitiseTypeMap.put("清空为\"\"", DesensitizedUtil.DesensitizedType.CLEAR_TO_EMPTY);
    }

}

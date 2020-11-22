package com.android.libnetwork;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Type;

/**
 * 默认的Json转 Java Bean的转换器
 */
public class JsonConvert implements Convert {

    @Override
    public Object convert(String response, Type type) {
        JSONObject jsonObject = JSON.parseObject(response);
        JSONObject data = jsonObject.getJSONObject("data");
        if (data != null) {
            Object object = data.get("data");
            if (object != null) {
                return JSON.parseObject(object.toString(), type);
            }
        }
        return null;
    }

    @Override
    public Object convert(String response, Class clazz) {
        JSONObject jsonObject = JSON.parseObject(response);
        JSONObject data = jsonObject.getJSONObject("data");
        if (data != null) {
            Object object = data.get("data");
            if (object != null) {
                return JSON.parseObject(object.toString(), clazz);
            }
        }
        return null;
    }
}

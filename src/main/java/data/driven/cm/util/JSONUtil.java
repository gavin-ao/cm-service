package data.driven.cm.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Collection;

/**
 * json工具类
 * @author hejinkai
 * @date 2018/6/15
 */
public class JSONUtil {

    /**
     * 设置常规信息返回JSONObject
     * @param success 成功标志
     * @param code  状态码
     * @param msg   信息
     * @return
     */
    public static JSONObject putMsg(boolean success, String code, String msg){
        JSONObject result = new JSONObject();
        result.put("success", success);
        result.put("code", code);
        result.put("msg", msg);
        return result;
    }

    /**
     * 利用json去除为null的属性
     * @param obj
     * @return
     */
    public static Object replaceNull(Object obj){
        if(obj != null){
            if (obj instanceof Collection){
                return JSON.parseArray(JSON.toJSONString(obj));
            }else {
                return JSON.parseObject(JSON.toJSONString(obj));
            }
        }
        return obj;
    }
}

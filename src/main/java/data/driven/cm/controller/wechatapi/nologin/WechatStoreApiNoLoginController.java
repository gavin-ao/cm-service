package data.driven.cm.controller.wechatapi.nologin;

import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.system.StoreService;
import data.driven.cm.business.wechat.WechatAppInfoService;
import data.driven.cm.controller.wechatapi.WechatStoreApiController;
import data.driven.cm.entity.system.StoreEntity;
import data.driven.cm.entity.wechat.WechatAppInfoEntity;
import data.driven.cm.util.JSONUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author hejinkai
 * @date 2018/11/9
 */
@Controller
@RequestMapping(path = "/wechatapi/nologin/store")
public class WechatStoreApiNoLoginController {
    private static final Logger logger = Logger.getLogger(WechatStoreApiController.class);

    @Autowired
    private StoreService storeService;
    @Autowired
    private WechatAppInfoService wechatAppInfoService;

    @ResponseBody
    @RequestMapping("/findStoreTopList")
    public JSONObject findStoreTopList(String keyword, String appid){
        if(appid == null){
            return JSONUtil.putMsg(false, "101", "小程序为空");
        }
        WechatAppInfoEntity wechatAppInfoEntity = wechatAppInfoService.getAppInfo(appid);
        if(wechatAppInfoEntity == null){
            return JSONUtil.putMsg(false, "102", "小程序为空");
        }
        JSONObject result = new JSONObject();
        List<StoreEntity> storeList = storeService.findStoreTopList(keyword, wechatAppInfoEntity.getAppInfoId());
        result.put("success", true);
        result.put("storeList", storeList);
        return result;
    }

}

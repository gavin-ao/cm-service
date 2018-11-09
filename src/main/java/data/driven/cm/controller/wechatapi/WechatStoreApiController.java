package data.driven.cm.controller.wechatapi;

import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.system.StoreService;
import data.driven.cm.common.WechatApiSession;
import data.driven.cm.common.WechatApiSessionBean;
import data.driven.cm.entity.system.StoreEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 门店小程序调用接口
 * @author hejinkai
 * @date 2018/11/9
 */
@Controller
@RequestMapping(path = "/wechatapi/store")
public class WechatStoreApiController {

    private static final Logger logger = Logger.getLogger(WechatStoreApiController.class);

    @Autowired
    private StoreService storeService;


    @ResponseBody
    @RequestMapping("/findStoreTopList")
    public JSONObject findStoreTopList(String sessionID, String keyword){
        WechatApiSessionBean wechatApiSessionBean = WechatApiSession.getSessionBean(sessionID);
        JSONObject result = new JSONObject();
        List<StoreEntity> storeList = storeService.findStoreTopList(keyword, wechatApiSessionBean.getUserInfo().getAppInfoId());
        result.put("success", true);
        result.put("storeList", storeList);
        return result;
    }



}

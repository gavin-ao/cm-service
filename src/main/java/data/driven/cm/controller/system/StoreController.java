package data.driven.cm.controller.system;

import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.system.StoreService;
import data.driven.cm.business.user.UserInfoService;
import data.driven.cm.common.ApplicationSessionFactory;
import data.driven.cm.common.Constant;
import data.driven.cm.component.Page;
import data.driven.cm.component.PageBean;
import data.driven.cm.entity.system.StoreEntity;
import data.driven.cm.entity.user.UserInfoEntity;
import data.driven.cm.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hejinkai
 * @date 2018/11/5
 */
@Controller
@RequestMapping(path = "/system/store")
public class StoreController {

    private static final Logger logger = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    private StoreService storeService;
    @Autowired
    private UserInfoService userInfoService;

    @ResponseBody
    @RequestMapping(path = "/findStorePage")
    public JSONObject findStorePage(String keyword, String appInfoId, Integer pageNo, Integer pageSize){
        JSONObject result = new JSONObject();
        PageBean pageBean = new PageBean();
        if(pageNo == null){
            pageNo = 1;
        }
        if(pageSize == null){
            pageSize = 10;
        }
        pageBean.setPageNo(pageNo);
        pageBean.setPageSize(pageSize);

        Page<StoreEntity> page = storeService.findStorePage(keyword, appInfoId, pageBean);
        if(page != null && page.getResult() != null && page.getResult().size() > 0){
            List<StoreEntity> storeList = page.getResult();
            List<String> managerList = storeList.stream().filter(storeEntity -> storeEntity.getManager() != null).collect(Collectors.mapping(o -> o.getManager(), Collectors.toList()));
            if(managerList != null && managerList.size() > 0){
                Map<String, String> userMap = userInfoService.findUserNameMapByUserId(managerList);
                if(userMap != null && userMap.size() > 0){
                    for(StoreEntity storeEntity : storeList){
                        storeEntity.setManager(userMap.get(storeEntity.getManager()));
                    }
                }
            }
        }
        result.put("success", true);
        result.put("page", page);
        return result;
    }

    @ResponseBody
    @RequestMapping(path = "/getStoreById")
    public JSONObject getStoreById(String storeId){
        JSONObject result = new JSONObject();
        StoreEntity store = storeService.getStoreById(storeId);
        result.put("success", true);
        result.put("store", store);
        return result;
    }

    @ResponseBody
    @RequestMapping(path = "/getStoreQrCode")
    public JSONObject getStoreQrCode(HttpServletRequest request, HttpServletResponse response, String storeId){
        JSONObject result = new JSONObject();
        if(storeId == null){
            UserInfoEntity user = ApplicationSessionFactory.getUser(request, response);
            storeId = storeService.getStoreIdByCurrentUser(user.getUserId());
        }
        String filePath = storeService.getStoreQrCode(storeId);
        if(filePath != null){
            filePath = Constant.STATIC_FILE_PATH + filePath;
        }
        result.put("success", true);
        result.put("filePath", filePath);
        return result;
    }



    @ResponseBody
    @RequestMapping(path = "/addStore")
    public JSONObject addStore(HttpServletRequest request, HttpServletResponse response, StoreEntity storeEntity, String managerName, String pwd){
        UserInfoEntity user = ApplicationSessionFactory.getUser(request, response);
        JSONObject result = new JSONObject();
        if(storeEntity != null){
            storeEntity.setUserId(user.getUserId());
        }
        return storeService.addStore(storeEntity, managerName, pwd);
    }

    @ResponseBody
    @RequestMapping(path = "/updateStore")
    public JSONObject updateStore(StoreEntity storeEntity){
        return storeService.updateStore(storeEntity);
    }

    @ResponseBody
    @RequestMapping(path = "/updateStoreManager")
    public JSONObject updateStoreManager(String storeId, String managerName, String pwd){
        return storeService.updateStoreManager(storeId, managerName, pwd);
    }

    @ResponseBody
    @RequestMapping(path = "/updateStoreManagerPwd")
    public JSONObject updateStoreManagerPwd(String storeId, String pwd){
        return storeService.updateStoreManagerPwd(storeId, pwd);
    }

    @ResponseBody
    @RequestMapping(path = "/getStoreId")
    public JSONObject getStoreId(HttpServletRequest request, HttpServletResponse response){
        UserInfoEntity user = ApplicationSessionFactory.getUser(request, response);
        String storeId = storeService.getStoreIdByCurrentUser(user.getUserId());
        JSONObject result = JSONUtil.putMsg(true, "200", "操作成功");
        result.put("storeId", storeId);
        return result;
    }

}

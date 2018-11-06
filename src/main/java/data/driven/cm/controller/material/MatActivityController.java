package data.driven.cm.controller.material;

import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.material.MatActivityService;
import data.driven.cm.business.system.StoreService;
import data.driven.cm.common.ApplicationSessionFactory;
import data.driven.cm.component.Page;
import data.driven.cm.component.PageBean;
import data.driven.cm.entity.system.StoreEntity;
import data.driven.cm.entity.user.UserInfoEntity;
import data.driven.cm.util.JSONUtil;
import data.driven.cm.vo.material.MatActivityVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 活动controller
 * @author hejinkai
 * @date 2018/11/7
 */
@Controller
@RequestMapping(path = "/mat/activity")
public class MatActivityController {

    private static final Logger logger = LoggerFactory.getLogger(MatActivityController.class);

    @Autowired
    private MatActivityService matActivityService;
    @Autowired
    private StoreService storeService;

    @ResponseBody
    @RequestMapping(path = "findActivityPage")
    public JSONObject findActivityPage(HttpServletRequest request, HttpServletResponse response, String keyword, Integer pageNo, Integer pageSize){
        UserInfoEntity user = ApplicationSessionFactory.getUser(request, response);
        String storeId = storeService.getStoreIdByCurrentUser(user.getUserId());
        if(storeId == null){
            return JSONUtil.putMsg(false, "109", "不是门店管理员");
        }
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

        Page<MatActivityVO> page = matActivityService.findActivityPage(keyword, storeId, pageBean);
        result.put("success", true);
        result.put("page", page);
        return result;
    }

    @ResponseBody
    @RequestMapping(path = "updateActivity")
    public JSONObject updateActivity(HttpServletRequest request, HttpServletResponse response, MatActivityVO activity, String btnCopywritingJson){
        UserInfoEntity user = ApplicationSessionFactory.getUser(request, response);
        String storeId = storeService.getStoreIdByCurrentUser(user.getUserId());
        if(storeId == null){
            return JSONUtil.putMsg(false, "109", "不是门店管理员");
        }
        activity.setStoreId(storeId);
        StoreEntity storeEntity = storeService.getStoreById(storeId);
        if(storeEntity != null){
            activity.setAppInfoId(storeEntity.getAppInfoId());
        }
        return matActivityService.updateActivity(activity, btnCopywritingJson, user.getUserId());
    }

    @ResponseBody
    @RequestMapping(path = "getNextActivityStartDate")
    public JSONObject getNextActivityStartDate(HttpServletRequest request, HttpServletResponse response){
        UserInfoEntity user = ApplicationSessionFactory.getUser(request, response);
        String storeId = storeService.getStoreIdByCurrentUser(user.getUserId());
        if(storeId == null){
            return JSONUtil.putMsg(false, "109", "不是门店管理员");
        }
        return matActivityService.getNextActivityStartDate(storeId);
    }

}

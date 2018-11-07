package data.driven.cm.controller.reward;

import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.material.MatActivityService;
import data.driven.cm.business.reward.RewardActCommandService;
import data.driven.cm.business.system.StoreService;
import data.driven.cm.common.ApplicationSessionFactory;
import data.driven.cm.component.Page;
import data.driven.cm.component.PageBean;
import data.driven.cm.entity.reward.RewardActCommandEntity;
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
import java.util.Date;

/**
 * @author hejinkai
 * @date 2018/11/7
 */
@Controller
@RequestMapping(path = "/reward/command")
public class RewardActCommandController {

    private static final Logger logger = LoggerFactory.getLogger(RewardActContentController.class);

    @Autowired
    private RewardActCommandService rewardActCommandService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private MatActivityService matActivityService;

    @ResponseBody
    @RequestMapping(path = "/findRewardActCommandPage")
    public JSONObject findRewardActCommandPage(HttpServletRequest request, HttpServletResponse response, String actId, Integer commandType, Integer pageNo, Integer pageSize){
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

        Page<RewardActCommandEntity> page = rewardActCommandService.findRewardActCommandPage(actId, commandType, storeId, pageBean);
        result.put("success", true);
        result.put("page", page);
        return result;
    }

    @ResponseBody
    @RequestMapping(path = "/insertRewardActCommand")
    public JSONObject insertRewardActCommand(HttpServletRequest request, HttpServletResponse response, String actId, Integer rewardNum, Integer commandType){
        UserInfoEntity user = ApplicationSessionFactory.getUser(request, response);
        String storeId = storeService.getStoreIdByCurrentUser(user.getUserId());
        if(storeId == null){
            return JSONUtil.putMsg(false, "109", "不是门店管理员");
        }
        MatActivityVO matActivity = matActivityService.getMatActivityAnyID(actId);
        Date date = new Date();
        if(matActivity.getEndAt().after(date)){
            rewardActCommandService.insertRewardActCommand(rewardNum, matActivity, commandType);
            return JSONUtil.putMsg(true, "200", "操作成功");
        }else{
            return JSONUtil.putMsg(false, "101", "结束的活动不能追加奖励");
        }
    }

    @ResponseBody
    @RequestMapping(path = "/deleteRewardActCommand")
    public JSONObject deleteRewardActCommand(HttpServletRequest request, HttpServletResponse response, String actId, Integer commandType, String ids){
        UserInfoEntity user = ApplicationSessionFactory.getUser(request, response);
        String storeId = storeService.getStoreIdByCurrentUser(user.getUserId());
        if(storeId == null){
            return JSONUtil.putMsg(false, "109", "不是门店管理员");
        }
        MatActivityVO matActivity = matActivityService.getMatActivityAnyID(actId);
        Date date = new Date();
        if(matActivity.getStartAt().before(date)){
            return JSONUtil.putMsg(false, "102", "进行中的活动不能删除奖励");
        }
        if(matActivity.getEndAt().after(date)){
            rewardActCommandService.deleteRewardActCommand(actId, commandType, storeId, ids);
            return JSONUtil.putMsg(true, "200", "操作成功");
        }else{
            return JSONUtil.putMsg(false, "101", "结束的活动不能删除奖励");
        }
    }

}

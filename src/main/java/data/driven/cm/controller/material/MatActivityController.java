package data.driven.cm.controller.material;

import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.material.BtnCopywritingService;
import data.driven.cm.business.material.MatActivityService;
import data.driven.cm.business.reward.RewardActContentService;
import data.driven.cm.business.reward.RewardActCurrencyService;
import data.driven.cm.business.reward.RewardActCustMsgService;
import data.driven.cm.business.system.PictureService;
import data.driven.cm.business.system.StoreService;
import data.driven.cm.common.ApplicationSessionFactory;
import data.driven.cm.common.Constant;
import data.driven.cm.component.Page;
import data.driven.cm.component.PageBean;
import data.driven.cm.entity.reward.RewardActContentEntity;
import data.driven.cm.entity.reward.RewardActCurrencyEntity;
import data.driven.cm.entity.reward.RewardActCustMsgEntity;
import data.driven.cm.entity.system.StoreEntity;
import data.driven.cm.entity.user.UserInfoEntity;
import data.driven.cm.util.DateFormatUtil;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private BtnCopywritingService btnCopywritingService;
    @Autowired
    private RewardActContentService rewardActContentService;
    @Autowired
    private RewardActCurrencyService rewardActCurrencyService;
    @Autowired
    private RewardActCustMsgService rewardActCustMsgService;
    @Autowired
    private PictureService pictureService;

    @ResponseBody
    @RequestMapping(path = "/findActivityPage")
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
        if(page != null && page.getResult() != null && page.getResult().size() > 0){
            Date date = DateFormatUtil.convertDate(new Date());
            //判断活动状态
            for (MatActivityVO matActivityVO : page.getResult()){
                if(matActivityVO.getStartAt() == null || matActivityVO.getEndAt() == null){
                    continue;
                }
                int stats = 0;
                if(matActivityVO.getStartAt().after(date)){
                    stats = 0;
                }else{
                    if(matActivityVO.getEndAt().before(date)){
                        stats = 2;
                    }else{
                        stats = 1;
                    }
                }
                matActivityVO.setStatus(stats);
            }
        }

        result.put("success", true);
        result.put("page", page);
        return result;
    }

    @ResponseBody
    @RequestMapping(path = "/updateActivity")
    public JSONObject updateActivity(HttpServletRequest request, HttpServletResponse response, MatActivityVO activity, String btnCopywritingJson, String rewardActContentJson, String initiatorRewardJson, String assistanceRewardJson, Integer rewardNum, Integer assistanceAwardsNum){
        UserInfoEntity user = ApplicationSessionFactory.getUser(request, response);
        String storeId = storeService.getStoreIdByCurrentUser(user.getUserId());
        if(storeId == null){
            return JSONUtil.putMsg(false, "109", "不是门店管理员");
        }
        activity.setStoreId(storeId);
        StoreEntity storeEntity = storeService.getStoreById(storeId);
        if(storeEntity != null){
            activity.setAppInfoId(storeEntity.getAppInfoId());
        }else{
            return JSONUtil.putMsg(false, "111", "门店为空");
        }
        return matActivityService.updateActivity(activity, btnCopywritingJson, rewardActContentJson, initiatorRewardJson, assistanceRewardJson, rewardNum, assistanceAwardsNum, user.getUserId());
    }

    @ResponseBody
    @RequestMapping(path = "/getNextActivityStartDate")
    public JSONObject getNextActivityStartDate(HttpServletRequest request, HttpServletResponse response){
        UserInfoEntity user = ApplicationSessionFactory.getUser(request, response);
        String storeId = storeService.getStoreIdByCurrentUser(user.getUserId());
        if(storeId == null){
            return JSONUtil.putMsg(false, "109", "不是门店管理员");
        }
        return matActivityService.getNextActivityStartDate(storeId);
    }

    @ResponseBody
    @RequestMapping(path = "/getMatActivityAllInfo")
    public JSONObject getMatActivityAllInfo(HttpServletRequest request, HttpServletResponse response, String actId){
        UserInfoEntity user = ApplicationSessionFactory.getUser(request, response);
        String storeId = storeService.getStoreIdByCurrentUser(user.getUserId());
        if(storeId == null){
            return JSONUtil.putMsg(false, "109", "不是门店管理员");
        }
        MatActivityVO matActivityVO = matActivityService.getMatActivityAllInfo(actId, storeId);
        if(matActivityVO == null){
            return JSONUtil.putMsg(false, "101", "活动不存在");
        }
        matActivityVO.setFilePath(Constant.STATIC_FILE_PATH + matActivityVO.getFilePath());
        JSONObject result = JSONUtil.putMsg(true, "200", "操作成功");
        result.put("matActivity", matActivityVO);

        Map<String, String> btnMap = btnCopywritingService.findBtnCopyWritingMapByActId(actId);
        result.put("btnMap", btnMap);

        List<RewardActContentEntity> rewardList = rewardActContentService.findRewardActContentList(actId);
        result.put("rewardList", rewardList);

        if(matActivityVO.getInitiatorRewardType() != null){
            if(matActivityVO.getInitiatorRewardType().intValue() == 2){
                RewardActCurrencyEntity currencyEntity = rewardActCurrencyService.getRewardActCurrency(actId, 1);
                result.put("initiatorReward", currencyEntity);
            }else if(matActivityVO.getInitiatorRewardType().intValue() == 3){
                RewardActCustMsgEntity rewardActCustMsgEntity = rewardActCustMsgService.getRewardActCustMsg(actId, 1);
                if(rewardActCustMsgEntity.getType().intValue() == 2){
                    String pictureIds = rewardActCustMsgEntity.getContext();
                    List<String> filePath = pictureService.findPictureByIds(Arrays.asList(pictureIds.split(",")));
                    if(filePath != null && filePath.size() > 0){
                        filePath = filePath.stream().collect(Collectors.mapping(o -> Constant.STATIC_FILE_PATH + o,Collectors.toList()));
                        result.put("initiatorRewardPicture", filePath);
                    }
                }
                result.put("initiatorReward", rewardActCustMsgEntity);
            }
        }

        if(matActivityVO.getAssistanceRewardType() != null){
            if(matActivityVO.getAssistanceRewardType().intValue() == 2){
                RewardActCurrencyEntity currencyEntity = rewardActCurrencyService.getRewardActCurrency(actId, 2);
                result.put("assistanceReward", currencyEntity);
            }else if(matActivityVO.getAssistanceRewardType().intValue() == 3){
                RewardActCustMsgEntity rewardActCustMsgEntity = rewardActCustMsgService.getRewardActCustMsg(actId, 2);
                if(rewardActCustMsgEntity.getType().intValue() == 2){
                    String pictureIds = rewardActCustMsgEntity.getContext();
                    List<String> filePath = pictureService.findPictureByIds(Arrays.asList(pictureIds.split(",")));
                    if(filePath != null && filePath.size() > 0){
                        filePath = filePath.stream().collect(Collectors.mapping(o -> Constant.STATIC_FILE_PATH + o,Collectors.toList()));
                        result.put("assistanceRewardPicture", filePath);
                    }
                }
                result.put("assistanceReward", rewardActCustMsgEntity);
            }
        }


        return result;
    }

}

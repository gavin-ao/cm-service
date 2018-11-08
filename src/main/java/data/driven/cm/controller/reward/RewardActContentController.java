package data.driven.cm.controller.reward;

import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.reward.RewardActContentService;
import data.driven.cm.component.Page;
import data.driven.cm.component.PageBean;
import data.driven.cm.util.DateFormatUtil;
import data.driven.cm.vo.reward.RewardActContentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author hejinkai
 * @date 2018/11/7
 */
@Controller
@RequestMapping(path = "/reward/content")
public class RewardActContentController {

    private static final Logger logger = LoggerFactory.getLogger(RewardActContentController.class);

    @Autowired
    private RewardActContentService rewardActContentService;

    @ResponseBody
    @RequestMapping(path = "/findRewardActContentPage")
    public JSONObject getRewardActContentPage(String keyword, String storeId, Integer pageNo, Integer pageSize){
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
        Page<RewardActContentVO> page = rewardActContentService.findRewardActContentPage(keyword, storeId, pageBean);
        if(page != null && page.getResult() != null && page.getResult().size() > 0){
            Date date = DateFormatUtil.convertDate(new Date());
            //判断活动状态
            for (RewardActContentVO rewardActContentVO : page.getResult()){
                if(rewardActContentVO.getStartAt() == null || rewardActContentVO.getEndAt() == null){
                    continue;
                }
                int stats = 0;
                if(rewardActContentVO.getStartAt().after(date)){
                    stats = 0;
                }else{
                    if(rewardActContentVO.getEndAt().before(date)){
                        stats = 2;
                    }else{
                        stats = 1;
                    }
                }
                rewardActContentVO.setStatus(stats);
            }
        }
        result.put("success", true);
        result.put("page", page);
        return result;
    }

}

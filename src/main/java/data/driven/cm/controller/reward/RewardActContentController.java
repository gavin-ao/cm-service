package data.driven.cm.controller.reward;

import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.reward.RewardActContentService;
import data.driven.cm.component.Page;
import data.driven.cm.component.PageBean;
import data.driven.cm.vo.reward.RewardActContentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        result.put("success", true);
        result.put("page", page);
        return result;
    }

}

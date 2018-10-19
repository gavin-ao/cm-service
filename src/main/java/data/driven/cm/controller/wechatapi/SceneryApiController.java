package data.driven.cm.controller.wechatapi;

import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.tourism.SceneryService;
import data.driven.cm.business.tourism.ScenicSpotService;
import data.driven.cm.entity.tourism.ScenicSpotEntity;
import data.driven.cm.util.RequestUtil;
import data.driven.cm.vo.tourism.SceneryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 何晋凯
 * @date 2018/06/06
 */
@Controller
@RequestMapping(path = "/wechatapi/scenery")
public class SceneryApiController {

    private static final Logger logger = LoggerFactory.getLogger(SceneryApiController.class);

    @Autowired
    private SceneryService sceneryService;
    @Autowired
    private ScenicSpotService scenicSpotService;

    @RequestMapping(path = "/findSceneryList")
    public ModelAndView findSceneryList(String scenicSpotId){
        ModelAndView modelAndView = new ModelAndView("/tourism/scenery-list");
        ScenicSpotEntity scenicSpot = scenicSpotService.getScenicSpot(scenicSpotId);
        List<SceneryVO> sceneryList = sceneryService.findSceneryList(scenicSpotId);
        modelAndView.addObject("scenicSpot", scenicSpot);
        modelAndView.addObject("sceneryList", sceneryList);
        modelAndView.addObject("pathHead", "/static/file/");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(path = "/findSceneryListJson")
    public JSONObject findSceneryListJson(HttpServletRequest request, String scenicSpotId){
        JSONObject result = new JSONObject();
        ScenicSpotEntity scenicSpot = scenicSpotService.getScenicSpot(scenicSpotId);
        if(scenicSpot != null){
            List<SceneryVO> sceneryList = sceneryService.findSceneryList(scenicSpotId);
            if(sceneryList == null || sceneryList.size() < 1){
                result.put("success", false);
                result.put("code", "101");
                result.put("msg", "没有找到景观数据");
            }else{

                String pathHead = RequestUtil.getStaticFilePath(request);
                List<JSONObject> intrctList = new ArrayList<JSONObject>(sceneryList.size());
                for(SceneryVO sceneryVO : sceneryList){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("sceneryName", sceneryVO.getSceneryName());
                    jsonObject.put("text", sceneryVO.getSceneryIntroduce());
                    String imgUrl = pathHead;
//                    if(!sceneryVO.getFilePath().startsWith("/")){
//                        imgUrl += "/";
//                    }
                    if(sceneryVO.getFilePath() == null){
                        continue;
                    }
                    jsonObject.put("imgUrl", imgUrl+sceneryVO.getFilePath());
                    jsonObject.put("ord", sceneryVO.getOrd());
                    intrctList.add(jsonObject);
                }
                result.put("success", true);
                result.put("intrctList", intrctList);
                result.put("scenicSpot", scenicSpot.getScenicSpotName());
            }
        }else{
            result.put("success", false);
            result.put("code", "102");
            result.put("msg", "没有找到景区数据");
        }

        return result;
    }

}

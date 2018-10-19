package data.driven.cm.controller.wechatapi;

import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.tourism.TravelsPictureTemplateService;
import data.driven.cm.business.tourism.TravelsTextTemplateService;
import data.driven.cm.common.Constant;
import data.driven.cm.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 游记模板
 * @author 何晋凯
 * @date 2018/06/07
 */
@Controller
@RequestMapping(path = "/wechatapi/travels-template")
public class TravelsTemplateApiController {
    private static final Logger logger = LoggerFactory.getLogger(TravelsTemplateApiController.class);

    @Autowired
    private TravelsTextTemplateService travelsTextTemplateService;
    @Autowired
    private TravelsPictureTemplateService travelsPictureTemplateService;

    /**
     * 获取游记模板，文字模板和图片模板
     * @param request
     * @param scenicSpotId
     * @return
     */
    @ResponseBody
    @RequestMapping("/findTravelsTemplates")
    public JSONObject findTravelsTemplates(HttpServletRequest request, String scenicSpotId){
        JSONObject result = new JSONObject();
        List<String> textTemplateList = travelsTextTemplateService.findTravelsTextTemplateList(scenicSpotId);
        if(textTemplateList != null){
            result.put("words", textTemplateList);
        }

        List<String> pictureTemplateList = travelsPictureTemplateService.findTravelsPictureTemplateList(scenicSpotId);
        if(pictureTemplateList != null && pictureTemplateList.size() > 0){
            String pathHead = RequestUtil.getStaticFilePath(request);
            List<JSONObject> boards = new ArrayList<JSONObject>(pictureTemplateList.size());
            for(String picturePath : pictureTemplateList){
                JSONObject pictJson = new JSONObject();
//                String imgUrl = pathHead;
//                if(!picturePath.startsWith("/")){
//                    imgUrl += "/";
//                }
                picturePath = Constant.STATIC_FILE_PATH + picturePath;
                pictJson.put("key", picturePath);
                boards.add(pictJson);
            }
            result.put("boards", boards);
        }



        result.put("success", true);
        return result;
    }

}

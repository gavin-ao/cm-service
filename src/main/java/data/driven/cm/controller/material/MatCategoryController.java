package data.driven.cm.controller.material;

import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.material.MatCategoryService;
import data.driven.cm.controller.wechatapi.ScenicSpotApiController;
import data.driven.cm.entity.material.MatCategoryEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author hejinkai
 * @date 2018/7/13
 */
@Controller
@RequestMapping(path = "/material/category")
public class MatCategoryController {

    private static final Logger logger = LoggerFactory.getLogger(ScenicSpotApiController.class);

    @Autowired
    private MatCategoryService matCategoryService;

    @ResponseBody
    @RequestMapping(path = "/getMatCategory")
    public JSONObject getMatCategory(String catgId){
        JSONObject result = new JSONObject();
        MatCategoryEntity matCategory = matCategoryService.getMatCategoryNoUser(catgId);
        if(matCategory != null){
            result.put("success", true);
            result.put("msg", matCategory.getCatgName());
        }else{
            result.put("success", false);
            result.put("msg", "查询的分类信息为空");
        }
        return result;
    }

}

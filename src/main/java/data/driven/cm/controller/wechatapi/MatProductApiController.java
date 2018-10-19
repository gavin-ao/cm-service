package data.driven.cm.controller.wechatapi;

import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.material.MatCategoryService;
import data.driven.cm.business.material.MatProductService;
import data.driven.cm.entity.material.MatCategoryEntity;
import data.driven.cm.util.RequestUtil;
import data.driven.cm.vo.material.MatProductVO;
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
 * @author hejinkai
 * @date 2018/7/13
 */
@Controller
@RequestMapping(path = "/wechatapi/product")
public class MatProductApiController {

    private static final Logger logger = LoggerFactory.getLogger(MatProductApiController.class);

    @Autowired
    private MatCategoryService matCategoryService;
    @Autowired
    private MatProductService matProductService;

    @ResponseBody
    @RequestMapping(path = "/findMatProductListByCatgJson")
    public JSONObject findMatProductListByCatgJson(HttpServletRequest request, String catgId){
        JSONObject result = new JSONObject();
        MatCategoryEntity matCategory = matCategoryService.getMatCategoryNoUser(catgId);
        if(matCategory != null){
            List<MatProductVO> matProductVOList = matProductService.findMatProductListByCatgLevelCode(matCategory.getLevelCode());
            if(matProductVOList == null || matProductVOList.size() < 1){
                result.put("success", false);
                result.put("code", "101");
                result.put("msg", "没有找到产品数据");
            }else{
                String pathHead = RequestUtil.getStaticFilePath(request);
                List<JSONObject> intrctList = new ArrayList<JSONObject>(matProductVOList.size());
                int i = 1;
                for(MatProductVO matProductVO : matProductVOList){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("prodName", matProductVO.getProdName());
                    jsonObject.put("text", matProductVO.getProdIntroduce());
                    String imgUrl = pathHead;
                    jsonObject.put("imgUrl", imgUrl+matProductVO.getFilePath());
                    jsonObject.put("ord", i++);
                    intrctList.add(jsonObject);
                }
                result.put("success", true);
                result.put("intrctList", intrctList);
                result.put("catgName", matCategory.getCatgName());
            }
        }else{
            result.put("success", false);
            result.put("code", "102");
            result.put("msg", "没有找到分类数据");
        }
        return result;
    }

}

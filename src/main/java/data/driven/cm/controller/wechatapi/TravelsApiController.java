package data.driven.cm.controller.wechatapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.tourism.PictureService;
import data.driven.cm.business.tourism.TravelsService;
import data.driven.cm.entity.tourism.PictureEntity;
import data.driven.cm.entity.tourism.TravelsEntity;
import data.driven.cm.util.FileUtil;
import data.driven.cm.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 景区游记controller
 * @author 何晋凯
 * @date 2018/06/07
 */
@Controller
@RequestMapping(path = "/wechatapi/travels")
public class TravelsApiController {

    private static final Logger logger = LoggerFactory.getLogger(TravelsApiController.class);

    @Autowired
    private PictureService pictureService;
    @Autowired
    private TravelsService travelsService;

    @RequestMapping(path = "/uploadPage")
    public ModelAndView uploadPage(){
        ModelAndView modelAndView = new ModelAndView("/tourism/uploadPage");

        return modelAndView;
    }

    /**
     * 请求游记
     * @param request
     * @param travelsJsonStr
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/uploadTravels", method = RequestMethod.POST)
    public JSONObject uploadTravels(HttpServletRequest request, String travelsJsonStr) {
        JSONObject result = new JSONObject();
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> m = multipartHttpServletRequest.getFileMap();
        List<MultipartFile> files = multipartHttpServletRequest.getFiles("file");

        if(travelsJsonStr != null && travelsJsonStr.length() > 0){
            JSONObject travelsJson = JSONObject.parseObject(travelsJsonStr);
            String wechatUser = travelsJson.getString("wechatUser");
            String scenicSpotId = travelsJson.getString("scenicSpotId");
            String sceneryId = travelsJson.getString("sceneryId");
            JSONArray travelsTextArray = travelsJson.getJSONArray("travelsText");
            int index = 0;
            for (MultipartFile file : files){
                String fileName = file.getOriginalFilename();
                String travelsText = travelsTextArray.getString(index);
                try {
                    String pictureId = UUIDUtil.getUUID();
                    //利用图片对象的主键作为上传后存储到磁盘中的文件名，避免文件重名的冲突。
                    String tempName = pictureId + fileName.substring(fileName.lastIndexOf("."));
                    JSONObject uploadResult = FileUtil.uploadFile(file.getBytes(), tempName);
                    if(uploadResult.getBoolean("success")){
                        String relativePath =  uploadResult.getString("relativePath");
                        //新增图片信息
                        PictureEntity pictureEntity = new PictureEntity();
                        pictureEntity.setPictureId(pictureId);
                        pictureEntity.setFilePath(relativePath);
                        pictureEntity.setRealName(fileName);
                        pictureEntity.setWechatUser(wechatUser);
                        pictureService.addPicture(pictureEntity);

                        TravelsEntity travelsEntity = new TravelsEntity();
                        travelsEntity.setTravelsId(UUIDUtil.getUUID());
                        travelsEntity.setWechatUser(wechatUser);
                        travelsEntity.setSceneryId(sceneryId);
                        travelsEntity.setScenicSpotId(scenicSpotId);
                        travelsEntity.setTravelsText(travelsText);
                        travelsService.addTravels(travelsEntity, pictureEntity.getPictureId());
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                index ++;
            }
        }
        result.put("success", true);
        return result;
    }

}

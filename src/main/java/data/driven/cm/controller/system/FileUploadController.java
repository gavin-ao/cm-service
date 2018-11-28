package data.driven.cm.controller.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.system.PictureService;
import data.driven.cm.common.ApplicationSessionFactory;
import data.driven.cm.entity.system.PictureEntity;
import data.driven.cm.entity.user.UserInfoEntity;
import data.driven.cm.util.FileUtil;
import data.driven.cm.util.JSONUtil;
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
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件上传测试
 * @author 何晋凯
 * @date 2018/06/05
 */
@Controller
@RequestMapping(path = "/system/file")
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private Base64.Encoder encoder = Base64.getEncoder();
    private Base64.Decoder decoder = Base64.getDecoder();

    @Autowired
    private PictureService pictureService;

    @RequestMapping(path = "/fileUploadDemo")
    public ModelAndView fileUploadDemo(){
        ModelAndView modelAndView = new ModelAndView("demo/fileUploadDemo");
        return modelAndView;
    }

    /**
     * 文件上传
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public JSONObject upload(HttpServletRequest request, HttpServletResponse response) {
        UserInfoEntity user = ApplicationSessionFactory.getUser(request, response);
        JSONObject result = new JSONObject();
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartHttpServletRequest.getFiles("file");
        List<PictureEntity> pictureList = new ArrayList<PictureEntity>();
        Date date = new Date();
        for (MultipartFile file : files){
            PictureEntity pictureEntity = new PictureEntity();
            String fileName = file.getOriginalFilename();
            pictureEntity.setPictureId(UUIDUtil.getUUID());
            pictureEntity.setRealName(fileName);
            pictureEntity.setCreator(user.getUserId());
            pictureEntity.setCreateAt(date);
            try {
                fileName = pictureEntity.getPictureId() + fileName.substring(fileName.lastIndexOf("."));
                JSONObject uploadResult = FileUtil.uploadFile(file.getBytes(), fileName);
                pictureEntity.setFilePath(uploadResult.getString("relativePath"));
                pictureList.add(pictureEntity);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                continue;
            }
        }
        if(pictureList.size() > 0){
            pictureService.insertManyPicture(pictureList);
            result.put("idList", pictureList.stream().collect(Collectors.mapping(o -> o.getPictureId(), Collectors.toList())));
        }
        result.put("success", true);
        return result;
    }

    /**
     * 图片上传
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/pictureUpload")
    public JSONObject pictureUpload(HttpServletRequest request, HttpServletResponse response, String pictureJson) {
        if(pictureJson == null){
            return JSONUtil.putMsg(false, "101", "参数为空");
        }
        UserInfoEntity user = ApplicationSessionFactory.getUser(request, response);
        JSONObject result = new JSONObject();
        Date date = new Date();
        PictureEntity pictureEntity = new PictureEntity();
        String fileName = UUIDUtil.getUUID();
        pictureEntity.setPictureId(fileName);
        pictureEntity.setRealName(fileName);
        pictureEntity.setCreator(user.getUserId());
        pictureEntity.setCreateAt(date);
        try {
            fileName = pictureEntity.getPictureId() + ".jpg";
            String[] pictureJsonArr = pictureJson.split(",");
            JSONObject uploadResult = FileUtil.uploadFile(decoder.decode(pictureJsonArr[pictureJsonArr.length - 1]), fileName);
            pictureEntity.setFilePath(uploadResult.getString("relativePath"));
            pictureService.insertPicture(pictureEntity);
            result.put("pictureId", pictureEntity.getPictureId());
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 图片上传
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/pictureUploads")
    public JSONObject pictureUploads(HttpServletRequest request, HttpServletResponse response, String paramJson) {
        if(paramJson == null){
            return JSONUtil.putMsg(false, "101", "参数为空");
        }
        UserInfoEntity user = ApplicationSessionFactory.getUser(request, response);
        JSONObject result = new JSONObject();
        Date date = new Date();
        JSONObject param = JSONObject.parseObject(paramJson);
        List<PictureEntity> pictureList = new ArrayList<PictureEntity>();
        for(String key : param.keySet()){
            if(key.equals("first")){
                String pictureJson = param.getString(key);
                String pictureId = insertPictures(user, date, pictureList, pictureJson);
                result.put(key, pictureId);
            }else{
                JSONArray jsonArray = param.getJSONArray(key);
                if(jsonArray != null && jsonArray.size() > 0){
                    List<String> idList = new ArrayList<String>();
                    for(int i = 0; i < jsonArray.size(); i++){
                        String pictureJson = jsonArray.getString(i);
                        String pictureId = insertPictures(user, date, pictureList, pictureJson);
                        idList.add(pictureId);
                    }
                    result.put(key, idList);
                }
            }
        }
        result.put("success", true);
        return result;
    }

    /**
     * 批量上传图片
     * @param user
     * @param date
     * @param pictureList
     * @param pictureJson
     * @return
     */
    private String insertPictures(UserInfoEntity user, Date date, List<PictureEntity> pictureList, String pictureJson) {
        PictureEntity pictureEntity = new PictureEntity();
        String fileName = UUIDUtil.getUUID();
        pictureEntity.setPictureId(fileName);
        pictureEntity.setRealName(fileName);
        pictureEntity.setCreator(user.getUserId());
        pictureEntity.setCreateAt(date);
        try {
            fileName = pictureEntity.getPictureId() + ".jpg";
            String[] pictureJsonArr = pictureJson.split(",");
            JSONObject uploadResult = FileUtil.uploadFile(decoder.decode(pictureJsonArr[pictureJsonArr.length - 1]), fileName);
            pictureEntity.setFilePath(uploadResult.getString("relativePath"));
            pictureList.add(pictureEntity);
        } catch (Exception e) {
            pictureList.add(pictureEntity);
            logger.error(e.getMessage(), e);
            return "-1";
        }
        return pictureEntity.getPictureId();
    }

}

package data.driven.cm.controller.system;

import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.system.PictureService;
import data.driven.cm.common.ApplicationSessionFactory;
import data.driven.cm.entity.system.PictureEntity;
import data.driven.cm.entity.user.UserInfoEntity;
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
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    @RequestMapping(path = "/pictureUpload", method = RequestMethod.POST)
    public JSONObject pictureUpload(HttpServletRequest request, HttpServletResponse response) {
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

}

package data.driven.cm.business.system.impl;

import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.system.StoreService;
import data.driven.cm.business.user.UserInfoService;
import data.driven.cm.business.wechat.WechatAppInfoService;
import data.driven.cm.common.Constant;
import data.driven.cm.component.Page;
import data.driven.cm.component.PageBean;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.entity.system.StoreEntity;
import data.driven.cm.entity.user.UserInfoEntity;
import data.driven.cm.entity.wechat.WechatAppInfoEntity;
import data.driven.cm.util.DateFormatUtil;
import data.driven.cm.util.JSONUtil;
import data.driven.cm.util.QRCodeUtil;
import data.driven.cm.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 门店service
 * @author hejinkai
 * @date 2018/10/27
 */
@Service
public class StoreServiceImpl implements StoreService {
    /** 门店二维码文件夹名称 **/
    private final static String storeQrcodeFolder = "storeQRcode/";

    @Autowired
    private JDBCBaseDao jdbcBaseDao;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private WechatAppInfoService wechatAppInfoService;

    @Override
    public StoreEntity getStoreById(String storeId) {
        String sql = "select store_id,app_info_id,user_id,store_name,store_addr,province,city,country,stats,manager from sys_store where store_id = ?";
        List<StoreEntity> storeList = jdbcBaseDao.queryList(StoreEntity.class, sql, storeId);
        if(storeList != null && storeList.size() > 0){
            return storeList.get(0);
        }
        return null;
    }

    @Override
    public String getStoreQrCode(String storeId) {
        String sql = "select p.file_path from sys_store s left join sys_picture p on s.picture_id = p.picture_id where s.store_id = ?";
        Object filePath = jdbcBaseDao.getColumn(sql, storeId);
        if(filePath != null){
            return filePath.toString();
        }
        return null;
    }

    @Override
    public Page<StoreEntity> findStorePage(String keyword, String appInfoId, Integer stats, PageBean pageBean) {
        String sql = "select store_id,app_info_id,user_id,store_name,store_addr,province,city,country,picture_id,manager,stats,create_at from sys_store";
        StringBuffer where = new StringBuffer();
        List<Object> paramList = new ArrayList<Object>();
        if(keyword != null){
            where.append(" and store_name like ?");
            paramList.add("%" + keyword.trim() + "%");
        }
        if(appInfoId != null){
            where.append(" and app_info_id = ?");
            paramList.add(appInfoId);
        }
        if(stats != null){
            where.append(" and stats = ?");
            paramList.add(stats);
        }
        if(where.length() > 0){
            sql += " where" + where.delete(0,4);
        }
        sql += " order by create_at desc,store_id";
        return jdbcBaseDao.queryPageWithListParam(StoreEntity.class, pageBean, sql, paramList);
    }

    @Override
    public List<StoreEntity> findStoreTopList(String keyword, String appInfoId) {
        String sql = "select store_id,store_name,store_addr,province,city,country,stats,create_at from sys_store";
        StringBuffer where = new StringBuffer();
        List<Object> paramList = new ArrayList<Object>();
        if(keyword != null){
            where.append(" and store_name like ?");
            paramList.add("%" + keyword.trim() + "%");
        }
        if(appInfoId != null){
            where.append(" and app_info_id = ?");
            paramList.add(appInfoId);
        }
        if(where.length() > 0){
            sql += " where" + where.delete(0,4);
        }
        sql += " order by create_at desc,store_id limit 10";
        return jdbcBaseDao.queryListWithListParam(StoreEntity.class, sql, paramList);
    }

    @Override
    public JSONObject addStore(StoreEntity storeEntity, String managerName, String pwd) {
        Date date = new Date();
        if(storeEntity == null){
            return JSONUtil.putMsg(false, "102", "门店参数为空");
        }
        WechatAppInfoEntity wechatAppInfoEntity = wechatAppInfoService.getAppInfoById(storeEntity.getAppInfoId());
        if(wechatAppInfoEntity == null){
            return JSONUtil.putMsg(false, "103", "小程序为空");
        }
        String pageUrl = wechatAppInfoEntity.getPageUrl();
        if(pageUrl == null){
            return JSONUtil.putMsg(false, "104", "请为小程序添加首页url，用于生成门店二维码");
        }
        storeEntity.setStoreId(UUIDUtil.getUUID());
        pageUrl += storeEntity.getStoreId();
        String pictureId = createStoreQRcode(storeEntity, date, pageUrl);
        if(pictureId != null){
            storeEntity.setPictureId(pictureId);
            storeEntity.setCreateAt(date);
            //新增管理员
            UserInfoEntity userInfoEntity = new UserInfoEntity();
            userInfoEntity.setUserId(UUIDUtil.getUUID());
            userInfoEntity.setUserName(managerName);
            userInfoEntity.setPwd(pwd);
            userInfoEntity.setCreator(storeEntity.getUserId());
            boolean bl = userInfoService.addUser(userInfoEntity);
            if(!bl){
                return JSONUtil.putMsg(false, "101", "门店管理员创建失败");
            }
            storeEntity.setManager(userInfoEntity.getUserId());
            jdbcBaseDao.insert(storeEntity, "sys_store");
            return JSONUtil.putMsg(true, "200", "门店新增成功");
        }
        return JSONUtil.putMsg(false, "102", "二维码创建失败");
    }

    /**
     * 生成门店二维码并存入数据库返回图片id
     * @param storeEntity
     * @param date
     * @param context
     * @return
     */
    private String createStoreQRcode(StoreEntity storeEntity, Date date, String context) {
        SimpleDateFormat sdf = DateFormatUtil.getLocal("yyyyMM");
        String tempFolderPath = sdf.format(date) + File.separator + storeQrcodeFolder;
        String folderPath = Constant.FILE_UPLOAD_PATH + tempFolderPath;
        //判断文件夹不存在则新增
        Path path = Paths.get(folderPath);
        File file = path.toFile();
        if(!file.exists()){
            file.mkdirs();
        }
        String fileUUid = UUIDUtil.getUUID();
        String fileName = folderPath + fileUUid;
        try{
            String sql = "insert into sys_picture(picture_id,file_path,real_name,creator,create_at) values(?,?,?,?,?)";
            String pictureId = UUIDUtil.getUUID();
            jdbcBaseDao.executeUpdate(sql, pictureId, tempFolderPath + fileUUid + "." + QRCodeUtil.fileType, fileUUid + "." + QRCodeUtil.fileType, storeEntity.getUserId(), date);
            QRCodeUtil.createQRCode(context, fileName);
            return pictureId;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public JSONObject updateStore(StoreEntity storeEntity) {
        if(storeEntity != null && storeEntity.getStoreId() != null){
            storeEntity.setManager(null);
            storeEntity.setPictureId(null);
            jdbcBaseDao.update(storeEntity, "sys_store", "store_id", false);
            return JSONUtil.putMsg(true, "200", "修改成功");
        }
        return JSONUtil.putMsg(false, "101", "参数为空");
    }

    @Override
    public JSONObject updateStoreManager(String storeId, String managerName, String pwd) {
        StoreEntity storeEntity = getStoreById(storeId);
        if(storeEntity == null){
            return JSONUtil.putMsg(false, "101", "门店对象为空");
        }
        //新增管理员
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setUserId(UUIDUtil.getUUID());
        userInfoEntity.setUserName(managerName);
        userInfoEntity.setPwd(pwd);
        userInfoEntity.setCreator(storeEntity.getUserId());
        boolean bl = userInfoService.addUser(userInfoEntity);
        if(!bl){
            return JSONUtil.putMsg(false, "102", "门店管理员创建失败");
        }
        String sql = "update sys_store set manager = ? where store_id = ?";
        jdbcBaseDao.executeUpdate(sql, userInfoEntity.getUserId(), storeId);
        return JSONUtil.putMsg(true, "200", "修改成功");
    }

    @Override
    public JSONObject updateStoreManagerPwd(String storeId, String pwd) {
        StoreEntity storeEntity = getStoreById(storeId);
        if(storeEntity == null){
            return JSONUtil.putMsg(false, "101", "门店对象为空");
        }
        String sql = "update sys_user_info set pwd = ? where user_id = ?";
        jdbcBaseDao.executeUpdate(sql, pwd, storeEntity.getManager());
        return JSONUtil.putMsg(true, "200", "修改成功");
    }

    @Override
    public String getStoreIdByCurrentUser(String userId) {
        String sql = "select store_id from sys_store where manager = ?";
        Object obj = jdbcBaseDao.getColumn(sql, userId);
        if (obj != null){
            return obj.toString();
        }
        return null;
    }

    //TODO 删除方法暂不提供
    @Override
    public JSONObject deleteStore(String ids) {
        return null;
    }
}

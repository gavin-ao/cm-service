package data.driven.cm.business.system;

import com.alibaba.fastjson.JSONObject;
import data.driven.cm.component.Page;
import data.driven.cm.component.PageBean;
import data.driven.cm.entity.system.StoreEntity;

/**
 * 门店service
 * @author hejinkai
 * @date 2018/10/27
 */
public interface StoreService {

    /**
     * 根据id查询数据对象
     * @param storeId
     * @return
     */
    public StoreEntity getStoreById(String storeId);

    /**
     * 根据id查询数据对象
     * @param storeId
     * @return
     */
    public String getStoreQrCode(String storeId);

    /**
     * 分页查询数据
     * @param keyword   关键词查询
     * @param appInfoId   小程序id
     * @param pageBean  分页信息
     * @return
     */
    public Page<StoreEntity> findStorePage(String keyword, String appInfoId, PageBean pageBean);

    /**
     * 新增store
     * @param storeEntity
     * @param managerName
     * @param pwd
     * @return
     */
    public JSONObject addStore(StoreEntity storeEntity, String managerName, String pwd);

    /**
     * 修改store - 不能修改门店管理员和密码
     * @param storeEntity
     * @return
     */
    public JSONObject updateStore(StoreEntity storeEntity);

    /**
     * 修改门店管理员
     * @param storeId
     * @param managerName
     * @param pwd
     * @return
     */
    public JSONObject updateStoreManager(String storeId, String managerName, String pwd);

    /**
     * 修改门店管理员的密码
     * @param storeId
     * @param pwd
     * @return
     */
    public JSONObject updateStoreManagerPwd(String storeId, String pwd);

    /**
     * 根据当前用户获取门店id  -  如果当前登录人是门店管理员的话
     * @param userId
     * @return
     */
    public String getStoreIdByCurrentUser(String userId);

    /**
     * 删除门店 - 待定
     * @return
     */
    public JSONObject deleteStore(String ids);
}
